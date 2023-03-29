package com.safecode.security.order.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.safecode.security.order.entity.OrderInfo;
import com.safecode.security.order.entity.PriceInfo;
import com.safecode.security.order.entity.User;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private RestTemplate restTemplate = new RestTemplate();

    /*
     * @AuthenticationPrincipal String username
     * 在没有配置UserDetailsService时，只能获取用户名
     * 配置UserDetailsService后，可根据DefaultAccessTokenConverter将用户名转换为用户相关信息
     * 只获取ID：@AuthenticationPrincipal(expression = "#this.id") Long id
     */
    @PostMapping
    public OrderInfo create(OrderInfo info, @AuthenticationPrincipal User user) {
        log.info(
                "id is {} , username:{} , password:{} , authorities:{} , accountNonExpired:{} , accountNonLocked:{} , credentialsNonExpired:{} , enabled:{}",
                user.getId(), user.getUsername(), user.getPassword(), user.getAuthorities(), user.isAccountNonExpired(),
                user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled());
/*		
		log.info("product id is {}", info.getProductId());
		
		PriceInfo price = restTemplate.getForObject("http://127.0.0.1:8020/prices/" + info.getProductId(),
				PriceInfo.class);
		
		log.info("price is {}", price.getPrice());
*/
        return info;
    }

    @GetMapping("/{id}")
    public OrderInfo get(@PathVariable("id") Long id) {
        log.info("order id is {}", id);
        return new OrderInfo();
    }

}
