package com.example.service.Mapper;

import com.example.service.Bean.In.Promotion;
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

    @Select("SELECT count(*) FROM userinfo WHERE Email = #{email} ")
    int findUserInfo(String email);

    @Select("SELECT count(*) FROM username WHERE name = #{username}")
    int checkusername (String username);
    @Select("SELECT count(*) FROM alluser WHERE Email = #{email} ")
    int checkallUser(String email);

    @Select("SELECT count(*) FROM userview WHERE Email = #{email} and password = md5(#{password}) ")
    int checkUser(User user);

    @Select("SELECT count(*) FROM userinfo WHERE Email = #{email} and Pin = md5(#{Pin}) ")
    int checkPayPswd(String email , String Pin);

    @Select("SELECT count(*) FROM userinfo WHERE UserID = #{UserID} ")
    int findUserID(String UserID);

    @Select("SELECT count(*) FROM userinfo WHERE UserName = #{username}")
    int checkUsername(String username);

    @Select("SELECT Mobile FROM userinfo WHERE Email = #{email} ")
    String checkUserInfo(String email);

    @Select("SELECT payUser,receiveUser,Amount,Date FROM transactionview WHERE payUser=#{payUser} OR receiveUser=#{payUser}")
    List<Bill> selectBill(String payUser);

    @Select("SELECT UserID FROM userinfo WHERE Email = #{Email}")
    String getUserId(String Email);

    @Select("SELECT UserID FROM userinfo WHERE CustomerId = #{CustomerId}")
    String getUserIdby_cid(String CustomerId);

    @Select("SELECT UserID,Email,UserName,Mobile FROM userinfo WHERE Email = #{Email}")
    UserInfo getUserInfo(String Email);

    @Select("SELECT picturename FROM userpicture WHERE username = #{UserName}")
    String getPictureName(String UserName);

    @Select("SELECT CustomerId FROM userinfo WHERE Email = #{Email}")
    String getCustomerId(String Email);

    @Select("SELECT Balance FROM userinfo WHERE UserID = #{UserID}")
    String selectBalance(String UserID);

    @Select("SELECT name FROM username WHERE ID = #{UserID}")
    String selectUserName(String UserID);

    @Select("SELECT UserName FROM userinfo WHERE Email = #{Email}")
    String getUserName(String Email);

    @Select("SELECT Balance FROM userinfo WHERE CustomerId = #{CustomerId}")
    String getBalance(String CustomerId);

    @Select("SELECT C_name,C_Mobile,C_address,Type FROM companyinfo")
    List<Company> selectCompany();

    @Select("SELECT count(*) FROM userinfo WHERE UserName = #{username}")
    int checkeUserName();

    @Select("SELECT Title,Content,Date FROM promotion WHERE Date Between  date_sub(#{Date}, interval 1 week) and #{Date}")
    List<Promotion> Get_Promotion(String Date);

    //插入
    @Insert("INSERT INTO transaction (payUser,receiveUser,Amount,Date) VALUES (#{payUser},#{receiveUser},#{Amount},#{Date})")
    int transferTo(Transaction transaction);

    @Insert("INSERT INTO transaction_company (payUser,receiveUser,Amount,Date) VALUES (#{payUser},#{receiveUser},#{Amount},#{Date})")
    int transferTo_Company(Transaction transaction);

    @Insert("INSERT INTO userinfo (UserID,password,Email,UserName,Mobile,Pin,Balance,CustomerId,PictureName) VALUES (#{UserID},md5(#{password}),#{email},#{username},#{Mobile},md5(#{pin}),#{Balance},#{CustomerId},#{PictureName})")
    int addUserInfo(UserInfo userInfo);

    @Insert("INSERT INTO userinfo (CustomerId) VALUES (#{id})")
    int addCustomerId(String id);

    @Insert("INSERT INTO userinfo (UserID) VALUES (#{UserID})")
    int addUserID(String UserID);


    @Insert("INSERT INTO userinfo (Email,Password) VALUES (#{email},md5(#{password}))")
    int addUser(User user);




    //更新
    @Update("UPDATE userinfo SET Balance=#{balance} WHERE UserID = #{UserID}")
    void updateBalance(String balance,String UserID);

    @Update("UPDATE userinfo SET Balance=#{balance} WHERE CustomerId = #{CustomerId}")
    void updateBalances(String balance,String CustomerId);

    @Update("UPDATE userinfo SET Password = md5(#{newPswd}) WHERE Email = #{email}")
    void changePswd(String newPswd, String email);

    @Update("UPDATE userinfo SET Pin = md5(#{Pin}) WHERE Email = #{email}")
    void changePayPswd(String Pin, String email);

    @Update("UPDATE userinfo SET UserName = #{username} WHERE Email = #{email}")
    void changeUsername(String username, String email);

    @Update("UPDATE userinfo SET PictureName = #{PictureName} WHERE Email = #{email}")
    void updatePictureName(String PictureName, String email);

    @Update("UPDATE userinfo SET Mobile= #{Mobile},pin= md5(#{pin}),Balance= #{Balance},CustomerId= #{CustomerId},PictureName= #{PictureName} WHERE Email = #{email}")
    void updateUserInfo(String Mobile,String pin,String Balance,String CustomerId,String PictureName, String email);





}
