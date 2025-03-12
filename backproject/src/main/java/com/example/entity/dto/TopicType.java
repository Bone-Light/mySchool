package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@TableName("db_topic_type")
public class TopicType implements BaseData {
    @TableId(type = IdType.AUTO)
    Integer id;
    String name;
    String description;
    String color;
}
