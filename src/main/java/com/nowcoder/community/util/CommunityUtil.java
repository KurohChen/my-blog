package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    // 生成随机字符串
    public static String generateUUID(){
        // 去掉-
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    // MD5加密
    // hello -> abc124sfhhg
    // hello + 3e4a8 -> abc124sfhhg6hgbfd
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            // 全是空格也是空的
            return null;
        }else {
            // 返回md5
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }
    }
}
