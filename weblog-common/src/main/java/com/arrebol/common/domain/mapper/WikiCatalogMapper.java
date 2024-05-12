package com.arrebol.common.domain.mapper;

import com.arrebol.common.config.InsertBatchMapper;
import com.arrebol.common.domain.dos.WikiCatalogDO;
import com.arrebol.common.enums.WikiCatalogLevelEnum;
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

    /**
     * 查询知识库目录中第一篇文章
     */
    default WikiCatalogDO selectFirstArticleId(Long wikiId) {
        return selectOne(Wrappers.<WikiCatalogDO>lambdaQuery()
                .eq(WikiCatalogDO::getWikiId, wikiId) // 查询指定知识库 id
                .eq(WikiCatalogDO::getLevel, WikiCatalogLevelEnum.TWO.getValue()) // 查询二级目录
                .isNotNull(WikiCatalogDO::getArticleId) // article_id 字段不能为空
                .orderByAsc(WikiCatalogDO::getId) // 按 id 增序排列
                .last("LIMIT 1") // 仅查询一条
        );
    }

}