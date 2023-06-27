package com.only.mvc.plus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDoc implements Serializable {

    private String title;
    private String author;
    private String content;
    private Date createTime;
}
