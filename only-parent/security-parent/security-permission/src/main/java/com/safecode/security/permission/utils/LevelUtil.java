package com.safecode.security.permission.utils;

import org.apache.commons.lang3.StringUtils;

public class LevelUtil {

    // level的分隔符
    public final static String SEPARATOR = ".";

    // 根节点
    public final static String ROOT = "0";

    public static String calculateLevel(String parentLevel, Integer parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
