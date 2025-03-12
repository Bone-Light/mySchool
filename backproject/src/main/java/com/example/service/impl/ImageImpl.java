package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Mapper.AccountMapper;
import com.example.Mapper.ImageMapper;
import com.example.entity.dto.Account;
import com.example.entity.dto.StoreImage;
import com.example.service.ImageService;
import com.example.util.Const;
import com.example.util.FlowUtils;
import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class ImageImpl extends ServiceImpl<ImageMapper, StoreImage> implements ImageService {
    @Resource
    private MinioClient minioClient;

    @Resource
    AccountMapper accountMapper;

    @Resource
    private FlowUtils flowUtils;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @Override
    public String uploadAvatar(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replaceAll("-", "");
        imageName = "/avatar/" + imageName;
        PutObjectArgs putObjectArgs = PutObjectArgs
                .builder()
                .bucket("study")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(putObjectArgs);
            String avatar = accountMapper.selectById(id).getAvatar();
            this.deleteOldAvatar(avatar);
            if(accountMapper.update(null, Wrappers.<Account>update()
                    .eq("id", id).set("avatar", imageName)) > 0) {
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("图片上传出现问题: {}", String.valueOf(e));
            return null;
        }
    }

    @Override
    public String uploadImage(MultipartFile file, int id) throws IOException {
        String key = Const.FORUM_LIMIT_IMAGE_COUNTER + id;
        if(!flowUtils.limitPeriodCountCheck(key, 15, 1800)){
            return null;
        }
        String imageName = UUID.randomUUID().toString().replaceAll("-", "");
        Date date = new Date();
        imageName = "/cache/" + format.format(date) + "/" + imageName;

        PutObjectArgs putObjectArgs = PutObjectArgs
                .builder()
                .bucket("study")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(putObjectArgs);
            if(this.save(new StoreImage(id,imageName,date))) {
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("图片上传出现问题: {}", String.valueOf(e));
            return null;
        }

    }

    @Override
    public void fetchImageFromMinio(OutputStream stream, String imagePath) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("study")
                .object(imagePath)
                .build();
        GetObjectResponse response = minioClient.getObject(args);
        IOUtils.copy(response, stream);
    }

    private void deleteOldAvatar(String avatar) throws Exception {
        if(avatar == null || avatar.isEmpty()) return;
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("study")
                .object(avatar)
                .build();
        minioClient.removeObject(removeObjectArgs);


    }
}
