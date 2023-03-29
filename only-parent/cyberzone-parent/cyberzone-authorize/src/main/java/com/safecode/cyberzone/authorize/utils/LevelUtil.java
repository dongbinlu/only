package com.safecode.cyberzone.authorize.utils;

import org.apache.commons.lang3.StringUtils;

public class LevelUtil {
    // 各层级的分隔符
    public final static String SEPARATOR = ".";
    public final static String ROOT = "0";

    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4
    // StringUtils.join()和String.join()用途：将数组或集合以某拼接符拼接到一起形成新的字符串。
    // 上一层部门规则 上一层部门id
    public static String calculateLevel(String parentLevel, int parentId) {
        // 首层
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }

    }
}
