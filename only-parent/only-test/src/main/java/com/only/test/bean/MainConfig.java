package com.only.test.bean;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"com.only.test.bean"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Service.class)},
        useDefaultFilters = true)
@ImportResource(locations = ("classpath:spring.xml"))
@Import(value = {Car.class, OnlyImportSelector.class, OnlyBeanDefinitionRegister.class})
public class MainConfig {

    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public Person person() {
        return new Person();
    }


}
