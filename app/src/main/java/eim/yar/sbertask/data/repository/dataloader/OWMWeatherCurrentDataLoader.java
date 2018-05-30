package eim.yar.sbertask.data.repository.dataloader;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.net.OWMWeatherApi;
import eim.yar.sbertask.data.net.WeatherApi;

/**
 * {@link WeatherCurrentDataLoader} implementation based on OpenWeatherMap api.
 */
public class OWMWeatherCurrentDataLoader implements WeatherCurrentDataLoader {

    /**
     * Object to work with weather api.
     */
    private final WeatherApi weatherApi;

    /**
     * Construct a {@link OWMWeatherCurrentDataLoader}
     * @param weatherApi object to work with OpenWeatherMap api
     */
    public OWMWeatherCurrentDataLoader(OWMWeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }

    @Override
    public void loadCurrentWeatherByCoordinates(double latitude, double longitude,
            final WeatherCurrentCallback weatherCurrentCallback) {
        weatherApi.getCurrentWeatherByCoordinates(latitude, longitude,
                new WeatherApi.WeatherCurrentCallback() {
            @Override
            public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                weatherCurrentCallback.onWeatherEntityLoaded(weatherEntity);
            }

            @Override
            public void onError(Exception exception) {
                weatherCurrentCallback.onError(exception);
            }
        });
    }
}
