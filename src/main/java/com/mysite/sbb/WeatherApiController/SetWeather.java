package com.mysite.sbb.WeatherApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SetWeather {

    private double temp;

    public SetWeather(double temp){
        this.temp = temp;
    }

}
