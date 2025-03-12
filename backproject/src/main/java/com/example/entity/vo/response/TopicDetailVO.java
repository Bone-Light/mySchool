package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class TopicDetailVO {
    Integer id;
    String title;
    String content;
    Integer type;
    Date time;
    User user;
    Interact interact;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Interact {
        Boolean like;
        Boolean collect;
    }

    @Data
    public static class User {
        Integer id;
        String username;
        String avatar;
        String description;
        Integer gender;
        String qq;
        String wx;
        String phone;
        String email;
    }
}
