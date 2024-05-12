package com.arrebol.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.arrebol.common.domain.dos.WikiCatalogDO;
import com.arrebol.common.domain.dos.WikiDO;
import com.arrebol.common.domain.mapper.WikiCatalogMapper;
import com.arrebol.common.domain.mapper.WikiMapper;
import com.arrebol.common.enums.WikiCatalogLevelEnum;
import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.article.FindPreNextArticleRspVO;
import com.arrebol.web.model.vo.wiki.*;
import com.arrebol.web.service.WikiService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WikiServiceImpl implements WikiService {

    @Resource
    private WikiMapper wikiMapper;
    @Resource
    private WikiCatalogMapper wikiCatalogMapper;

    @Override
    public Response findWikiList() {
        // 查询已经发布的知识库
        List<WikiDO> wikiDOS = wikiMapper.selectPublished();

        List<FindWikiListRspVO> vos = null;
        if (CollUtil.isNotEmpty(wikiDOS)) {
            vos = wikiDOS.stream()
                    .map(wikiDO -> FindWikiListRspVO.builder()
                            .id(wikiDO.getId())
                            .title(wikiDO.getTitle())
                            .cover(wikiDO.getCover())
                            .summary(wikiDO.getSummary())
                            .isTop(wikiDO.getWeight() > 0)
                            .build())
                    .collect(Collectors.toList());

            // 设置每个知识库的第一篇文章 ID，方便前端跳转
            vos.forEach(vo -> {
                Long wikiId = vo.getId();
                WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectFirstArticleId(wikiId);
                vo.setFirstArticleId(Objects.nonNull(wikiCatalogDO) ? wikiCatalogDO.getArticleId() : null);
            });
        }
        return Response.success(vos);
    }

    @Override
    public Response findWikiCatalogList(FindWikiCatalogListReqVO findWikiCatalogListReqVO) {
        Long wikiId = findWikiCatalogListReqVO.getId();
        // 通过知识库id 获取知识库
        List<WikiCatalogDO> wikiCatalogDOS = wikiCatalogMapper.selectByWikiId(wikiId);

        List<FindWikiCatalogListRspVO> vos = null;
        if (CollUtil.isNotEmpty(wikiCatalogDOS)) {
            vos = Lists.newArrayList();

            // 一级目录
            List<WikiCatalogDO> level1Catalogs = wikiCatalogDOS.stream()
                    .filter(catalogDO -> Objects.equals(catalogDO.getLevel(), WikiCatalogLevelEnum.ONE.getValue()))
                    .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                    .collect(Collectors.toList());

            // 构造 VO 对象, 并添加到 vos 集合中
            for (WikiCatalogDO level1Catalog : level1Catalogs) {
                vos.add(FindWikiCatalogListRspVO.builder()
                        .id(level1Catalog.getId())
                        .articleId(level1Catalog.getArticleId())
                        .title(level1Catalog.getTitle())
                        .level(level1Catalog.getLevel())
                        .build());
            }

            // 二级目录
            vos.forEach(level1Catalog -> {
                Long parentId = level1Catalog.getId();
                List<WikiCatalogDO> level2CatalogDOS  = wikiCatalogDOS.stream().filter(catalogDO -> Objects.equals(catalogDO.getParentId(), parentId) &&
                                Objects.equals(catalogDO.getLevel(), WikiCatalogLevelEnum.TWO.getValue()))
                        .sorted(Comparator.comparing(WikiCatalogDO::getSort))
                        .collect(Collectors.toList());

                // 设置子目录数据到 children 字段中
                List<FindWikiCatalogListRspVO> level2Catalogs = level2CatalogDOS.stream()
                        .map(catalogDO -> FindWikiCatalogListRspVO.builder()
                                .id(catalogDO.getId())
                                .articleId(catalogDO.getArticleId())
                                .title(catalogDO.getTitle())
                                .level(catalogDO.getLevel())
                                .build())
                        .collect(Collectors.toList());
                level1Catalog.setChildren(level2Catalogs);
            });
        }
        return Response.success(vos);
    }

    @Override
    public Response findArticlePreNext(FindWikiArticlePreNextReqVO findWikiArticlePreNextReqVO) {
        Long wikiId = findWikiArticlePreNextReqVO.getId();
        Long articleId = findWikiArticlePreNextReqVO.getArticleId();

        FindWikiArticlePreNextRspVO vo = new FindWikiArticlePreNextRspVO();
        WikiCatalogDO wikiCatalogDO = wikiCatalogMapper.selectByWikiIdAndArticleId(wikiId, articleId);

        // 构建上一篇文章
        WikiCatalogDO preArticle = wikiCatalogMapper.selectPreArticle(wikiId, wikiCatalogDO.getId());
        if (ObjectUtil.isNotNull(preArticle)) {
            FindPreNextArticleRspVO preArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(preArticle.getArticleId())
                    .articleTitle(preArticle.getTitle())
                    .build();
            vo.setPreArticle(preArticleVO);
        }

        // 构建下一篇文章
        WikiCatalogDO nextArticle = wikiCatalogMapper.selectNextArticle(wikiId, wikiCatalogDO.getId());
        if (ObjectUtil.isNotNull(nextArticle)) {
            FindPreNextArticleRspVO nextArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(nextArticle.getArticleId())
                    .articleTitle(nextArticle.getTitle())
                    .build();
            vo.setNextArticle(nextArticleVO);
        }

        return Response.success(vo);
    }

}
