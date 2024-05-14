package com.arrebol.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.arrebol.admin.convert.CommentConvert;
import com.arrebol.admin.model.vo.comment.FindCommentPageListReqVO;
import com.arrebol.admin.model.vo.comment.FindCommentPageListRspVO;
import com.arrebol.admin.service.AdminCommentService;
import com.arrebol.common.domain.dos.CommentDO;
import com.arrebol.common.domain.mapper.CommentMapper;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class AdminCommentServiceImpl implements AdminCommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Response findCommentPageList(FindCommentPageListReqVO findCommentPageListReqVO) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findCommentPageListReqVO.getCurrent();
        Long size = findCommentPageListReqVO.getSize();
        LocalDate startDate = findCommentPageListReqVO.getStartDate();
        LocalDate endDate = findCommentPageListReqVO.getEndDate();
        String routerUrl = findCommentPageListReqVO.getRouterUrl();
        Integer status = findCommentPageListReqVO.getStatus();

        // 执行分页查询
        Page<CommentDO> commentDOPage = commentMapper.selectPageList(current, size, routerUrl, startDate, endDate, status);

        List<CommentDO> commentDOS = commentDOPage.getRecords();

        // DO 转 VO
        List<FindCommentPageListRspVO> vos = null;
        if (CollUtil.isNotEmpty(commentDOS)) {
            vos = commentDOS.stream()
                    .map(commentDO -> CommentConvert.INSTANCE.convertDO2VO(commentDO))
                    .collect(Collectors.toList());
        }

        return PageResponse.success(commentDOPage, vos);
    }
}
