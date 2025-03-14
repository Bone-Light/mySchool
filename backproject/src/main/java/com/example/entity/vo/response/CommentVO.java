package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
public class CommentVO {
    int id;
    String content;
    Date time;
    String quote;
    User user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        Integer id;
        String username;
        String avatar;
        Integer gender;
        String qq;
        String wx;
        String email;
        String phone;
    }
}
