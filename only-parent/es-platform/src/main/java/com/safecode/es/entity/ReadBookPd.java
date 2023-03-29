package com.safecode.es.entity;

import java.util.Date;

import lombok.Data;

@Data
public class ReadBookPd {
    private Integer id;

    private String name;

    private String enName;

    private String author;

    private String imgurl;

    private Date createTime;

    private Integer creator;

    private Date updateTime;
    private Integer commentNum;
    private Integer price;
    private String category;

    /**
     * 1正常，-1删除，0下架
     */
    private Integer status;

    private String discription;

}