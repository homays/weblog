package com.arrebol.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.arrebol.common.domain.dos.WikiCatalogDO;
import com.arrebol.common.domain.dos.WikiDO;
import com.arrebol.common.domain.mapper.WikiCatalogMapper;
import com.arrebol.common.domain.mapper.WikiMapper;
import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.wiki.FindWikiListRspVO;
import com.arrebol.web.service.WikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

}
