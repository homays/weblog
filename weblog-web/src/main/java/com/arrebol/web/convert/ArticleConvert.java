package com.arrebol.web.convert;

import com.arrebol.common.domain.dos.ArticleDO;
import com.arrebol.web.model.vo.article.FindIndexArticlePageListRspVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/29
 */
@Mapper
public interface ArticleConvert {
    /**
     * 初始化 convert 实例
     */
    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);

    /**
     * 将 DO 转化为 VO
     * @param bean
     * @return
     */
    @Mapping(target = "createDate", expression = "java(java.time.LocalDate.from(bean.getCreateTime()))")
    FindIndexArticlePageListRspVO convertDO2VO(ArticleDO bean);

}
