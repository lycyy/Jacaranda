package com.example.service.Mapper;

import com.example.service.Bean.In.Transaction;
import com.example.service.Bean.Out.Bill;
import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT count(*) FROM Users WHERE Email = #{email} ")
    int findUser(String email);

    @Select("SELECT * FROM Users WHERE Email = #{email} and password = #{password} ")
    User checkUser(User user);

    @Select("SELECT count(*) FROM UserInfo WHERE Email = #{email} and payPassword = #{payPassword} ")
    int checkPayPswd(String email , String payPassword);

    @Select("SELECT count(*) FROM UserInfo WHERE UserID = #{UserID} ")
    int findUserID(String UserID);

    @Select("SELECT receiveUser,Amount,Date FROM Transaction WHERE payUser=#{payUser}")
    List<Bill> selectBill(String payUser);

    @Select("SELECT UserID FROM UserInfo WHERE Email = #{Email}")
    String getUserId(String Email);

    @Select("SELECT UserID FROM UserInfo WHERE CustomerId = #{CustomerId}")
    String getUserIdby_cid(String CustomerId);

    @Select("SELECT UserID,Email,UserName,Mobile FROM UserInfo WHERE Email = #{Email}")
    UserInfo getUserInfo(String Email);

    @Select("SELECT PictureName FROM UserInfo WHERE Email = #{Email}")
    String getPictureName(String Email);

    @Select("SELECT CustomerId FROM UserInfo WHERE Email = #{Email}")
    String getCustomerId(String Email);

    @Select("SELECT Balance FROM UserInfo WHERE UserID = #{UserID}")
    String selectBalance(String UserID);

    @Select("SELECT UserName FROM UserInfo WHERE UserID = #{UserID}")
    String selectUserName(String UserID);

    @Select("SELECT Balance FROM UserInfo WHERE CustomerId = #{CustomerId}")
    String getBalance(String CustomerId);


    //插入
    @Insert("INSERT INTO Transaction (payUser,receiveUser,Amount,Date) VALUES (#{payUser},#{receiveUser},#{Amount},#{Date})")
    int transferTo(Transaction transaction);

    @Insert("INSERT INTO UserInfo (UserID,Email,UserName,Mobile,payPassword,Balance,CustomerId,PictureName) VALUES (#{UserID},#{email},#{username},#{Mobile},#{paypassword},#{Balance},#{CustomerId},#{PictureName})")
    int addUserInfo(UserInfo userInfo);

    @Insert("INSERT INTO UserInfo (CustomerId) VALUES (#{id})")
    int addCustomerId(String id);

    @Insert("INSERT INTO UserInfo (UserID) VALUES (#{UserID})")
    int addUserID(String UserID);


    @Insert("INSERT INTO Users (Email,Password) VALUES (#{email},#{password})")
    int addUser(User user);




    //更新
    @Update("UPDATE UserInfo SET Balance=#{balance} WHERE UserID = #{UserID}")
    void updateBalance(String balance,String UserID);

    @Update("UPDATE UserInfo SET Balance=#{balance} WHERE CustomerId = #{CustomerId}")
    void updateBalances(String balance,String CustomerId);

    @Update("UPDATE Users SET Password = #{newPswd} WHERE Email = #{email}")
    void changePswd(String newPswd, String email);

    @Update("UPDATE UserInfo SET payPassword = #{newPayPswd} WHERE Email = #{email}")
    void changePayPswd(String newPayPswd, String email);

    @Update("UPDATE UserInfo SET UserName = #{username} WHERE Email = #{email}")
    void changeUsername(String username, String email);

    @Update("UPDATE UserInfo SET PictureName = #{PictureName} WHERE Email = #{email}")
    void updatePictureName(String PictureName, String email);





}
