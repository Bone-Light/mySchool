package com.example.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@RabbitListener(queuesToDeclare = @Queue("mail"), concurrency = "3")
@Component
public class MailQueueListener {
    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        System.out.println("123123");
        String email = data.get("email").toString();
        Integer code = (Integer) data.get("code");
        String type = (String) data.get("type");

        SimpleMailMessage mailMessage =  switch (type) {
            case "register" -> createMessage("欢迎注册我们的网站",
                    "您的邮箱验证码为: " + code + ", 有效时间3min，为了保证您的账号安全，请勿向他人泄露验证码信息",
                            email);
            case "reset" -> createMessage("您的重置密码邮件",
                    "您好，您正在进行重置密码操作，验证码: " + code + "有效时间3min，若非本人操作，请无视",
                    email);
            default -> null;
        };

        if(mailMessage == null)  return;
        mailSender.send(mailMessage);
    }

    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
