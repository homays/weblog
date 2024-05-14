package com.arrebol.common.domain.mapper;

import com.arrebol.common.domain.dos.CommentDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface CommentMapper extends BaseMapper<CommentDO> {

    /**
     * 根据路由地址、状态查询对应的评论
     */
    default List<CommentDO> selectByRouterUrlAndStatus(String routerUrl, Integer status) {
        return selectList(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getRouterUrl, routerUrl) // 按路由地址查询
                .eq(CommentDO::getStatus, status) // 按状态查询
                .orderByDesc(CommentDO::getCreateTime) // 按创建时间倒序
        );
    }

    /**
     * 分页查询
     */
    default Page<CommentDO> selectPageList(Long current, Long size, String routerUrl,
                                           LocalDate startDate, LocalDate endDate, Integer status) {
        // 分页对象(查询第几页、每页多少数据)
        Page<CommentDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<CommentDO> wrapper = Wrappers.<CommentDO>lambdaQuery()
                .like(StringUtils.isNotBlank(routerUrl), CommentDO::getRouterUrl, routerUrl) // like 模糊查询
                .eq(Objects.nonNull(status), CommentDO::getStatus, status) // 评论状态
                .ge(Objects.nonNull(startDate), CommentDO::getCreateTime, startDate) // 大于等于 startDate
                .le(Objects.nonNull(endDate), CommentDO::getCreateTime, endDate)  // 小于等于 endDate
                .orderByDesc(CommentDO::getCreateTime); // 按创建时间倒叙

        return selectPage(page, wrapper);
    }

}