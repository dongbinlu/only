package com.safecode.security.permission.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.safecode.security.permission.common.ApplicationContextHelper;
import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.entity.SysAclModule;
import com.safecode.security.permission.mapper.SysAclModuleMapper;
import com.safecode.security.permission.param.TestVo;
import com.safecode.security.permission.utils.BeanValidator;
import com.safecode.security.permission.utils.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestController {
    @GetMapping("/validate")
    public JsonData validate(TestVo vo) {
        try {
            Map<String, String> map = BeanValidator.validateObject(vo);
            if (map != null && map.entrySet().size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    log.info("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
        }
        return JsonData.success("hello validate !!!");
    }

    @GetMapping("/validate2")
    public JsonData validate2(TestVo vo) {
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule sysAclModule = moduleMapper.selectByPrimaryKey(13);
        String str = JsonMapper.obj2String(sysAclModule);
        log.info("JSON字符串：" + str);
        BeanValidator.check(vo);
        return JsonData.success("hello validate !!!");
    }

    public static void main(String[] args) {
        boolean flag = true;

        if (!flag) {
            System.out.println("333333333");
        } else {
            System.out.println("44444444444");
        }
        System.out.println("______________________");
        String[] strArray = {"1"};
        String key = "SYSTEM_ACLS";
        key += "_";
        String join = Joiner.on("_").join(strArray);
        System.err.println(join);
        System.out.println(key);
    }
}
