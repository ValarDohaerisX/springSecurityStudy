package com.lzx.springsecuritystudy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.springsecuritystudy.entity.Role;
import com.lzx.springsecuritystudy.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    public User loadUserByUsername(String username);

    public List<Role> getRolesById(int id);
}

