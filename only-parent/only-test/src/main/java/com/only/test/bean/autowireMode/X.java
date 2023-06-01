package com.only.test.bean.autowireMode;

import org.springframework.stereotype.Component;

@Component
public class X {

//    @Autowired
    private Y y;

    public void setY(Y y){
        this.y = y;
    }


    @Override
    public String toString() {
        return "X{" +
                "y=" + y +
                '}';
    }
}
