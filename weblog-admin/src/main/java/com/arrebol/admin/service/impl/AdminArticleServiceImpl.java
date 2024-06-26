package com.arrebol.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.arrebol.admin.convert.ArticleDetailConvert;
import com.arrebol.admin.event.DeleteArticleEvent;
import com.arrebol.admin.event.PublishArticleEvent;
import com.arrebol.admin.event.UpdateArticleEvent;
import com.arrebol.admin.model.vo.article.*;
import com.arrebol.admin.service.AdminArticleService;
import com.arrebol.common.domain.dos.*;
import com.arrebol.common.domain.mapper.*;
import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.exception.BizException;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AdminArticleServiceImpl implements AdminArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleContentMapper articleContentMapper;
    @Resource
    private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private ArticleTagRelMapper articleTagRelMapper;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private WikiCatalogMapper wikiCatalogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response publishArticle(PublishArticleReqVO publishArticleReqVO) {
        // 1. VO 转 ArticleDO, 并保存
        ArticleDO articleDO = ArticleDO.builder()
                .title(publishArticleReqVO.getTitle())
                .cover(publishArticleReqVO.getCover())
                .summary(publishArticleReqVO.getSummary())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        articleMapper.insert(articleDO);

        // 拿到插入记录的主键 ID
        Long articleId = articleDO.getId();

        // 2. VO 转 ArticleContentDO，并保存
        ArticleContentDO articleContentDO = ArticleContentDO.builder()
                .articleId(articleId)
                .content(publishArticleReqVO.getContent())
                .build();
        articleContentMapper.insert(articleContentDO);

        // 3. 处理文章关联的分类
        Long categoryId = publishArticleReqVO.getCategoryId();

        // 3.1 校验提交的分类是否真实存在
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 分类不存在, categoryId: {}", categoryId);
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }

        ArticleCategoryRelDO articleCategoryRelDO = ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(categoryId)
                .build();
        articleCategoryRelMapper.insert(articleCategoryRelDO);

        // 4. 保存文章关联的标签集合
        List<String> publishTags = publishArticleReqVO.getTags();
        insertTags(articleId, publishTags);

        // 发送文章发布事件
        eventPublisher.publishEvent(new PublishArticleEvent(this, articleId));

        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO) {
        Long articleId = deleteArticleReqVO.getId();
        WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectByArticleId(articleId);
        if (ObjectUtil.isNotNull(wikiCatalogDO)) {
            throw new BizException(ResponseCodeEnum.ARTICLE_IS_IN_WIKI);
        }
        // 1. 删除文章
        articleMapper.delete(Wrappers.lambdaQuery(ArticleDO.class)
                .eq(ArticleDO::getId, articleId));
        // 2. 删除文章内容
        articleContentMapper.delete(Wrappers.lambdaQuery(ArticleContentDO.class)
                .eq(ArticleContentDO::getArticleId, articleId));
        // 3. 删除文章与分类关联
        articleCategoryRelMapper.delete(Wrappers.lambdaQuery(ArticleCategoryRelDO.class)
                .eq(ArticleCategoryRelDO::getArticleId, articleId));
        // 4. 删除文章与标签关联
        articleTagRelMapper.delete(Wrappers.lambdaQuery(ArticleTagRelDO.class)
                .eq(ArticleTagRelDO::getArticleId, articleId));

        // 发布文章删除事件
        eventPublisher.publishEvent(new DeleteArticleEvent(this, articleId));
        return Response.success();
    }

    @Override
    public Response findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findArticlePageListReqVO.getCurrent();
        Long size = findArticlePageListReqVO.getSize();
        String title = findArticlePageListReqVO.getTitle();
        LocalDate startDate = findArticlePageListReqVO.getStartDate();
        LocalDate endDate = findArticlePageListReqVO.getEndDate();
        Integer type = findArticlePageListReqVO.getType();

        // 执行分页查询
        Page<ArticleDO> articleDOPage = articleMapper.selectPageList(current, size, title, startDate, endDate, type);

        List<ArticleDO> articleDOS = articleDOPage.getRecords();

        // DO 转 VO
        List<FindArticlePageListRspVO> vos = null;
        if (CollUtil.isNotEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(articleDO -> FindArticlePageListRspVO.builder()
                            .id(articleDO.getId())
                            .title(articleDO.getTitle())
                            .cover(articleDO.getCover())
                            .createTime(articleDO.getCreateTime())
                            .isTop(articleDO.getWeight() > 0)
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(articleDOPage, vos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long articleId = findArticleDetailReqVO.getId();
        // 查询文章
        LambdaQueryWrapper<ArticleDO> wrapper = Wrappers.lambdaQuery(ArticleDO.class)
                .eq(ArticleDO::getId, articleId);
        ArticleDO articleDO = articleMapper.selectOne(wrapper);
        if (Objects.isNull(articleDO)) {
            log.warn("==> 查询的文章不存在，articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }
        // 查询文章详情
        LambdaQueryWrapper<ArticleContentDO> contentWrapper = Wrappers.lambdaQuery(ArticleContentDO.class)
                .eq(ArticleContentDO::getArticleId, articleId);
        ArticleContentDO articleContentDO = articleContentMapper.selectOne(contentWrapper);
        // 查询文章分类
        LambdaQueryWrapper<ArticleCategoryRelDO> categoryWrapper = Wrappers.lambdaQuery(ArticleCategoryRelDO.class)
                .eq(ArticleCategoryRelDO::getArticleId, articleId);
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectOne(categoryWrapper);
        // 查询文章标签
        LambdaQueryWrapper<ArticleTagRelDO> tagsWrapper = Wrappers.lambdaQuery(ArticleTagRelDO.class)
                .eq(ArticleTagRelDO::getArticleId, articleId);
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectList(tagsWrapper);
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());
        // 封装
        FindArticleDetailRspVO findArticleDetailRspVO = ArticleDetailConvert.INSTANCE.convertDO2VO(articleDO);
        findArticleDetailRspVO.setContent(articleContentDO.getContent());
        findArticleDetailRspVO.setCategoryId(articleCategoryRelDO.getCategoryId());
        findArticleDetailRspVO.setTagIds(tagIds);
        return Response.success(findArticleDetailRspVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateArticle(UpdateArticleReqVO updateArticleReqVO) {
        Long articleId = updateArticleReqVO.getId();

        // 1. VO 转 ArticleDO, 并更新
        ArticleDO articleDO = ArticleDO.builder()
                .id(articleId)
                .title(updateArticleReqVO.getTitle())
                .cover(updateArticleReqVO.getCover())
                .summary(updateArticleReqVO.getSummary())
                .updateTime(LocalDateTime.now())
                .build();
        int count = articleMapper.updateById(articleDO);

        // 根据更新是否成功，来判断该文章是否存在
        if (count == 0) {
            log.warn("==> 该文章不存在, articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_FOUND);
        }

        // 2. VO 转 ArticleContentDO，并更新
        ArticleContentDO articleContentDO = ArticleContentDO.builder()
                .articleId(articleId)
                .content(updateArticleReqVO.getContent())
                .build();
        articleContentMapper.updateByArticleId(articleContentDO);


        // 3. 更新文章分类
        Long categoryId = updateArticleReqVO.getCategoryId();

        // 3.1 校验提交的分类是否真实存在
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 分类不存在, categoryId: {}", categoryId);
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }

        // 先删除该文章关联的分类记录，再插入新的关联关系
        articleCategoryRelMapper.deleteByArticleId(articleId);
        ArticleCategoryRelDO articleCategoryRelDO = ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(categoryId)
                .build();
        articleCategoryRelMapper.insert(articleCategoryRelDO);

        // 4. 保存文章关联的标签集合
        // 先删除该文章对应的标签
        articleTagRelMapper.deleteByArticleId(articleId);
        List<String> publishTags = updateArticleReqVO.getTags();
        insertTags(articleId, publishTags);

        // 发布文章修改事件
        eventPublisher.publishEvent(new UpdateArticleEvent(this, articleId));

        return Response.success();
    }

    @Override
    public Response updateArticleIsTop(UpdateArticleIsTopReqVO updateArticleIsTopReqVO) {
        Long articleId = updateArticleIsTopReqVO.getId();
        Boolean isTop = updateArticleIsTopReqVO.getIsTop();

        // 默认权重为 0
        Integer weight = 0;
        // 若设置为置顶
        if (isTop) {
            // 查询出表中最大的权重值
            ArticleDO articleDO = articleMapper.selectMaxWeight();
            Integer maxWeight = articleDO.getWeight();
            // 最大权重值加一
            weight = maxWeight + 1;
        }

        // 更新该篇文章的权重值
        articleMapper.updateById(ArticleDO.builder()
                .id(articleId)
                .weight(weight)
                .build());

        return Response.success();
    }

    /**
     * 保存标签
     */
    private void insertTags(Long articleId, List<String> publishTags) {
        // 筛选提交的标签（表中不存在的标签）
        List<String> notExistTags = null;
        // 筛选提交的标签（表中已存在的标签）
        List<String> existedTags = null;

        // 查询出所有标签
        List<TagDO> tagDOS = tagMapper.selectList(null);

        // 如果表中还没有添加任何标签
        if (CollectionUtils.isEmpty(tagDOS)) {
            notExistTags = publishTags;
        } else {
            List<String> tagIds = tagDOS.stream().map(tagDO -> String.valueOf(tagDO.getId())).collect(Collectors.toList());
            // 表中已添加相关标签，则需要筛选
            // 通过标签 ID 来筛选，包含对应 ID 则表示提交的标签是表中存在的
            existedTags = publishTags.stream().filter(publishTag -> tagIds.contains(publishTag)).collect(Collectors.toList());
            // 否则则是不存在的
            notExistTags = publishTags.stream().filter(publishTag -> !tagIds.contains(publishTag)).collect(Collectors.toList());

            // 补充逻辑：
            // 还有一种可能：按字符串名称提交上来的标签，也有可能是表中已存在的，比如表中已经有了 Java 标签，用户提交了个 java 小写的标签，需要内部装换为 Java 标签
            Map<String, Long> tagNameIdMap = tagDOS.stream().collect(Collectors.toMap(tagDO -> tagDO.getName().toLowerCase(), TagDO::getId));

            // 使用迭代器进行安全的删除操作
            Iterator<String> iterator = notExistTags.iterator();
            while (iterator.hasNext()) {
                String notExistTag = iterator.next();
                // 转小写, 若 Map 中相同的 key，则表示该新标签是重复标签
                if (tagNameIdMap.containsKey(notExistTag.toLowerCase())) {
                    // 从不存在的标签集合中清除
                    iterator.remove();
                    // 并将对应的 ID 添加到已存在的标签集合
                    existedTags.add(String.valueOf(tagNameIdMap.get(notExistTag.toLowerCase())));
                }
            }
        }

        // 将提交的上来的，已存在于表中的标签，文章-标签关联关系入库
        if (!CollectionUtils.isEmpty(existedTags)) {
            List<ArticleTagRelDO> articleTagRelDOS = Lists.newArrayList();
            existedTags.forEach(tagId -> {
                ArticleTagRelDO articleTagRelDO = ArticleTagRelDO.builder()
                        .articleId(articleId)
                        .tagId(Long.valueOf(tagId))
                        .build();
                articleTagRelDOS.add(articleTagRelDO);
            });
            // 批量插入
            articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
        }

        // 将提交的上来的，不存在于表中的标签，入库保存
        if (!CollectionUtils.isEmpty(notExistTags)) {
            // 需要先将标签入库，拿到对应标签 ID 后，再把文章-标签关联关系入库
            List<ArticleTagRelDO> articleTagRelDOS = Lists.newArrayList();
            notExistTags.forEach(tagName -> {
                TagDO tagDO = TagDO.builder()
                        .name(tagName)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();

                tagMapper.insert(tagDO);

                // 拿到保存的标签 ID
                Long tagId = tagDO.getId();

                // 文章-标签关联关系
                ArticleTagRelDO articleTagRelDO = ArticleTagRelDO.builder()
                        .articleId(articleId)
                        .tagId(tagId)
                        .build();
                articleTagRelDOS.add(articleTagRelDO);
            });
            // 批量插入
            articleTagRelMapper.insertBatchSomeColumn(articleTagRelDOS);
        }
    }
}
