package com.example.service.Controller;


import com.example.service.Bean.In.Images;
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


    @PostMapping(value = "/C_PictureUpload")
    public Result C_upload(@RequestParam MultipartFile file,@RequestHeader(value = "token") String token) {
        logger.info("C_PictureUpload interface is call");
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




    @PostMapping(value = "/Picture_Download")
    public ResponseEntity<FileSystemResource> downloadPicture(@RequestBody Images images){
        logger.info("Picture_Download interface is call");
        ResponseEntity<FileSystemResource> file = fileService.downloadCompany(images.getImages());
        return file;
    }

}
