package com.safecode.cyberzone.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Setter;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser2 {

    // 用户ID
    private Integer userId;

    private String username;

}
