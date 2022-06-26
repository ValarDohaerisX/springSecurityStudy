package com.lzx.springsecuritystudy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.springsecuritystudy.entity.Role;
import com.lzx.springsecuritystudy.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lzx
 * @version 1.0
 * @company 赞同科技
 * @date 2022/06/26 下午 10:15
 * @description:
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select c.* from user a left join user_role b on a.id = b.uid left join role c on b.rid = c.id where a.id = #{id}")
    public List<Role> getRolesById(@RequestParam("id") int id);

    public User loadUserByUsername(String username);
}
