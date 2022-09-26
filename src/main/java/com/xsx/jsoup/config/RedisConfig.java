package com.xsx.jsoup.config;

/**
 * @Author:夏世雄
 * @Date: 2022/07/22/14:17
 * @Version: 1.0
 * @Discription:
 **/
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {


    private static ThreadLocal<RedisTemplate<String, Object>>  redisTemplateThreadLocal = new ThreadLocal<>();

    /**
     *  自定义RedisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean(name = "myRedisTemplate")
    public  RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //为了开发方便一般使用 <String, Object>
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //string序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //key采用string的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        //hash采用string的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        //value序列化的方式采用jackson
        template.setValueSerializer(redisSerializer());
        //hash value序列化的方式采用jackson
        template.setHashValueSerializer(redisSerializer());
        template.setDefaultSerializer(redisSerializer());

        template.afterPropertiesSet();

        return template;
    }

    @Bean(name = "springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> redisSerializer() {
        //序列化配置，默认采用jdk序列化
        //json序列化
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .registerModule(new JavaTimeModule());
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }
}
