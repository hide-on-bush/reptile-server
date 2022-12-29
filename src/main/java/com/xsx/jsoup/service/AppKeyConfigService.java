package com.xsx.jsoup.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xsx.jsoup.dao.AppKeyConfigMapper;
import com.xsx.jsoup.entity.AppKeyConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author:夏世雄
 * @Date: 2022/10/27/15:33
 * @Version: 1.0
 * @Discription:
 **/
@Service
public class AppKeyConfigService {

    @Resource
    private AppKeyConfigMapper appKeyConfigMapper;

    public AppKeyConfig getByKeyWord() {
        return appKeyConfigMapper.getByKeyWord();
    }

    public Boolean update(AppKeyConfig appKeyConfig) {
        return appKeyConfigMapper.update(appKeyConfig) == 1;
    }

    public Boolean update(String chanel, String message) {
        //1.将message转成JSONObject，取出其中的msg
        JSONObject messageJson = JSONObject.parseObject(message);
        String msg = messageJson.containsKey("msg") ? messageJson.getString("msg") : "";
        AppKeyConfig appKeyConfig = appKeyConfigMapper.getByKeyWord();
        if (ObjectUtils.isEmpty(appKeyConfig)) {
            return false;
        }
        String value = appKeyConfig.getValue();
        JSONObject jsonObject = JSON.parseObject(value);
        if (jsonObject.containsKey(chanel)) {
            JSONArray jsonArray = jsonObject.getJSONArray(chanel);
            jsonArray.add(msg);
        } else {
            JSONArray insertJson = new JSONArray();
            insertJson.add(msg);
            jsonObject.put(chanel, insertJson);
        }
        appKeyConfig.setValue(jsonObject.toJSONString());
        return this.update(appKeyConfig);
    }
}
