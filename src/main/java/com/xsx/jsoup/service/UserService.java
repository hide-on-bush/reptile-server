package com.xsx.jsoup.service;

import com.xsx.jsoup.dao.UserMapper;
import com.xsx.jsoup.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/24/10:14
 * @Version: 1.0
 * @Discription:
 **/

@Service
public class UserService {

    private UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> getAll() {
        return userMapper.getAll();
    }
}
