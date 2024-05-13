package com.example.echoesSecurity.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.echoesSecurity.auth.DbUserDetailsManager;
import com.example.echoesSecurity.entity.BUser;
import com.example.echoesSecurity.entity.enumVo.ErrorCode;
import com.example.echoesSecurity.service.BUserService;
import com.example.echoesSecurity.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 劳威锟
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/user")
public class BUserController {

    @Autowired
    DbUserDetailsManager dbUserDetailsManager;

    @Autowired
    BUserService bUserService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("getUser/{id}")
    public Result getUser(@PathVariable String id) {
        BUser user = bUserService.getById(id);
        return Result.success().data("data",user);
    }

    /**
     * @description:  获取用户信息
     * @param:
     * @return:
     * @author 劳威锟
     * @date: 2023/7/31 2:31 PM
     */
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        // 获取请求头token字符串
        String token = request.getHeader("token");
        if (token == null) {
            return Result.error().code(ErrorCode.TOKEN_NOT_EXIST.getCode()).msg(ErrorCode.TOKEN_NOT_EXIST.getMsg());
        }
        String username = stringRedisTemplate.opsForValue().get(token);

        // 根据token获取ID
        try {
            QueryWrapper<BUser> saUserQueryWrapper =new QueryWrapper<>();
            saUserQueryWrapper.eq("username",username);
            BUser one = bUserService.getOne(saUserQueryWrapper);
            return Result.success().data("one",one);
        }catch (Exception e) {
            return Result.error().code(ErrorCode.TOKEN_VALID.getCode()).msg(ErrorCode.TOKEN_VALID.getMsg());
        }
    }


}

