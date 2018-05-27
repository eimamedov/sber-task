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
        void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent);

        void onError(Exception exception);
    }

    /**
     * Get an {@link WeatherCurrent} for current location.
     * @param weatherCurrentCallback an {@link WeatherCurrentCallback} to handle results
     */
    void getCurrentWeatherForCurrentLocation(WeatherCurrentCallback weatherCurrentCallback);
}
