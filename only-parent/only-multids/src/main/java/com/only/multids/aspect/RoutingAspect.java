package com.only.multids.aspect;

import com.only.multids.annotation.Router;
import com.only.multids.core.IRouting;
import com.only.multids.dynamicdatasource.DataSourceHolder;
import com.only.multids.enumuration.MultiDsErrorEnum;
import com.only.multids.exception.LoadRoutingStategyUnMatch;
import com.only.multids.exception.ParamsNotContainsRoutingField;
import com.only.multids.exception.RoutingFiledArgsIsNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 拦截切面组件
 */
@Component
@Aspect
@Slf4j
public class RoutingAspect {

    @Autowired
    private IRouting routing;


    @Pointcut("@annotation(com.only.multids.annotation.Router)")
    public void pointCut(){};

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws LoadRoutingStategyUnMatch, RoutingFiledArgsIsNull, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ParamsNotContainsRoutingField {
        long beginTime = System.currentTimeMillis();
        //获取方法调用名称
        Method method = getInvokeMethod(joinPoint);

        //获取方法指定的注解
        Router router = method.getAnnotation(Router.class);
        //获取指定的路由key
        String routingFiled = router.routingFiled();

        //获取方法入参
        Object[] args = joinPoint.getArgs();


        boolean havingRoutingField = false;

        if(args!=null && args.length>0) {
            for(int index=0;index<args.length;index++) {
                //参数值
                String routingFieldValue = BeanUtils.getProperty(args[index],routingFiled);
                if(!StringUtils.isEmpty(routingFieldValue)) {
                    //根据路由关键字 计算出 哪个数据源
                    String dbKey = routing.calDataSourceKey(routingFieldValue,routingFiled);
                    //根据路由关键字 计算出 哪个表
                    String tableIndex = routing.calTableKey(routingFieldValue);
                    log.info("选择的Dbkey是:{},tableKey是:{}",dbKey,tableIndex);
                    havingRoutingField = true;
                    break;
                }else {

                }
            }

            //判断入参中没有路由字段
            if(!havingRoutingField) {
                log.warn("入参{}中没有包含路由字段:{}",args,routingFiled);
                throw new ParamsNotContainsRoutingField(MultiDsErrorEnum.PARAMS_NOT_CONTAINS_ROUTINGFIELD);
            }
        }

    }

    private Method getInvokeMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        return targetMethod;
    }

    /**
     * 清除线程缓存
     * @param joinPoint
     */
    @After("pointCut()")
    public void methodAfter(JoinPoint joinPoint){
        //清理掉当前设置的数据源，让默认的数据源不受影响
        DataSourceHolder.clearDataSourceKey();
        DataSourceHolder.clearTableIndex();
    }
}
