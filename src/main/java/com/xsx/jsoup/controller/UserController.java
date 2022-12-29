package com.xsx.jsoup.controller;

import com.alibaba.fastjson.JSON;
import com.xsx.jsoup.common.util.JwtUtil;
import com.xsx.jsoup.entity.User;
import com.xsx.jsoup.entity.dto.UserTokenDTO;
import com.xsx.jsoup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/24/10:16
 * @Version: 1.0
 * @Discription:
 **/
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<User> getAll() {
        List<User> list = userService.getAll();
        list.forEach(x -> System.out.println(x));
        return list;
    }

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/token")
    public String token(@RequestBody UserTokenDTO userTokenDTO) {
        //return jwtUtil.createToken(userTokenDTO);
        return jwtUtil.generateToken(userTokenDTO);
    }


    @GetMapping("/check/token")
    public UserTokenDTO checkToken(String token) {
        //return jwtUtil.parseToken1(token);
        return jwtUtil.parseToken2(token);
    }

    /**
     * 传json必须要使用@RequestBody formData则不需要
     *
     * @param userTokenDTO
     * @return
     */
    @PostMapping("/test")
    public String test(UserTokenDTO userTokenDTO) {
        return JSON.toJSONString(userTokenDTO);
    }
}
