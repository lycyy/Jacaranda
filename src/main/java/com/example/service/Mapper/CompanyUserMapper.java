package com.example.service.Mapper;

import com.example.service.Bean.In.CompanyInfo;
import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CompanyUserMapper {
    //查询
    @Select("SELECT * FROM CompanyUser WHERE Email = #{email} and password = md5(#{password}) ")
    User checkUser(User user);

    @Select("SELECT CompanyID,Email FROM Company WHERE Email = #{email}")
    CompanyInfo getUserInfo(String Email);

    @Select("SELECT C_name FROM Company WHERE Email = #{email}")
    String getUsername(String Email);

    @Select("SELECT CompanyID FROM Company WHERE Email = #{email}")
    String getUserId(String email);

    @Select("SELECT count(*) FROM Company WHERE CompanyID = #{CompanyID} ")
    int findUserID(String CompanyID);

    @Select("SELECT count(*) FROM Company WHERE Email = #{email} ")
    int findUser(String Email);

    @Select("SELECT Balance FROM Company WHERE CompanyID = #{CompanyID} ")
    String selectBalance(String CompanyID);

    @Select("SELECT CompanyID FROM Company WHERE Email = #{email} ")
    String selectUserID(String email);

    @Select("SELECT PictureName FROM Company WHERE C_name = #{username} ")
    String getPicture(String username);
    //增添
    @Insert("INSERT INTO Company (CompanyID,PictureName,C_name,C_Mobile,C_address,Balance,Email) VALUES (#{CompanyID},#{Picture},#{C_name},#{C_Mobile},#{C_address},#{Balance},#{email})")
    int addUserInfo(CompanyInfo companyInfo);

    @Insert("INSERT INTO CompanyUser (Email,Password) VALUES (#{email},md5(#{password}))")
    int addUser(User user);
    //修改
    @Update("UPDATE Company SET Balance=#{balance} WHERE CompanyID = #{CompanyID}")
    void updateBalances(String balance,String CompanyID);

    @Update("UPDATE Company SET PictureName = #{PictureName} WHERE Email = #{email}")
    void updateCompanyPicture(String PictureName, String email);

    @Update("UPDATE Company SET Head_portrait = #{PictureName} WHERE Email = #{email}")
    void updateCompanyHead(String PictureName, String email);
    //删除
}
