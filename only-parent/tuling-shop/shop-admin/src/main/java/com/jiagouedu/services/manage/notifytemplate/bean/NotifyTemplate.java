package com.jiagouedu.services.manage.notifytemplate.bean;import java.io.Serializable;public class NotifyTemplate extends com.jiagouedu.services.common.NotifyTemplate implements Serializable {    private static final long serialVersionUID = 1L;    private String templateCheckError;// 模板检查是否存在错误。    public void clear() {        super.clear();        templateCheckError = null;    }    public String getTemplateCheckError() {        return templateCheckError;    }    public void setTemplateCheckError(String templateCheckError) {        this.templateCheckError = templateCheckError;    }}