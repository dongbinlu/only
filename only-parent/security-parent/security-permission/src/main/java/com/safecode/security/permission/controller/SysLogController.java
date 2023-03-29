package com.safecode.security.permission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.service.SysLogService;

@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("/page")
    public JsonData page(
            @PageableDefault(page = 1, size = 10, sort = "operate_time", direction = Direction.ASC) Pageable pageable) {
        return JsonData.success(sysLogService.getPage(pageable));
    }

    /**
     * <p>@Title: recover<p>
     * <p>@Description: </p>
     *
     * @param id
     * @return
     * @date 2019年10月11日 下午1:10:12
     */
    @PostMapping("/recover")
    public JsonData recover(@RequestParam("id") Integer id) {
        sysLogService.recover(id);
        return JsonData.success();

    }

}
