package com.example.ecommercebackend.Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public String uploadImage(String path, MultipartFile productImage) throws IOException;
}
