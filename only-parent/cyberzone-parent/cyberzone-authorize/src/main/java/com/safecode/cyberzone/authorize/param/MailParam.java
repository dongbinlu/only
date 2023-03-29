package com.safecode.cyberzone.authorize.param;

import org.hibernate.validator.constraints.Email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MailParam {

    @Email(message = "不是一个合法的电子邮件地址")
    private String mail;
}
