package com.example.service.Service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface FileService {

    public boolean upload(MultipartFile file,String token);
    public boolean Companyupload(MultipartFile file,String token);
    public ResponseEntity<FileSystemResource> download(String token);
    public boolean CompapnyHead(MultipartFile file,String token);
    public ResponseEntity<FileSystemResource> downloadCompany(String UserName);
}
