package eim.yar.sbertask.data.repository.dataloader;

import eim.yar.sbertask.data.entity.WeatherEntity;

/**
 * Interface that represents a data loader from where {@link WeatherEntity} objects are loaded.
 */
public interface WeatherCurrentDataLoader {

    /**
     * Callback used for clients to be notified when either current weather data has been
     * retrieved successfully or any error occurred.
     */
    interface WeatherCurrentCallback {
        void onWeatherEntityLoaded(WeatherEntity weatherEntity);

        void onError(Exception exception);
    }

    /**
     * Load current weather {@link WeatherEntity} data by geographic coordinates.
     * @param latitude latitude value
     * @param longitude longitude value
     * @return an {@link WeatherEntity} with current weather data for specified city name
     */
    void loadCurrentWeatherByCoordinates(double latitude, double longitude,
                                         WeatherCurrentCallback weatherCurrentCallback);
}
