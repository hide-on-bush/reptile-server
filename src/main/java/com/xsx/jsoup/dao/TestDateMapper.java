package com.xsx.jsoup.dao;

import com.xsx.jsoup.entity.TestDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/18/13:59
 * @Version: 1.0
 * @Discription:
 **/
@Mapper
public interface TestDateMapper {

    @Select("select id, create_time as createTime from t_test_date")
    List<TestDate> getAll();
}
