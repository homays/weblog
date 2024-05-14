package com.arrebol.common.enums;

import com.arrebol.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义错误码枚举
 *
 * @author Arrebol
 * @date 2024/1/19
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("10001", "参数错误"),

    // ----------- 业务异常状态码 -----------
    //PRODUCT_NOT_FOUND("20000", "该产品不存在（测试使用）"),
    LOGIN_FAIL("20000", "登录失败"),
    USERNAME_OR_PWD_ERROR("20101", "用户名或密码错误"),
    UNAUTHORIZED("20102", "无访问权限，请先登录！"),
    USERNAME_NOT_FOUND("20103", "该用户不存在"),
    FORBIDDEN("20104", "演示账号仅支持查询操作！"),
    FILE_UPLOAD_FAILED("20201", "文件上传失败！"),
    CATEGORY_NAME_IS_EXISTED("20301", "该分类已存在，请勿重复添加！"),
    CATEGORY_NOT_EXISTED("20302", "提交的分类不存在！"),
    CATEGORY_CAN_NOT_DELETE("20303", "该分类下包含文章，请先删除对应文章，才能删除！"),
    TAG_NOT_EXISTED("20401", "该标签不存在！"),
    TAG_EXISTED("20402", "该标签已存在，请勿重复添加！"),
    TAG_CAN_NOT_DELETE("20403", "该标签下包含文章，请先删除对应文章，才能删除！"),
    ARTICLE_NOT_FOUND("20501", "该文章不存在！"),
    ARTICLE_IS_IN_WIKI("20502", "该文章在知识库中存在，请先在知识库中移除该文章！"),
    WIKI_EXISTED("20601", "该知识库已存在"),
    WIKI_NOT_FOUND("20602", "该知识库不存在"),
    NOT_QQ_NUMBER("20701", "QQ 号格式不正确"),
    COMMENT_CONTAIN_SENSITIVE_WORD("20702", "评论内容中包含敏感词，请重新编辑后再提交"),
    COMMENT_WAIT_EXAMINE("20703", "评论已提交, 等待博主审核通过"),
    COMMENT_NOT_FOUND("20704", "该评论不存在"),
    COMMENT_STATUS_NOT_WAIT_EXAMINE("20705", "该评论未处于待审核状态");

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

}
