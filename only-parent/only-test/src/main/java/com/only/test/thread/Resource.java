package com.only.test.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    private String name;

    private String sex;

    private volatile boolean flag;// true : 有数据， false : 没数据

    public synchronized void input(String name, String sex) throws Exception {

        if (!flag) {
            this.name = name;
            this.sex = sex;
            flag = true;
            this.notify();
        } else {
            this.wait();
        }

    }

    public synchronized void output() throws Exception {
        if (flag) {
            System.out.println("姓名:" + this.name + ",性别:" + sex);
            this.flag = false;
            this.notify();
        } else {
            this.wait();
        }
    }


}
