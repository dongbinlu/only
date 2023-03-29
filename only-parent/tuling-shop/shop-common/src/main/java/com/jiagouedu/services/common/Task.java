package com.jiagouedu.services.common;import com.jiagouedu.core.dao.page.PagerModel;import java.io.Serializable;public class Task extends PagerModel implements Serializable {    private static final long serialVersionUID = 1L;    private String id;    private String code;    private String name;    private String sleep;    private String unit;    private String nextWorkTime;    private String currentStatus;    private String clz;    public static final String task_code_ManageIndexReportTask = "ManageIndexReportTask";    public static final String task_currentStatus_wait = "wait";    public static final String task_currentStatus_run = "run";    public static final String task_currentStatus_stop = "stop";    public void clear() {        super.clear();        id = null;        code = null;        name = null;        sleep = null;        unit = null;        nextWorkTime = null;        currentStatus = null;        clz = null;    }    public String getId() {        return id;    }    public void setId(String id) {        this.id = id;    }    public String getCode() {        return code;    }    public void setCode(String code) {        this.code = code;    }    public String getName() {        return name;    }    public void setName(String name) {        this.name = name;    }    public String getSleep() {        return sleep;    }    public void setSleep(String sleep) {        this.sleep = sleep;    }    public String getUnit() {        return unit;    }    public void setUnit(String unit) {        this.unit = unit;    }    public String getNextWorkTime() {        return nextWorkTime;    }    public void setNextWorkTime(String nextWorkTime) {        this.nextWorkTime = nextWorkTime;    }    public String getCurrentStatus() {        return currentStatus;    }    public void setCurrentStatus(String currentStatus) {        this.currentStatus = currentStatus;    }    public String getClz() {        return clz;    }    public void setClz(String clz) {        this.clz = clz;    }}