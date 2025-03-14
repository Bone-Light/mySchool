package com.example.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class TopicUpdateVO {
    @Min(0)
    int id;
    @Min(0)
    int type;
    @Length(min = 1, max = 60)
    String title;
    JSONObject content;
}
