package eim.yar.sbertask.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import eim.yar.sbertask.data.entity.WeatherEntity;

/**
 * Class used to transform weather data json strings to {@link WeatherEntity} objects.
 */
public class WeatherEntityJsonMapper {

    /**
     * Object for using Gson.
     */
    private final Gson gson;

    public WeatherEntityJsonMapper() {
        this.gson = new Gson();
    }

    /**
     * Transform from weather data json string to {@link WeatherEntity}.
     *
     * @param weatherJsonResponse A json representing a weather data
     * @return {@link WeatherEntity} object
     * @throws com.google.gson.JsonSyntaxException if the json string is not valid
     */
    public WeatherEntity transformWeatherEntity(String weatherJsonResponse)
            throws JsonSyntaxException {
        return gson.fromJson(weatherJsonResponse, WeatherEntity.class);
    }
}
