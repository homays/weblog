package com.arrebol.admin.convert;

import com.arrebol.admin.model.vo.wiki.FindWikiPageListRspVO;
import com.arrebol.common.domain.dos.WikiDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WikiConvert {
    /**
     * 初始化 convert 实例
     */
    WikiConvert INSTANCE = Mappers.getMapper(WikiConvert.class);

    /**
     * WikiDO -> FindWikiPageListRspVO
     */
    @Mapping(target = "isTop", expression = "java(bean.getWeight() > 0)")
    FindWikiPageListRspVO convertDO2VO(WikiDO bean);

}