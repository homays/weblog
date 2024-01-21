package com.arrebol.common.domain.mapper;

import com.arrebol.common.domain.dos.UserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户持久层
 *
 * @author Arrebol
 * @date 2024/1/21
 */
public interface UserMapper extends BaseMapper<UserDO> {

    default UserDO findByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername, username);
        return selectOne(wrapper);
    }

}
