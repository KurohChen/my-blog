package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


// 让spring容器装配这个bean，也可以用@Repository
@Mapper
public interface UserMapper {

    // 根据xx查询用户的方法
    User selectById(int id);
    User selectByName(String username);

    User selectByEmail(String email);

    // 新增一个用户
    int insertUser(User user);

    // 修改用户状态，int是返回修改了几条数据
    int updateStatus(int id,int status);

    // 更新头像路径
    int updateHeader(int id,String headerUrl);

    // 修改密码
    int updatePassword(int id, String password);



}
