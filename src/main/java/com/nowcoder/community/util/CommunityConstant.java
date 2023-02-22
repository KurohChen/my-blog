package com.nowcoder.community.util;

public interface CommunityConstant {
    // 激活成功
    int ACTIVATION_SUCCESS = 0;

    // 重复激活
    int ACTIVATION_REPEAT = 1;

    // 激活失败
    int ACTIVATION_FAILURE = 2;

    //默认状态的登录超时时间:12小时
    int DEFAULT_EXPIRED_SECONDS = 3600*12;

    //记住状态下的超时时间：100天
    int REMEMBERME_EXPIRED_SECONDS = 3600*24*100;

    //实体类型：帖子
    int ENTITY_TYPE_POST = 1;

    //实体类型：帖子
    int ENTITY_TYPE_COMMENT = 2;

    //实体类型：人
    int ENTITY_TYPE_USER = 3;
}
