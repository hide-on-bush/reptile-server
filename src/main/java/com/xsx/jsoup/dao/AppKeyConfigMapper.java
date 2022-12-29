package com.xsx.jsoup.dao;

import com.xsx.jsoup.entity.AppKeyConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author:夏世雄
 * @Date: 2022/10/27/15:30
 * @Version: 1.0
 * @Discription:
 **/
@Mapper
public interface AppKeyConfigMapper {

    List<AppKeyConfig> list();

    @Update("update t_app_key_config set value =#{value} where id = #{id}")
    int update(AppKeyConfig appKeyConfig);

    @Select("select id, key_word as keyWord, value,stay,status,space,create_time as createTime " +
            "from t_app_key_config where key_word='BATCH_PAY_CHANNEL_MSG'")
    AppKeyConfig getByKeyWord();
}
