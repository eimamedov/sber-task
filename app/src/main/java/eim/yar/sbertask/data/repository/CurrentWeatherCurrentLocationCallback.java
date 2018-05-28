package eim.yar.sbertask.data.repository;

import android.location.Location;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityDataMapper;
import eim.yar.sbertask.data.exception.WeatherNotFoundException;
import eim.yar.sbertask.data.repository.dataloader.WeatherCurrentDataLoader;
import eim.yar.sbertask.data.repository.locationhelper.LocationHelper;
import eim.yar.sbertask.domain.model.WeatherCurrent;
import eim.yar.sbertask.domain.repository.WeatherRepository;

/**
 * Current location callback used to get current weather data in {@link WeatherDataRepository}
 * getCurrentWeatherForCurrentLocation method.
 */
class CurrentWeatherCurrentLocationCallback
        implements LocationHelper.CurrentLocationCallback {

    /**
     * Object to load weather data.
     */
    final WeatherCurrentDataLoader weatherCurrentDataLoader;

    /**
     * getCurrentWeatherForCurrentLocation method callback.
     */
    final WeatherRepository.WeatherCurrentCallback methodCallback;

    /**
     * Object to transform a {@link WeatherEntity} objects into an {@link WeatherCurrent} objects.
     */
    final WeatherEntityDataMapper dataMapper;

    /**
     * Construct a {@link CurrentWeatherCurrentLocationCallback}.
     * @param weatherCurrentDataLoader object to load weather data
     * @param methodCallback getCurrentWeatherForCurrentLocation method callback
     * @param mapper getCurrentWeatherForCurrentLocation method callback
     */
    CurrentWeatherCurrentLocationCallback(
            WeatherCurrentDataLoader weatherCurrentDataLoader,
            WeatherRepository.WeatherCurrentCallback methodCallback,
            WeatherEntityDataMapper mapper) {
        this.weatherCurrentDataLoader = weatherCurrentDataLoader;
        this.methodCallback = methodCallback;
        dataMapper = mapper;
    }

    @Override
    public void onCurrentLocationFound(Location currentLocation) {
        weatherCurrentDataLoader.loadCurrentWeatherByCoordinates(
                currentLocation.getLatitude(), currentLocation.getLongitude(),
                dataLoaderCallback);
    }

    @Override
    public void onError(Exception exception) {
        if (methodCallback != null) {
            methodCallback.onError(exception);
        }
    }

    /**
     * Current weather callback used to get current weather data for current location.
     */
    final WeatherCurrentDataLoader.WeatherCurrentCallback dataLoaderCallback =
            new WeatherCurrentDataLoader.WeatherCurrentCallback() {
                @Override
                public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                    if (methodCallback != null) {
                        WeatherCurrent weatherCurrent = dataMapper.transform(weatherEntity);
                        if (weatherCurrent == null) {
                            methodCallback.onError(
                                    new WeatherNotFoundException("Weather data not found"));
                        } else {
                            methodCallback.onCurrentWeatherLoaded(weatherCurrent);
                        }
                    }
                }

                @Override
                public void onError(Exception exception) {
                    if (methodCallback != null) {
                        methodCallback.onError(exception);
                    }
                }
            };
}
