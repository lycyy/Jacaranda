package com.example.service.Mapper;

import com.example.service.Bean.User;
import com.example.service.Bean.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

@Mapper
public interface UserMapper {


    @Insert("INSERT INTO users_table (Email,password) VALUES (#{Email},#{password})")
    int addUser(User user);

    @Select("SELECT * FROM users_table WHERE Email = #{email} ")
    User findUser(String email);

    @Insert("INSERT INTO userinfo_table (UserID,email,username,Mnumber,paypassword) VALUES (#{UserID},#{email},#{username},#{Mnumber},#{paypassword})")
    int addUserInfo(UserInfo userInfo);

    @Insert("INSERT INTO userinfo_table (UserID) VALUES (#{UserID})")
    int addUserID(String UserID);

    @Select("SELECT * FROM userinfo_table WHERE UserID = #{UserID} ")
    String findUserID(String UserID);

}
