package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    // 返回选择的帖子，加userId是后续可能会使用到用户主页功能
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
    // 一共有可能有多少页
    // @param用于给参数取别名，如果只有一个参数并且再<if>里使用，则必须取别名。
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id,int commentCount);

    int updateType(int id,int type);

    int updateStatus(int id,int status);

}
