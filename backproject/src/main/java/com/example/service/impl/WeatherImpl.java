package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.WeatherService;
import com.example.util.Const;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

@Service
public class WeatherImpl implements WeatherService {

    @Resource
    RestTemplate restTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Value("${spring.weather.key}")
    String key;

    @Override
    public WeatherVO fetchWeather(double longitude, double latitude) throws IOException {
        return fetchFromCache(longitude, latitude);
    }

    private WeatherVO fetchFromCache(double longitude, double latitude) throws IOException {
        JSONObject geo = this.decompressStringToJson(
                restTemplate.getForObject("https://geoapi.qweather.com/v2/city/lookup?location="+ latitude+","+longitude +"&key="+key, byte[].class));
        if(geo == null) return null;
        JSONObject location = geo.getJSONArray("location").getJSONObject(0);
        int id = location.getInteger("id");
        String key = Const.FORUM_LIMIT_WEATHER_CACHE + id;;
        String cache = stringRedisTemplate.opsForValue().get(key);
        if(cache != null){
            return JSONObject.parseObject(cache, WeatherVO.class);
        }
        WeatherVO vo = this.fetchFromAPI(id, location);
        if(vo == null){return null;}
        stringRedisTemplate.opsForValue().set(key,JSONObject.toJSONString(vo), 1, TimeUnit.HOURS);
        return vo;
    }

    private WeatherVO fetchFromAPI(int id, JSONObject location) throws IOException {
        WeatherVO vo = new WeatherVO();
        vo.setLocation(location);
        JSONObject now = this.decompressStringToJson(restTemplate.getForObject(
                "https://devapi.qweather.com/v7/weather/now?location=" + id + "&key="+key, byte[].class));
        if(now == null){return null;}
        vo.setNow(now.getJSONObject("now"));
        JSONObject hourly = this.decompressStringToJson(restTemplate.getForObject(
                "https://devapi.qweather.com/v7/weather/24h?location=" + id + "&key="+key, byte[].class));
        if(hourly == null){return null;}
        vo.setHourly(new JSONArray(hourly.getJSONArray("hourly").stream().limit(5).toList()));
        return vo;
    }

    private JSONObject decompressStringToJson(byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(data));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = gzipInputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, read);
            }
            gzipInputStream.close();
            stream.close();
            return JSONObject.parseObject(stream.toString());
        } catch (IOException e){
            return null;
        }
    }
}
