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


}
