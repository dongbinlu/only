package com.macro.mall.config;

import com.macro.mall.config.properties.TradePayProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ,;,,;
 * ,;;'(    社
 * __      ,;;' ' \   会
 * /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 * ,;' \    /-.,,(   ) \    码
 * ) /       ) / )|    农
 * ||        ||  \)
 * (_\       (_\
 *
 * @author ：杨过
 * @date ：Created in 2020/1/12 22:12
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description: 资源访问路径配置
 **/
@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Autowired
    private TradePayProp tradePayProp;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) { //windows系统
            /** 支付码图片QrCode图片存储路径 */
            registry.addResourceHandler(tradePayProp.getHttpBasePath()
                    + "/**")
                    .addResourceLocations("file:" + tradePayProp.getStorePath() + "/");
        } else { //linux或者mac
            registry.addResourceHandler(tradePayProp.getHttpBasePath()
                            + "/**")
                    .addResourceLocations("file:" + tradePayProp.getStorePath() + "/"); //静态资源
            //.addResourceLocations("classpath:/META-INF/resources/"); //swagger2页面
        }
    }

}
