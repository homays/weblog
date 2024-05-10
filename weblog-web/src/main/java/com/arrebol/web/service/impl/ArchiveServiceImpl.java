package com.arrebol.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.arrebol.common.domain.dos.ArticleDO;
import com.arrebol.common.domain.mapper.ArticleMapper;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.arrebol.web.convert.ArticleConvert;
import com.arrebol.web.model.vo.archive.FindArchiveArticlePageListReqVO;
import com.arrebol.web.model.vo.archive.FindArchiveArticlePageListRspVO;
import com.arrebol.web.model.vo.archive.FindArchiveArticleRspVO;
import com.arrebol.web.service.ArchiveService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/2/4
 */
@Service
@RequiredArgsConstructor
public class ArchiveServiceImpl implements ArchiveService {

    private final ArticleMapper articleMapper;

    @Override
    public Response findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO) {
        Long current = findArchiveArticlePageListReqVO.getCurrent();
        Long size = findArchiveArticlePageListReqVO.getSize();

        // 分页查询
        IPage<ArticleDO> page = articleMapper.selectPageList(current, size, null, null, null, null);
        List<ArticleDO> articleDOS = page.getRecords();

        List<FindArchiveArticlePageListRspVO> vos = Lists.newArrayList();
        if (ObjectUtil.isNotEmpty(articleDOS)) {
            // DO 转 VO
            List<FindArchiveArticleRspVO> archiveArticleRspVOS =  articleDOS.stream()
                    .map(ArticleConvert.INSTANCE::convertDO2ArchiveArticleVO)
                    .collect(Collectors.toList());

            // 按创建的月份进行分组
            Map<YearMonth, List<FindArchiveArticleRspVO>> map = archiveArticleRspVOS.stream().collect(Collectors.groupingBy(FindArchiveArticleRspVO::getCreateMonth));
            // 使用 TreeMap 按月份倒序排列
            Map<YearMonth, List<FindArchiveArticleRspVO>> sortedMap = new TreeMap<>(Collections.reverseOrder());
            sortedMap.putAll(map);

            // 遍历排序后的 Map，将其转换为归档 VO
            sortedMap.forEach((k, v) -> vos.add(FindArchiveArticlePageListRspVO.builder().month(k).articles(v).build()));
        }
        return PageResponse.success(page, vos);
    }
}
