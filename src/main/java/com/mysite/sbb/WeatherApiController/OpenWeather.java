package com.mysite.sbb.WeatherApiController;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.List;

@Data
public class OpenWeather {

    private List<Weather> weather;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather"; // 기존 URL
    private static final String API_KEY = "1a159c8fec9ab3d5a420d8c2fbded16e"; // 발급받은 API KEY

    @Autowired
    private static SetWeather setWeather;

    // OpenWeatherMap에서 얻어온 json객체를 response로 받아서 원하는 정보만을 담은 객체를 만들어 return 한다.
    public static SetWeather getWeatherData() {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        try {
            urlBuilder.append("?" + URLEncoder.encode("q", "UTF-8") + "=Chuncheon");
            urlBuilder.append("&" + URLEncoder.encode("appid", "UTF-8") + "=" + API_KEY);
            urlBuilder.append("&" + URLEncoder.encode("lang", "UTF-8") + "=kr");
            urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric");

            RestTemplate restTemplate = new RestTemplate();
            OpenWeather response = restTemplate.getForObject(urlBuilder.toString(), OpenWeather.class);
            double temp = (Math.round((response.getMain().temp)*10))/10.0; // 춘천의 현재 온도를 소수 첫째까지(둘째 자리에서 반올림) 나타낸다.
            setWeather = new SetWeather(temp); // 춘천의 온도를 set 해서 새로운 객체를 만든다.

        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생 시 출력
        }
        return setWeather; // 춘천의 현재 온도를 담은 객체를 return
    }


    // 내부 매개 뱐수
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private int visibility; // 가시성
    private long dt; // 데이터 계산 시간, 유닉스, UTC
    private int timezone; // UTC에서 초 단위로 이동
    private long id; // 도시 ID
    private String name; // 도시 이름 --> 춘천의 이름
    private int cod; // 내부 매개 변수

    @Data
    public static class Weather {
        private int id; // 기상 조건 ID
        private String main; // 날씨 매개 변수 그룹(비, 눈, 극한 등)
        private String description; // 그룹 내 날씨 조건
        private String icon; // 날씨 아이콘 ID
    }

    @Data
    public static class Main {
        private float temp; // 온도 단위 기본값 --> 춘천의 현재 온도
        private float feels_like; // 온도 단위 기본값
        private float temp_min; // 현재 최저 온도
        private float temp_max; // 현재 최대 온도
        private int pressure; // 대기압 (해수면, 해수면 또는 grnd_level  데이터가 없는 경우)
        private float humidity; // 습도
        private float sea_level; // 해수면의 대기압
        private float grnd_level; // 지면에서의 대기압
    }

    @Data
    public static class Wind {
        private float speed; // 풍속 단위 기본값 : m/s
        private int deg; // 풍향, 도
        private float gust; // 바람 돌풍 단위 기본값 : m/s
    }

    @Data
    public static class Clouds{
        private int all; // 흐림
    }

    @Data
    public static class Rain {
        @JsonProperty("1h")
        private float rain1h; // 지난 1 시간 동안의 강우량
        @JsonProperty("3h")
        private float rain3h; // 지난 3 시간 동안의 강우량
    }

    @Data
    public static class Snow {
        @JsonProperty("1h")
        private float snow1h; // 지난 1 시간 동안의 눈량 mm
        @JsonProperty("3h")
        private float snow3h; // 지난 3 시간 동안의 눈량 mm
    }

    @Data
    public static class Sys {
        private int type;
        private int id;
        private String country; // 국가 코드
        private long sunrise; // 일출 시간, 유닉스, UTC
        private long sunset; // 일몰 시간, 유닉스, UTC
    }
}