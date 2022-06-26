package com.lzx.springsecuritystudy.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzx.springsecuritystudy.dao.UserMapper;
import com.lzx.springsecuritystudy.entity.Role;
import com.lzx.springsecuritystudy.entity.User;
import com.lzx.springsecuritystudy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author lzx
 * @version 1.0
 * @company 赞同科技
 * @date 2022/06/23 下午 11:44
 * @description:
 */
@Slf4j
@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userService.loadUserByUsername(username);
        if (Objects.isNull(user)){
            throw new UsernameNotFoundException("用户名不存在！");
        }
        final List<Role> roles = userService.getRolesById(user.getId());
        user.setRoles(roles);
        return user;
    }
}
