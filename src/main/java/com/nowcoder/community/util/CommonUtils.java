package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * 公共工具类
 */
public class CommonUtils {

    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    //MD5加密
    public static String md5(String key) {
        if (StringUtils.isNotBlank(key)) {
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }
        return null;
    }


}