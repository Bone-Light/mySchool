package com.example.service;

import com.example.entity.vo.response.WeatherVO;

import java.io.IOException;

public interface WeatherService {
    WeatherVO fetchWeather(double latitude, double longitude) throws IOException;
}
