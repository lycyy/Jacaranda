package com.example.service.Controller;


import com.example.service.Bean.Result;
import com.example.service.Service.FileService;

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

    @PostMapping(value = "/Upload")
    public Result upload(@RequestParam MultipartFile file,@RequestHeader(value = "token") String token) {
        System.out.println("Upload:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        boolean b = fileService.upload(file, token);
        Result result = new Result();
        if (b) {
            result = result.success("上传成功");
        }else {
            result = result.fail("上传失败");
        }
        return result;

    }


    @PostMapping(value = "/Download")
    public ResponseEntity<FileSystemResource> downloadImage(@RequestHeader(value = "token") String token){
        System.out.println("Download:"+new Date(System.currentTimeMillis()));
        System.out.println("-----------------------------------------------------------------------------------------------");
        ResponseEntity<FileSystemResource> file = fileService.download(token);
        return file;
    }
}
