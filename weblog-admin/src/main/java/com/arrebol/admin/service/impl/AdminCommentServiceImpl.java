package com.arrebol.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.arrebol.admin.convert.CommentConvert;
import com.arrebol.admin.event.UpdateCommentEvent;
import com.arrebol.admin.model.vo.comment.DeleteCommentReqVO;
import com.arrebol.admin.model.vo.comment.ExamineCommentReqVO;
import com.arrebol.admin.model.vo.comment.FindCommentPageListReqVO;
import com.arrebol.admin.model.vo.comment.FindCommentPageListRspVO;
import com.arrebol.admin.service.AdminCommentService;
import com.arrebol.common.domain.dos.CommentDO;
import com.arrebol.common.domain.mapper.CommentMapper;
import com.arrebol.common.enums.CommentStatusEnum;
import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.exception.BizException;
import com.arrebol.common.util.PageResponse;
import com.arrebol.common.util.Response;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminCommentServiceImpl implements AdminCommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ApplicationEventPublisher eventPublisher;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteComment(DeleteCommentReqVO deleteCommentReqVO) {
        Long commentId = deleteCommentReqVO.getId();

        CommentDO commentDO = commentMapper.selectById(commentId);
        if (ObjectUtil.isNull(commentDO)) {
            log.warn("该评论不存在, commentId: {}", commentId);
            throw new BizException(ResponseCodeEnum.COMMENT_NOT_FOUND);
        }

        // 删除评论
        commentMapper.deleteById(commentId);

        Long replyCommentId = commentDO.getReplyCommentId();

        if (ObjectUtil.isNull(replyCommentId)) {
            // 删除子评论
            commentMapper.deleteByParentCommentId(commentId);
        } else {
            // 删除此评论, 以及此评论下的所有回复
            deleteAllChildComment(commentId);
        }

        return Response.success();
    }

    @Override
    public Response examine(ExamineCommentReqVO examineCommentReqVO) {
        Long commentId = examineCommentReqVO.getId();
        Integer status = examineCommentReqVO.getStatus();
        String reason = examineCommentReqVO.getReason();

        // 根据提交的评论 ID 查询该条评论
        CommentDO commentDO = commentMapper.selectById(commentId);

        // 判空
        if (ObjectUtil.isNull(commentDO)) {
            log.warn("该评论不存在, commentId: {}", commentId);
            throw new BizException(ResponseCodeEnum.COMMENT_NOT_FOUND);
        }

        // 评论当前状态
        Integer currStatus = commentDO.getStatus();

        // 若未处于待审核状态
        if (ObjectUtil.notEqual(currStatus, CommentStatusEnum.WAIT_EXAMINE.getCode())) {
            log.warn("该评论未处于待审核状态, commentId: {}", commentId);
            throw new BizException(ResponseCodeEnum.COMMENT_STATUS_NOT_WAIT_EXAMINE);
        }

        // 更新评论
        commentMapper.updateById(CommentDO.builder()
                .id(commentId)
                .status(status)
                .reason(reason)
                .updateTime(LocalDateTime.now())
                .build());

        // 发送文章发布事件
        eventPublisher.publishEvent(new UpdateCommentEvent(this, commentId));

        return Response.success();
    }

    /**
     * 递归删除所有子评论
     */
    private void deleteAllChildComment(Long commentId) {
        // 查询此评论的所有回复
        List<CommentDO> childCommentDOS = commentMapper.selectByReplyCommentId(commentId);

        if (CollUtil.isEmpty(childCommentDOS))
            return;

        // 循环递归删除
        childCommentDOS.forEach(childCommentDO -> {
            Long childCommentId = childCommentDO.getId();

            commentMapper.deleteById(childCommentId);
            // 递归调用
            deleteAllChildComment(childCommentId);
        });
    }

}
