package com.safecode.security.price.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class Test {

    //测试grafana的告警，不停的调用test异常
    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTEwIiwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTk0MzY5MjM4LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjBhMzI0MTJkLTE1YzgtNGM5ZC04MTllLTU3YzE2NDNkOGUzMiIsImNsaWVudF9pZCI6ImF1dGgifQ.YX6nMxlxVfo0SJNdoBqIySkYjIhI6JK06yRAdRoZB19-jt753sKyx6GuSzf64vmlgYoSZXOD2balgKx94GlJmPzUcfhjCf_dzkp_7S-ET2JnKeTciIwn46dDsPOX20uORdc7hDivkno6GBy4wlvDzucQTqNoT5HPX5GyxEdDHvqpoPDAXd0XLIvIPkG5trJOl3HvolQ1IC5fFpPuCc6TBhzdkjAAKyRbO12SdxKglK2VOFXn4OfjwDonRmP_IcW-8I9obn5Ps8YZz0G_JWCS5rj0JTfZW7NIWhNGqHUa7VIsEIBCXE4R7l7Vp9DBLlhaxp6svEumCP5qV0tejch9fA");

        HttpEntity<Object> entity = new HttpEntity<Object>(null, headers);

        while (true) {
            try {
                restTemplate.exchange("http://127.0.0.1:9082/prices/1", HttpMethod.GET, entity, String.class);
            } catch (Exception e) {
            }
            Thread.sleep(100);
        }

    }

}
