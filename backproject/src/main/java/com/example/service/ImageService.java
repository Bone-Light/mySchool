package com.example.service;

import com.example.util.Const;
import io.minio.errors.*;
import jakarta.servlet.ServletOutputStream;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ImageService {
    String uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam(Const.ATTR_USER_ID) int id) throws IOException;

    void fetchImageFromMinio(OutputStream stream, String imagePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
