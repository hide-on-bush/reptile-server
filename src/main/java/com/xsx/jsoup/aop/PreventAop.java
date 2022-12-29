package com.xsx.jsoup.aop;

/**
 * @Author:夏世雄
 * @Date: 2022/10/09/17:40
 * @Version: 1.0
 * @Discription:
 **/

import com.alibaba.fastjson.JSON;
import com.xsx.jsoup.common.annotation.Prevent;
import com.xsx.jsoup.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Base64;

/**
 * 防刷切面实现类
 */
@Aspect
@Component
public class PreventAop {
    private static Logger log = LoggerFactory.getLogger(PreventAop.class);

    @Autowired
    private RedisService redisService;


    /**
     * 切入点
     */
    @Pointcut("@annotation(com.xsx.jsoup.common.annotation.Prevent)")
    public void pointcut() {
    }


    /**
     * 处理前
     *
     * @return
     */
    @Before("pointcut()")
    public void joinPoint(JoinPoint joinPoint) throws Exception {
        String requestStr = JSON.toJSONString(joinPoint.getArgs()[0]);
        if (StringUtils.isEmpty(requestStr) || requestStr.equalsIgnoreCase("{}")) {
            throw new RuntimeException("[防刷]入参不允许为空");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(),
                methodSignature.getParameterTypes());

        Prevent preventAnnotation = method.getAnnotation(Prevent.class);
        String methodFullName = method.getDeclaringClass().getName() + method.getName();

        entrance(preventAnnotation, requestStr, methodFullName);
        return;
    }


    /**
     * 入口
     *
     * @param prevent
     * @param requestStr
     */
    private void entrance(Prevent prevent, String requestStr, String methodFullName) throws Exception {
        PreventStrategy strategy = prevent.strategy();
        switch (strategy) {
            case DEFAULT:
                defaultHandle(requestStr, prevent, methodFullName);
                break;
            default:
                throw new RuntimeException("无效的策略");
        }
    }


    /**
     * 默认处理方式
     *
     * @param requestStr
     * @param prevent
     */
    private void defaultHandle(String requestStr, Prevent prevent, String methodFullName) throws Exception {
        String base64Str = toBase64String(requestStr);
        long expire = Long.parseLong(prevent.value());

        String resp = redisService.get(methodFullName + base64Str);
        if (StringUtils.isEmpty(resp)) {
            redisService.set(methodFullName + base64Str, requestStr, expire);
        } else {
            String message = !StringUtils.isEmpty(prevent.message()) ? prevent.message() :
                    expire + "秒内不允许重复请求";
            throw new RuntimeException(message);
        }
    }


    /**
     * 对象转换为base64字符串
     *
     * @param obj 对象值
     * @return base64字符串
     */
    private String toBase64String(String obj) throws Exception {
        if (StringUtils.isEmpty(obj)) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = obj.getBytes("UTF-8");
        return encoder.encodeToString(bytes);
    }


}