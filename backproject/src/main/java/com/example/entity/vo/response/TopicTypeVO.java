package com.example.entity.vo.response;

import com.example.service.WeatherService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicTypeVO {
    Integer id;
    String name;
    String description;
    String color;
}
