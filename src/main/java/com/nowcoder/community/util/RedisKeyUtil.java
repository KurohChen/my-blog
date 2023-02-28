package com.nowcoder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";

    //以实体为key
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    //以用户为key
    private static final String PREFIX_USER_LIKE = "like:user";
    //某个实体的赞的key
    //like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
    //某个用户的赞
    //like:user:userId
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //关注目标key
    private static final String PREFIX_FOLLOWEE = "followee";
    //粉丝
    private static final String PREFIX_FOLLOWER = "follower";
    //某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType,int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }


    //验证码key
    private static final String PREFIX_KAPTCHA = "kaptcha";
    //登录验证码
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }
    private static final String PREFIX_TICKET = "ticket";
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    private static final String PREFIX_USER = "user";
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }

    private static final String PREFIX_UV = "uv";
    //单日uv
    public static String getUVKey(String date){
        return PREFIX_UV + SPLIT + date;
    }
    //区间uv
    public static String getUVKey(String startDate,String endDate){
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }
    private static final String PREFIX_DAU = "dau";
    //单日活跃用户
    public static String getDAUKey(String date){
        return PREFIX_DAU + SPLIT + date;
    }
    //区间uv
    public static String getDAUKey(String startDate,String endDate){
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    private static final String PREFIX_POST = "post";
    //统计帖子分数
    public static  String getPostScoreKay(){
        return PREFIX_POST + SPLIT + "score";
    }



}
