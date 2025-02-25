package com.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class BackprojectApplicationTests {
    @Resource
    JavaMailSender mailSender;

    @Test
    void contextLoads() {
        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件标题
        message.setSubject("【电子科技大学教务处】");
        //设置邮件内容
        message.setText("XXX同学您好");
        //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
        message.setTo("g15813695017@163.com");
        //邮件发送者，这里要与配置文件中的保持一致
        message.setFrom("r15813695017@163.com");
        //OK，万事俱备只欠发送
        mailSender.send(message);
    }

}
