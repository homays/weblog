package com.arrebol.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.arrebol.common.domain.dos.TagDO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface TagMapper extends BaseMapper<TagDO> {

    default Page<TagDO> selectPageList(long current, long size, String name, LocalDate startDate, LocalDate endDate) {
        // 分页对象
        Page<TagDO> page = new Page<>(current, size);
        // 构建查询条件
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Objects.nonNull(name), TagDO::getName, name) // 模糊查询
                .ge(Objects.nonNull(startDate), TagDO::getCreateTime, startDate) // 大于等于开始时间
                .le(Objects.nonNull(endDate), TagDO::getCreateTime, endDate) // 小于等于结束时间
                .orderByDesc(TagDO::getCreateTime); // order by create_time desc
        return selectPage(page, wrapper);
    }

    /**
     * 根据标签模糊查询
     */
    default List<TagDO> selectByKey(String key) {
        LambdaQueryWrapper<TagDO> wrapper = new LambdaQueryWrapper<>();
        // 构造模糊查询的条件
        wrapper.like(TagDO::getName, key).orderByDesc(TagDO::getCreateTime);
        return selectList(wrapper);
    }

    /**
     * 根据标签 ID 批量查询
     */
    default List<TagDO> selectByIds(List<Long> tagIds) {
        return selectList(Wrappers.<TagDO>lambdaQuery()
                .in(TagDO::getId, tagIds));
    }

    /**
     * 根据标签名称查询标签
     */
    default TagDO selectByName(String name) {
        return selectOne(Wrappers.<TagDO>lambdaQuery()
                .eq(TagDO::getName, name));
    }
}
