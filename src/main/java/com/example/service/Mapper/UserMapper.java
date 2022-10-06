package com.example.service.Mapper;

import com.example.service.Bean.In.Transaction;
import com.example.service.Bean.Out.Bill;
import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import com.example.service.Bean.Out.Company;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT count(*) FROM userview WHERE Email = #{email} ")
    int findUser(String email);

    @Select("SELECT count(*) FROM UserInfo WHERE Email = #{email} ")
    int findUserInfo(String email);

    @Select("SELECT count(*) FROM alluser WHERE Email = #{email} ")
    int checkallUser(String email);

    @Select("SELECT count(*) FROM userview WHERE Email = #{email} and password = md5(#{password}) ")
    int checkUser(User user);

    @Select("SELECT count(*) FROM UserInfo WHERE Email = #{email} and Pin = md5(#{Pin}) ")
    int checkPayPswd(String email , String Pin);

    @Select("SELECT count(*) FROM UserInfo WHERE UserID = #{UserID} ")
    int findUserID(String UserID);

    @Select("SELECT count(*) FROM UserInfo WHERE UserName = #{username}")
    int checkUsername(String username);

    @Select("SELECT Mobile FROM UserInfo WHERE Email = #{email} ")
    String checkUserInfo(String email);

    @Select("SELECT payUser,receiveUser,Amount,Date FROM Transaction WHERE payUser=#{payUser} OR receiveUser=#{payUser}")
    List<Bill> selectBill(String payUser);

    @Select("SELECT UserID FROM UserInfo WHERE Email = #{Email}")
    String getUserId(String Email);

    @Select("SELECT UserID FROM UserInfo WHERE CustomerId = #{CustomerId}")
    String getUserIdby_cid(String CustomerId);

    @Select("SELECT UserID,Email,UserName,Mobile FROM UserInfo WHERE Email = #{Email}")
    UserInfo getUserInfo(String Email);

    @Select("SELECT picturename FROM UserPicture WHERE username = #{UserName}")
    String getPictureName(String UserName);

    @Select("SELECT CustomerId FROM UserInfo WHERE Email = #{Email}")
    String getCustomerId(String Email);

    @Select("SELECT Balance FROM UserInfo WHERE UserID = #{UserID}")
    String selectBalance(String UserID);

    @Select("SELECT UserName FROM UserInfo WHERE UserID = #{UserID}")
    String selectUserName(String UserID);

    @Select("SELECT UserName FROM UserInfo WHERE Email = #{Email}")
    String getUserName(String Email);

    @Select("SELECT Balance FROM UserInfo WHERE CustomerId = #{CustomerId}")
    String getBalance(String CustomerId);

    @Select("SELECT C_name,C_Mobile,C_address,Type FROM CompanyInfo")
    List<Company> selectCompany();

    @Select("SELECT count(*) FROM UserInfo WHERE UserName = #{username}")
    int checkeUserName();


    //插入
    @Insert("INSERT INTO Transaction (payUser,receiveUser,Amount,Date) VALUES (#{payUser},#{receiveUser},#{Amount},#{Date})")
    int transferTo(Transaction transaction);

    @Insert("INSERT INTO Transaction_Company (payUser,receiveUser,Amount,Date) VALUES (#{payUser},#{receiveUser},#{Amount},#{Date})")
    int transferTo_Company(Transaction transaction);

    @Insert("INSERT INTO UserInfo (UserID,password,Email,UserName,Mobile,Pin,Balance,CustomerId,PictureName) VALUES (#{UserID},md5(#{password}),#{email},#{username},#{Mobile},md5(#{pin}),#{Balance},#{CustomerId},#{PictureName})")
    int addUserInfo(UserInfo userInfo);

    @Insert("INSERT INTO UserInfo (CustomerId) VALUES (#{id})")
    int addCustomerId(String id);

    @Insert("INSERT INTO UserInfo (UserID) VALUES (#{UserID})")
    int addUserID(String UserID);


    @Insert("INSERT INTO UserInfo (Email,Password) VALUES (#{email},md5(#{password}))")
    int addUser(User user);




    //更新
    @Update("UPDATE UserInfo SET Balance=#{balance} WHERE UserID = #{UserID}")
    void updateBalance(String balance,String UserID);

    @Update("UPDATE UserInfo SET Balance=#{balance} WHERE CustomerId = #{CustomerId}")
    void updateBalances(String balance,String CustomerId);

    @Update("UPDATE UserInfo SET Password = md5(#{newPswd}) WHERE Email = #{email}")
    void changePswd(String newPswd, String email);

    @Update("UPDATE UserInfo SET Pin = md5(#{Pin}) WHERE Email = #{email}")
    void changePayPswd(String Pin, String email);

    @Update("UPDATE UserInfo SET UserName = #{username} WHERE Email = #{email}")
    void changeUsername(String username, String email);

    @Update("UPDATE UserInfo SET PictureName = #{PictureName} WHERE Email = #{email}")
    void updatePictureName(String PictureName, String email);

    @Update("UPDATE UserInfo SET Mobile= #{Mobile},pin= md5(#{pin}),Balance= #{Balance},CustomerId= #{CustomerId},PictureName= #{PictureName} WHERE Email = #{email}")
    void updateUserInfo(String Mobile,String pin,String Balance,String CustomerId,String PictureName, String email);





}
