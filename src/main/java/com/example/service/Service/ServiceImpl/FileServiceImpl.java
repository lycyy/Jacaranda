package com.example.service.Service.ServiceImpl;

import com.example.service.Mapper.CompanyUserMapper;
import com.example.service.Mapper.UserMapper;
import com.example.service.Service.FileService;
import com.example.service.Util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CompanyUserMapper companyUserMapper;
    @Autowired
    public TokenUtil tokenUtil;
    @Override
    public boolean upload(MultipartFile file,String token) {
        String uploadPathStr = "/picture";
        String userEmail = tokenUtil.getValue(token);
        String UserId = userMapper.getUserId(userEmail);
        String fileName = UserId + "'s Head portrait";
        if (file == null || file.isEmpty() || fileName == null || fileName.isEmpty()) {
            return false;
        }
        try(InputStream inputStream = file.getInputStream()) {
            Path uploadPath = Paths.get(uploadPathStr);
            if (!uploadPath.toFile().exists())
                uploadPath.toFile().mkdirs();
            Files.copy(inputStream, Paths.get(uploadPathStr).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            userMapper.updatePictureName(fileName,userEmail);
            System.out.println("upload file , filename is "+fileName);
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean Companyupload(MultipartFile file,String token) {
        String uploadPathStr = "/picture";
        String userEmail = tokenUtil.getValue(token);
        String UserId = companyUserMapper.getUserId(userEmail);
        String fileName = UserId + "'s picture";
        if (file == null || file.isEmpty() || fileName == null || fileName.isEmpty()) {
            return false;
        }
        try(InputStream inputStream = file.getInputStream()) {
            Path uploadPath = Paths.get(uploadPathStr);
            if (!uploadPath.toFile().exists())
                uploadPath.toFile().mkdirs();
            Files.copy(inputStream, Paths.get(uploadPathStr).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            companyUserMapper.updateCompanyPicture(fileName,userEmail);
            System.out.println("upload file , filename is "+fileName);
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean CompapnyHead(MultipartFile file,String token) {
        String uploadPathStr = "/picture";
        String userEmail = tokenUtil.getValue(token);
        String username = companyUserMapper.getUsername(userEmail);
        String fileName = username + "'s Head portrait";
        if (file == null || file.isEmpty() || fileName == null || fileName.isEmpty()) {
            return false;
        }
        try(InputStream inputStream = file.getInputStream()) {
            Path uploadPath = Paths.get(uploadPathStr);
            if (!uploadPath.toFile().exists())
                uploadPath.toFile().mkdirs();
            Files.copy(inputStream, Paths.get(uploadPathStr).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            companyUserMapper.updateCompanyHead(fileName,userEmail);
            System.out.println("upload file , filename is "+fileName);
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ResponseEntity<FileSystemResource> download(String UserName) {
        String uploadPathStr = "/picture";
        String files = userMapper.getPictureName(UserName);
        if (files == null || files.isEmpty()) {
            return null;
        }
        File file = Paths.get(uploadPathStr).resolve(files).toFile();
        if (file.exists() && file.canRead()) {
            return ResponseEntity.ok()
                    .contentType(file.getName().contains(".jpg") ? MediaType.IMAGE_JPEG:MediaType.IMAGE_PNG)
                    .body(new FileSystemResource(file));
        }else {
            return null;
        }
    }

    @Override
    public ResponseEntity<FileSystemResource> downloadCompany(String UserName) {
        String uploadPathStr = "/picture";
        String files =companyUserMapper.getPicture(UserName);
        if (files == null || files.isEmpty()) {
            return null;
        }
        File file = Paths.get(uploadPathStr).resolve(files).toFile();
        if (file.exists() && file.canRead()) {
            return ResponseEntity.ok()
                    .contentType(file.getName().contains(".jpg") ? MediaType.IMAGE_JPEG:MediaType.IMAGE_PNG)
                    .body(new FileSystemResource(file));
        }else {
            return null;
        }
    }
}
