package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Mapper.ImageMapper;
import com.example.entity.dto.StoreImage;
import com.example.util.Const;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ImageService extends IService<StoreImage> {
    String uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam(Const.ATTR_USER_ID) int id) throws IOException;
    String uploadImage(@RequestParam("file") MultipartFile file, @RequestParam(Const.ATTR_USER_ID) int id) throws IOException;
    void fetchImageFromMinio(OutputStream stream, String imagePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
