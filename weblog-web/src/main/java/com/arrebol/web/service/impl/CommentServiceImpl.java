package com.arrebol.web.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.arrebol.common.enums.ResponseCodeEnum;
import com.arrebol.common.exception.BizException;
import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.comment.FindQQUserInfoReqVO;
import com.arrebol.web.model.vo.comment.FindQQUserInfoRspVO;
import com.arrebol.web.service.CommentService;
import com.arrebol.web.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public Response findQQUserInfo(FindQQUserInfoReqVO findQQUserInfoReqVO) {
        String qq = findQQUserInfoReqVO.getQq();

        // 校验 QQ 号
        if (!StringUtil.isPureNumber(qq)) {
            log.warn("昵称输入的格式不是 QQ 号: {}", qq);
            throw new BizException(ResponseCodeEnum.NOT_QQ_NUMBER);
        }

        // 请求第三方接口
        String url = String.format("https://api.qjqq.cn/api/qqinfo?qq=%s", qq);
        String result = restTemplate.getForObject(url, String.class);

        log.info("通过 QQ 号获取用户信息: {}", result);

        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (Objects.equals(Integer.valueOf(jsonObject.getStr("code")), HttpStatus.OK.value())) {
            // 获取用户头像、昵称、邮箱
            return Response.success(FindQQUserInfoRspVO.builder()
                    .avatar(jsonObject.getStr("imgurl"))
                    .nickname(jsonObject.getStr("name"))
                    .mail(jsonObject.getStr("mail"))
                    .build());
        }
        return Response.fail();
    }
}
