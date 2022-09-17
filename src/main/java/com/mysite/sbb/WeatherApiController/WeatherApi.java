package com.mysite.sbb.WeatherApiController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // json을 반환한다.
@Slf4j
public class WeatherApi {

    @Autowired
    private static OpenWeather openWeather;

    private String WeatherJsonData; // 날씨 정보를 담고 있는 json data

    @GetMapping("/chuncheon/current/temperature")
    public String getWeatherJsonData(){

        SetWeather Info = OpenWeather.getWeatherData();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            WeatherJsonData = objectMapper.writeValueAsString(Info);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        return WeatherJsonData; // json data는 String 이다.
    }

}
