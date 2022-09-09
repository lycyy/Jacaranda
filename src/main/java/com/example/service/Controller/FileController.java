package com.example.service.Controller;


import com.example.service.Bean.In.User;
import com.example.service.Bean.In.UserInfo;
import com.example.service.Bean.In.Usernames;
import com.example.service.Bean.Result;
import com.example.service.Service.FileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@ResponseBody
public class FileController {

    @Autowired
    FileService fileService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping(value = "/HeadUpload")
    public Result upload(@RequestParam MultipartFile file,@RequestHeader(value = "token") String token) {
        logger.info("Upload interface is call");
        boolean b = fileService.upload(file, token);
        Result result = new Result();
        if (b) {
            result = result.success("上传成功");
        }else {
            result = result.fail("上传失败");
        }
        return result;

    }

    @PostMapping(value = "/C_Upload")
    public Result C_upload(@RequestParam MultipartFile file,@RequestHeader(value = "token") String token) {
        logger.info("C_Upload interface is call");
        boolean b = fileService.Companyupload(file, token);
        Result result = new Result();
        if (b) {
            result = result.success("上传成功");
        }else {
            result = result.fail("上传失败");
        }
        return result;

    }

    @PostMapping(value = "/C_HeadUpload")
    public Result C_Hupload(@RequestParam MultipartFile file,@RequestHeader(value = "token") String token) {
        logger.info("C_HeadUpload interface is call");
        boolean b = fileService.CompapnyHead(file, token);
        Result result = new Result();
        if (b) {
            result = result.success("上传成功");
        }else {
            result = result.fail("上传失败");
        }
        return result;

    }


    @PostMapping(value = "/Head_Download")
    public ResponseEntity<FileSystemResource> downloadHead(@RequestBody Usernames usernames){
        logger.info("Head_Download interface is call");
        ResponseEntity<FileSystemResource> file = fileService.download(usernames.getUsername());
        return file;
    }

    @PostMapping(value = "/Picture_Download")
    public ResponseEntity<FileSystemResource> downloadPicture(@RequestBody Usernames usernames){
        logger.info("Picture_Download interface is call");
        ResponseEntity<FileSystemResource> file = fileService.downloadCompany(usernames.getUsername());
        return file;
    }

}
