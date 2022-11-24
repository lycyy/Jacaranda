package com.example.service.Mapper;

import com.example.service.Bean.In.CompanyInfo;
import com.example.service.Bean.In.Promotion;
import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import com.example.service.Bean.Out.Bill;
import com.example.service.Bean.Out.CompanyBill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CompanyUserMapper {
    //查询
    @Select("SELECT count(*) FROM companyview WHERE Email = #{email} and password = md5(#{password}) ")
    int checkUser(User user);

    @Select("SELECT count(*) FROM companyinfo WHERE Email = #{email}")
    int findUserInfo(String email);

    @Select("SELECT CompanyID,C_name FROM companyinfo WHERE Email = #{email}")
    CompanyInfo getUserInfo(String Email);

    @Select("SELECT C_name FROM companyinfo WHERE Email = #{email}")
    String getUsername(String Email);

    @Select("SELECT CompanyID FROM companyinfo WHERE Email = #{email}")
    String getUserId(String email);

    @Select("SELECT count(*) FROM companyinfo WHERE CompanyID = #{CompanyID} ")
    int findUserID(String CompanyID);

    @Select("SELECT count(*) FROM companyinfo WHERE Email = #{email} ")
    int findUser(String Email);

    @Select("SELECT Balance FROM companyinfo WHERE CompanyID = #{CompanyID} ")
    String selectBalance(String CompanyID);

    @Select("SELECT CompanyID FROM companyinfo WHERE Email = #{email} ")
    String selectUserID(String email);

    @Select("SELECT PictureName FROM companyinfo WHERE C_name = #{username} ")
    String getPicture(String username);

    @Select("SELECT payUser,Amount,Date FROM transaction_company WHERE receiveUser = #{receiveUser}")
    List<CompanyBill> selectBill(String receiveUser);

    @Select("SELECT C_Mobile FROM companyinfo WHERE Email = #{email}")
    String checkUserInfo(String email);
    //增添
    @Insert("INSERT INTO companyinfo (CompanyID,Password,PictureName,C_name,C_Mobile,C_address,Balance,Email,Type) VALUES (#{CompanyID},md5(#{password}),#{Picture},#{C_name},#{C_Mobile},#{C_address},#{Balance},#{email},#{type})")
    int addUserInfo(CompanyInfo companyInfo);

    @Insert("INSERT INTO companyinfo (Email,Password) VALUES (#{email},md5(#{password}))")
    int addUser(User user);
    @Insert("INSERT INTO promotion (Title,Content,Date) VALUES (#{title},#{content},#{date})")
    int Publish_Promotion(Promotion promotion);
    //修改
    @Update("UPDATE companyinfo SET Balance=#{balance} WHERE CompanyID = #{CompanyID}")
    void updateBalances(String balance,String CompanyID);

    @Update("UPDATE companyinfo SET PictureName = #{PictureName} WHERE Email = #{email}")
    void updateCompanyPicture(String PictureName, String email);

    @Update("UPDATE companyinfo SET Head_portrait = #{PictureName} WHERE Email = #{email}")
    void updateCompanyHead(String PictureName, String email);

    @Update("UPDATE companyinfo SET Password = md5(#{newPswd}) WHERE Email = #{email}")
    void changePswd(String newPswd, String email);

    @Update("UPDATE companyinfo SET C_name = #{username} WHERE Email = #{email}")
    void changeUsername(String username, String email);

    @Update("UPDATE companyinfo SET C_Mobile= #{C_Mobile},C_address = #{C_address},Balance= #{Balance},PictureName= #{PictureName},Type = #{type} WHERE Email = #{email}")
    void updateUserInfo(String C_Mobile,String C_address,String Balance,String PictureName,String type, String email);



    //删除
}
