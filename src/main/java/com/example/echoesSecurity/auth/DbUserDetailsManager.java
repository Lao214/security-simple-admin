package com.example.echoesSecurity.auth;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.yundi.atp.entity.AuthUser;
//import com.yundi.atp.mapper.AuthUserMapper;
import com.example.echoesSecurity.entity.BUser;
import com.example.echoesSecurity.service.BUserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


/**
 * 创建DbUserDetailsManager类实现UserDetailsManager和UserDetailsPasswordService接口方法，
 * 用于实现数据库版本的认证权限管理，该类主要是实现用户数据和权限数据的获取，用于认证和授权使用
 */

@Service
public class DbUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
    @Resource
    private BUserService bUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        BUser authUser = new BUser();
        authUser.setUsername(userDetails.getUsername());
        authUser.setPassword(userDetails.getPassword());
        authUser.setEnabled(true);
        bUserService.save(authUser);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户数据
        QueryWrapper<BUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        BUser authUser = bUserService.getOne(queryWrapper);
        if (authUser == null) {
            throw new UsernameNotFoundException(username);
        } else {
            //从数据库获取用户权限列表
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(() -> "USER_LIST");
            authorities.add(() -> "USER_ADD");
            return new User(
                    //用户账号
                    authUser.getUsername(),
                    //用户密码
                    authUser.getPassword(),
                    //是否启用
                    authUser.getEnabled(),
                    //用户账号是否过期
                    true,
                    //用户凭证是否过期
                    true,
                    //用户是否未被锁定
                    true,
                    //权限列表
                    authorities);
        }
    }
}