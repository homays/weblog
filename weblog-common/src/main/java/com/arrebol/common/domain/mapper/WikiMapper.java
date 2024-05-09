package com.arrebol.common.domain.mapper;

import com.arrebol.common.domain.dos.WikiDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

public interface WikiMapper extends BaseMapper<WikiDO> {

    /**
     * 根据知识库标题查询知识库
     */
    default WikiDO selectByTitle(String title) {
        return selectOne(Wrappers.<WikiDO>lambdaQuery().eq(WikiDO::getTitle, title));
    }

}