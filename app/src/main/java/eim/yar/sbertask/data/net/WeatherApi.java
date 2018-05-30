package eim.yar.sbertask.data.net;

import eim.yar.sbertask.data.entity.WeatherEntity;

/**
 * WeatherApi for retrieving weather data from the network.
 */
public interface WeatherApi {

    /**
     * Callback to be notified when getting a current weather data from the network.
     */
    interface WeatherCurrentCallback {
        void onWeatherEntityLoaded(WeatherEntity weatherEntity);

        void onError(Exception exception);
    }

    /**
     * Retrieves a current weather data by coordinates from the network.
     * @param latitude latitude value
     * @param longitude longitude value
     * @param weatherCallback {@link WeatherCurrentCallback} to be notified when current weather data
     * has been retrieved.
     */
    void getCurrentWeatherByCoordinates(double latitude, double longitude,
                                      WeatherCurrentCallback weatherCallback);
}
