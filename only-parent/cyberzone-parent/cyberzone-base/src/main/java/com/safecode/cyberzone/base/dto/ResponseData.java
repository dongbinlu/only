package com.safecode.cyberzone.base.dto;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Spring controller中统一返回的json实体类
 *
 * @param <T>
 * @author Lunan
 */
public class ResponseData<T> {

    public interface SimpleView {
    }

    ;

    public interface DetailView extends SimpleView {
    }

    ;

    private Integer code;
    private String msg;
    private T data;

    public ResponseData() {

    }

    public ResponseData(Integer code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @JsonView(DetailView.class)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonView(DetailView.class)
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JsonView(DetailView.class)
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseData [code=" + code + ", msg=" + msg + ", data=" + data.toString() + "]";
    }

}
