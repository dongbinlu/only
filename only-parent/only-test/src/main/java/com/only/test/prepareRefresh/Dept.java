package com.only.test.prepareRefresh;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Dept {

    private Integer id;

    private String name;

    public Dept() {
        System.out.println("无参构造");
    }

}
