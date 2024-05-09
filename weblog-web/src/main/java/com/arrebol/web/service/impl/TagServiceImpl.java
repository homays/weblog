package com.arrebol.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.arrebol.common.domain.dos.ArticleDO;
import com.arrebol.common.domain.dos.ArticleTagRelDO;
import com.arrebol.common.domain.dos.TagDO;
import com.arrebol.common.domain.mapper.ArticleMapper;
import com.arrebol.common.domain.mapper.ArticleTagRelMapper;
import com.arrebol.common.domain.mapper.TagMapper;
import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.exception.BizException;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.arrebol.web.convert.ArticleConvert;
import com.arrebol.web.model.vo.tag.FindTagArticlePageListReqVO;
import com.arrebol.web.model.vo.tag.FindTagArticlePageListRspVO;
import com.arrebol.web.model.vo.tag.FindTagListRspVO;
import com.arrebol.web.service.TagService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/31
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;
    private final ArticleTagRelMapper articleTagRelMapper;
    private final ArticleMapper articleMapper;

    @Override
    public Response findTagList() {
        // 查询所有标签
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());

        // DO 转 VO
        List<FindTagListRspVO> vos = null;
        if (CollUtil.isNotEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .articlesTotal(tagDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }

    @Override
    public Response findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO) {
        Long current = findTagArticlePageListReqVO.getCurrent();
        Long size = findTagArticlePageListReqVO.getSize();
        // 标签 ID
        Long tagId = findTagArticlePageListReqVO.getId();

        // 判断该标签是否存在
        TagDO tagDO = tagMapper.selectById(tagId);
        if (ObjectUtil.isNull(tagDO)) {
            throw new BizException(ResponseCodeEnum.TAG_NOT_EXISTED);
        }

        // 先查询该标签下所有关联的文章 ID
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByTagId(tagId);

        // 若该标签下未发布任何文章
        if (CollUtil.isEmpty(articleTagRelDOS)) {
            return PageResponse.success(null, null);
        }

        // 提取所有文章 ID
        List<Long> articleIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getArticleId).collect(Collectors.toList());

        // 根据文章 ID 集合查询文章分页数据
        Page<ArticleDO> page = articleMapper.selectPageListByArticleIds(current, size, articleIds);
        List<ArticleDO> articleDOS = page.getRecords();

        // DO 转 VO
        List<FindTagArticlePageListRspVO> vos = null;
        if (CollUtil.isNotEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertDO2TagArticleVO)
                    .collect(Collectors.toList());
        }

        return PageResponse.success(page, vos);
    }
}
