package eim.yar.sbertask.data.repository.dataloader;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.net.OWMWeatherApi;
import eim.yar.sbertask.data.net.WeahterApi;

/**
 * {@link WeatherCurrentDataLoader} implementation based on OpenWeatherMap api.
 */
public class OWMWeatherCurrentDataLoader implements WeatherCurrentDataLoader {

    /**
     * Object to work with weather api.
     */
    private final WeahterApi weahterApi;

    /**
     * Construct a {@link OWMWeatherCurrentDataLoader}
     * @param weahterApi object to work with OpenWeatherMap api
     */
    public OWMWeatherCurrentDataLoader(OWMWeatherApi weahterApi) {
        this.weahterApi = weahterApi;
    }

    @Override
    public void loadCurrentWeatherByCoordinates(double latitude, double longitude,
            final WeatherCurrentCallback weatherCurrentCallback) {
        weahterApi.getCurrentWeatherByCoordinates(latitude, longitude,
                new WeahterApi.WeatherCurrentCallback() {
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
