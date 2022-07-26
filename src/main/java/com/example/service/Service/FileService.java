package com.example.service.Service;

import com.example.service.Bean.Code;
import com.example.service.Bean.FileName;
import com.example.service.Bean.In.User;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface FileService {

    public boolean upload(MultipartFile file,String token);
    public ResponseEntity<FileSystemResource> download(String token);
}
