package com.only.test.ioc;

import org.springframework.context.annotation.*;

@Configuration
@Import(value = {TestService.class})
@ImportResource(locations = ("classpath:spring.xml"))
@ComponentScan(basePackages = {"com.only.test"})
public class MainConfig {

    @Bean
    public Dog dog() {
        return new Dog();
    }

    /*@Bean
    public InstanceA instanceA(InstanceB instanceB) {
        return new InstanceA(instanceB);
    }*/

   /* @Bean
    public InstanceB instanceB(InstanceA instanceA) {
        return new InstanceB(instanceA);
    }*/

}
