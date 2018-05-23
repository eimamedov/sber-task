package eim.yar.sbertask.domain.repository;

import eim.yar.sbertask.domain.model.WeatherCurrent;

/**
 * Interface that represents a Repository for getting weather related data.
 */
public interface WeatherRepository {

    /**
     * Callback used to be notified when either a current weather data has been loaded or
     * an error happened.
     */
    interface WeatherCurrentCallback {
        void onWeatherEntityLoaded(WeatherCurrent weatherCurrent);

        void onError(Exception exception);
    }

    /**
     * Get an {@link WeatherCurrent} by specified location.
     * @param latitude latitude value
     * @param longitude longitude value
     * @param weatherCurrentCallback an {@link WeatherCurrentCallback} to handle results
     */
    void getCurrentWeatherByCoordinates(double latitude, double longitude,
                     WeatherCurrentCallback weatherCurrentCallback);
}
