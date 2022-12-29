package com.xsx.jsoup.aop;

import com.xsx.jsoup.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Author:夏世雄
 * @Date: 2022/10/17/18:18
 * @Version: 1.0
 * @Discription:
 **/
@Aspect
@Component
public class UserControllerAop {

    /**
     * spring容器中共有五种通知类型
     * 1 : 前置通知 @Before 在方法执行前执行
     * 2：后置通知 @After 在方法执行后执行
     * 3：返回通知 @AfterReturning在方法执行前执行，无论是否出现异常
     * 4：异常通知 @AfterThrowing在方法执行前执行，出现异常则不执行
     * 5：环绕通知 @Around可以单独完成以上四个通知
     */

    @Pointcut("execution(* com.xsx.jsoup.controller.UseAopController.*(..))")
    public void pointcut() {
    }


    /**
     * @param joinPoint
     * @return
     * @Around注解可以用来在调用一个具体方法前和调用后来完成一些具体的任务。 比如我们想在执行controller中方法前打印出请求参数，并在方法执行结束后来打印出响应值，这个时候，我们就可以借助于@Around注解来实现；
     */
    @Around("pointcut()")
    public String aroundProcess(ProceedingJoinPoint joinPoint) {
        System.out.println("我是环绕通知");
        Object obj = null;
        try {
            Object[] args = joinPoint.getArgs();
            System.out.println("请求参数的值为args=" + args[0]);
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            //方法名
            String methodName = methodSignature.getName();
            System.out.println("methodName =" + methodName);
            //参数名数组
            String[] parameters = methodSignature.getParameterNames();
            System.out.println("参数名为 paramName =" + parameters[0]);
            obj = joinPoint.proceed();

        } catch (Throwable throwable) {
            return "ERROR";
        }
        System.out.println("环绕通知，在调用方法之后执行");
        return "SUCCESS:" + obj;
    }

    @AfterReturning(pointcut = "pointcut()")
    public Result returnProcess(JoinPoint joinPoint) {
        System.out.println("返回通知================== ");
        Object obj = null;
        try {
            obj = joinPoint.getSignature().getName();
        } catch (Throwable throwable) {
            return new Result("ERROR", 500, null);
        }
        return new Result("SUCCESS", 200, obj);
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("我是前置通知====================");
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("我是后置通知=================");
    }
}
