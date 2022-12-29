package com.xsx.jsoup.dao;

import com.xsx.jsoup.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/21/15:04
 * @Version: 1.0
 * @Discription:
 **/
@Mapper
public interface UserMapper {

    @Select("select id,name, mobile, age, create_time as createTime from t_user")
    List<User> getAll();
}
