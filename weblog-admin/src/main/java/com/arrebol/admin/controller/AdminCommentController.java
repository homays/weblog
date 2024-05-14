package com.arrebol.admin.controller;

import com.arrebol.admin.model.vo.comment.DeleteCommentReqVO;
import com.arrebol.admin.model.vo.comment.ExamineCommentReqVO;
import com.arrebol.admin.model.vo.comment.FindCommentPageListReqVO;
import com.arrebol.admin.service.AdminCommentService;
import com.arrebol.common.aspect.ApiOperationLog;
import com.arrebol.common.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/comment")
@Api(tags = "Admin 评论模块")
public class AdminCommentController {

    @Resource
    private AdminCommentService commentService;

    @PostMapping("/list")
    @ApiOperation(value = "查询评论分页数据")
    @ApiOperationLog(description = "查询评论分页数据")
    public Response findCommentPageList(@RequestBody @Validated FindCommentPageListReqVO findCommentPageListReqVO) {
        return commentService.findCommentPageList(findCommentPageListReqVO);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "评论删除")
    @ApiOperationLog(description = "评论删除")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response deleteComment(@RequestBody @Validated DeleteCommentReqVO deleteCommentReqVO) {
        return commentService.deleteComment(deleteCommentReqVO);
    }

    @PostMapping("/examine")
    @ApiOperation(value = "评论审核")
    @ApiOperationLog(description = "评论审核")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Response examinePass(@RequestBody @Validated ExamineCommentReqVO examineCommentReqVO) {
        return commentService.examine(examineCommentReqVO);
    }

}