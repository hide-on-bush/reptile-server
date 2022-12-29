package com.xsx.jsoup.service;

import com.xsx.jsoup.dao.TestDateMapper;
import com.xsx.jsoup.entity.TestDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/18/13:58
 * @Version: 1.0
 * @Discription:
 **/
@Service
public class TestDateService {

    @Autowired
    private TestDateMapper testDateMapper;

    public List<TestDate> getAll() {
        return testDateMapper.getAll();
    }
}
