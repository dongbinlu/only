package com.safecode.security.subject.test;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.safecode.security.subject.vo.SysUserVo;

@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * RESTful 查询，GET方式：username模糊查询，测试用例中，期望返回长度为3
     *
     * @param username
     * @return
     * @RequestParam name或value = 请求参数名称 , required = 是否必传，默认为true ,
     * defaultValue = 默认值
     */
    @GetMapping
    public List<SysUserVo> query(
            @RequestParam(name = "username", required = false, defaultValue = "tom") String username) {
        List<SysUserVo> list = new ArrayList<>();
        list.add(new SysUserVo());
        list.add(new SysUserVo());
        list.add(new SysUserVo());
        return list;
    }

    /**
     * RESTful 多条件查询：测试用例中，期望返回长度为3
     *
     * @param sysUserVo
     * @param pageable
     * @return
     */

    @PostMapping
    public List<SysUserVo> queryConditions(SysUserVo sysUserVo,
                                           @PageableDefault(page = 1, size = 10, sort = "id,asc") Pageable pageable) {
        logger.info(ReflectionToStringBuilder.toString(sysUserVo, ToStringStyle.MULTI_LINE_STYLE));
        logger.info(ReflectionToStringBuilder.toString(pageable, ToStringStyle.MULTI_LINE_STYLE));
        logger.info(String.valueOf(pageable.getPageNumber()));
        logger.info(String.valueOf(pageable.getPageSize()));
        logger.info(String.valueOf(pageable.getSort()));
        List<SysUserVo> list = new ArrayList<>();
        list.add(new SysUserVo());
        list.add(new SysUserVo());
        list.add(new SysUserVo());
        return list;
    }

    /**
     * @param id 只接受数字 正则表达式：只接受固定的格式，数字或字符串 或者@GetMapping({"id:\\d+","/"})
     * @return
     * @RequestMapping 是可以处理多个url的 这样就是可以的，此时的required = false就可以起作用了。
     * 我觉得springmvc处理这块的逻辑就是：如果required = false，
     * 而你没有传这个参数，那么它会去找这个参数去掉之后的替代url (/user/id)，
     * 如果发现有替代的url，就可以处理这个请求，如果没有找到，就抛出异常不去处理。
     */
    @GetMapping({"/{id:^[0-9]*$}", "/"})
    public SysUserVo infoInteger(@PathVariable(name = "id", required = false) Integer id) {
        logger.info(String.valueOf(id));
        SysUserVo vo = new SysUserVo();
        vo.setAccount("tome");
        vo.setDeptId(1);
        return vo;
    }

    /**
     * @param id 由26个英文字母组成的字符串 只接受数字 正则表达式：只接受固定的格式，数字或字符串
     * @return
     */
    @GetMapping("/{id:^[A-Za-z]+$}")
    public SysUserVo infoString(@PathVariable(name = "id") String id) {
        logger.info(String.valueOf(id));
        SysUserVo vo = new SysUserVo();
        vo.setAccount("String");
        vo.setDeptId(1);
        return vo;
    }

    /**
     * @return
     * @JsonView控制json输出类容 用法： 1，使用接口来声明多个视图 2，在值对象的get方法上指定试图
     * 3，在controller方法上指定视图
     */

    @GetMapping("/jsonView")
    @JsonView(SysUserVo.SimpleView.class)
    public SysUserVo id() {
        SysUserVo vo = new SysUserVo();
        vo.setAccount("admin");
        vo.setDeptId(1);
        vo.setMail("admin@qq.com");
        vo.setPassword("123456");
        return vo;
    }

    /**
     * @param vo
     * @return
     * @Valid注解和BindingResult验证请求参数的合法性并处理校验结果 传进来的数据不符合相关注解，
     * 那么相应得错误信息会放在BindingResult对象中
     */

    @PostMapping("/create")
    public SysUserVo create(@Valid @RequestBody SysUserVo vo, BindingResult errors) {
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error -> logger.info(error.getDefaultMessage()));
        }
        logger.info(ReflectionToStringBuilder.toString(vo, ToStringStyle.MULTI_LINE_STYLE));
        vo.setId(1);
        return vo;
    }

    @PutMapping("/update/{id:\\d+}")
    public SysUserVo update(@Valid @RequestBody SysUserVo vo, BindingResult errors, @PathVariable("id") Integer id) {
        logger.info(String.valueOf(id));
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error -> {
                FieldError fieldError = (FieldError) error;
                String message = fieldError.getField() + " " + error.getDefaultMessage();
                logger.info(message);
            });
        }
        logger.info(ReflectionToStringBuilder.toString(vo, ToStringStyle.MULTI_LINE_STYLE));
        return vo;
    }
}
