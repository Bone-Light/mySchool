package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.WeatherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
    @Resource
    WeatherService weatherService;

    @GetMapping("/weather")
    public RestBean<WeatherVO> weather(double longitude, double latitude) throws IOException {
        WeatherVO vo = weatherService.fetchWeather(latitude, longitude);
        return vo == null?
                RestBean.failure(400, "获取地理位置信息和天气失败，请联系管理员") : RestBean.success(vo);
    }
}
