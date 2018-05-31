package eim.yar.sbertask.data.entity.mapper;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.domain.model.WeatherCurrent;

/**
 * Mapper class used to transform {@link WeatherEntity} (in the data layer)
 * to {@link WeatherCurrent} in the domain layer.
 */
public class WeatherEntityDataMapper {

    /**
     * Transform a {@link WeatherEntity} into an {@link WeatherCurrent}.
     *
     * @param weatherEntity Object to be transformed.
     * @return {@link WeatherCurrent} if valid {@link WeatherEntity} otherwise null.
     */
    public WeatherCurrent transform(WeatherEntity weatherEntity) {
        WeatherCurrent weatherCurrent = null;
        if (weatherEntity != null) {
            weatherCurrent = new WeatherCurrent();
            if (weatherEntity.getMain() != null) {
                weatherCurrent.setTemperature(weatherEntity.getMain().getTemperature());
            }
            if (weatherEntity.getCoord() != null) {
                weatherCurrent.setLatitude(weatherEntity.getCoord().getLat());
                weatherCurrent.setLongitude(weatherEntity.getCoord().getLon());
            }
            weatherCurrent.setAddress(weatherEntity.getName());
            weatherCurrent.setTimestamp(weatherEntity.getTimestamp());
        }
        return weatherCurrent;
    }
}
