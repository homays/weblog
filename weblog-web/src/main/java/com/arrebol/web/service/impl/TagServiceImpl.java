package com.arrebol.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.arrebol.common.domain.dos.TagDO;
import com.arrebol.common.domain.mapper.TagMapper;
import com.arrebol.common.util.Response;
import com.arrebol.web.model.vo.tag.FindTagListRspVO;
import com.arrebol.web.service.TagService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author Arrebol
 * @date 2024/1/31
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    public Response findTagList() {
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());
        List<FindTagListRspVO> vos = null;
        if (CollUtil.isNotEmpty(tagDOS)) {
            vos = tagDOS.stream().map(item -> BeanUtil.copyProperties(item, FindTagListRspVO.class))
                    .collect(Collectors.toList());
        }
        return Response.success(vos);
    }
}
