package com.lzx.springsecuritystudy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.springsecuritystudy.dao.UserMapper;
import com.lzx.springsecuritystudy.entity.Role;
import com.lzx.springsecuritystudy.entity.User;
import com.lzx.springsecuritystudy.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lzx
 * @version 1.0
 * @company 赞同科技
 * @date 2022/06/26 下午 10:24
 * @description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User loadUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public List<Role> getRolesById(int id) {
        return userMapper.getRolesById(id);
    }
}
