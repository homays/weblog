package com.arrebol.common.domain.mapper;

import com.arrebol.common.config.InsertBatchMapper;
import com.arrebol.common.domain.dos.WikiCatalogDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.List;

public interface WikiCatalogMapper extends InsertBatchMapper<WikiCatalogDO> {

    /**
     * 根据某个知识库下所有目录
     */
    default List<WikiCatalogDO> selectByWikiId(Long wikiId) {
        return selectList(Wrappers.<WikiCatalogDO>lambdaQuery()
                .eq(WikiCatalogDO::getWikiId, wikiId)
        );
    }

    /**
     * 删除知识库
     */
    default int deleteByWikiId(Long wikiId) {
        return delete(Wrappers.<WikiCatalogDO>lambdaQuery()
                .eq(WikiCatalogDO::getWikiId, wikiId));
    }

}