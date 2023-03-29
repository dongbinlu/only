package com.safecode.cyberzone.authorize.param;

import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class TelParam {
    @Pattern(regexp = "^\\d{8,12}$", message = "电话不合法,长度为8-12位")
    private String telephone;

}
