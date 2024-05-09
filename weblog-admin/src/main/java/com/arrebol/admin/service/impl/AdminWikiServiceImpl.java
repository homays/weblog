package com.arrebol.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.arrebol.admin.convert.WikiConvert;
import com.arrebol.admin.model.vo.wiki.*;
import com.arrebol.admin.service.AdminWikiService;
import com.arrebol.common.domain.dos.ArticleDO;
import com.arrebol.common.domain.dos.WikiCatalogDO;
import com.arrebol.common.domain.dos.WikiDO;
import com.arrebol.common.domain.mapper.ArticleMapper;
import com.arrebol.common.domain.mapper.WikiCatalogMapper;
import com.arrebol.common.domain.mapper.WikiMapper;
import com.arrebol.common.enums.ArticleTypeEnum;
import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.enums.WikiCatalogLevelEnum;
import com.arrebol.common.exception.BizException;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminWikiServiceImpl implements AdminWikiService {

    @Resource
    private WikiMapper wikiMapper;
    @Resource
    private WikiCatalogMapper wikiCatalogMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addWiki(AddWikiReqVO addWikiReqVO) {
        WikiDO wikiDO1 = wikiMapper.selectByTitle(addWikiReqVO.getTitle());
        if (ObjectUtil.isNotNull(wikiDO1)) {
            throw new BizException(ResponseCodeEnum.WIKI_EXISTED);
        }
        WikiDO wikiDO = WikiDO.builder()
                .title(addWikiReqVO.getTitle())
                .cover(addWikiReqVO.getCover())
                .summary(addWikiReqVO.getSummary())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        // 新增知识库
        wikiMapper.insert(wikiDO);
        // 获取新增记录的主键 ID
        Long wikiId = wikiDO.getId();

        // 初始化默认目录
        // > 概述
        // > 基础
        wikiCatalogMapper.insert(WikiCatalogDO.builder().wikiId(wikiId).title("概述").sort(1).build());
        wikiCatalogMapper.insert(WikiCatalogDO.builder().wikiId(wikiId).title("基础").sort(2).build());
        return Response.success();
    }

    @Override
    public Response deleteWiki(DeleteWikiReqVO deleteWikiReqVO) {
        Long wikiId = deleteWikiReqVO.getId();
        // 删除知识库
        int count = wikiMapper.deleteById(wikiId);
        // 知识库不存在
        if (count == 0) {
            throw new BizException(ResponseCodeEnum.WIKI_NOT_FOUND);
        }
        // 查询此知识库下所有目录，并过滤目录中所有文章的 ID
        List<Long> articleIds = wikiCatalogMapper.selectByWikiId(wikiId).stream().filter(item -> Objects.nonNull(item.getArticleId())
                        && Objects.equals(item.getLevel(), WikiCatalogLevelEnum.TWO.getValue()))
                .map(WikiCatalogDO::getId).collect(Collectors.toList());
        // 更新文章类型为普通
        if (CollUtil.isNotEmpty(articleIds)) {
            articleMapper.updateByIds(ArticleDO.builder()
                    .type(ArticleTypeEnum.NORMAL.getValue())
                    .build(), articleIds);
        }
        // 删除目录
        wikiCatalogMapper.deleteByWikiId(wikiId);
        return Response.success();
    }

    @Override
    public Response findWikiPageList(FindWikiPageListReqVO findWikiPageListReqVO) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findWikiPageListReqVO.getCurrent();
        Long size = findWikiPageListReqVO.getSize();
        // 查询条件
        String title = findWikiPageListReqVO.getTitle();
        LocalDate startDate = findWikiPageListReqVO.getStartDate();
        LocalDate endDate = findWikiPageListReqVO.getEndDate();

        // 执行分页查询
        Page<WikiDO> wikiDOPage = wikiMapper.selectPageList(current, size, title, startDate, endDate, null);

        // 获取查询记录
        List<WikiDO> wikiDOS = wikiDOPage.getRecords();

        // DO 转 VO
        List<FindWikiPageListRspVO> vos = null;
        if (CollUtil.isNotEmpty(wikiDOS)) {
            vos = wikiDOS.stream()
                    .map(articleDO -> WikiConvert.INSTANCE.convertDO2VO(articleDO))
                    .collect(Collectors.toList());
        }

        return PageResponse.success(wikiDOPage, vos);
    }

    @Override
    public Response updateWikiIsTop(UpdateWikiIsTopReqVO updateWikiIsTopReqVO) {
        Long wikiId = updateWikiIsTopReqVO.getId();
        Boolean isTop = updateWikiIsTopReqVO.getIsTop();

        // 默认权重值为 0 ，即不参与置顶
        int weight = 0;
        // 若设置为置顶
        if (Boolean.TRUE.equals(isTop)) {
            // 查询最大权重值
            WikiDO wikiDO = wikiMapper.selectMaxWeight();
            Integer maxWeight = wikiDO.getWeight();
            // 最大权重值加一
            weight = maxWeight + 1;
        }

        // 更新该知识库的权重值
        wikiMapper.updateById(WikiDO.builder().id(wikiId).weight(weight).build());
        return Response.success();
    }

    @Override
    public Response updateWikiIsPublish(UpdateWikiIsPublishReqVO updateWikiIsPublishReqVO) {
        Long wikiId = updateWikiIsPublishReqVO.getId();
        Boolean isPublish = updateWikiIsPublishReqVO.getIsPublish();
        // 更新发布状态
        wikiMapper.updateById(WikiDO.builder().id(wikiId).isPublish(isPublish).build());
        return Response.success();
    }

}