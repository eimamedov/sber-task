package eim.yar.sbertask.data.repository;

import android.location.Location;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityDataMapper;
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
     * Object to work with location data.
     */
    final LocationHelper locationHelper;

    /**
     * getCurrentWeatherForCurrentLocation method callback.
     */
    final WeatherRepository.WeatherCurrentCallback methodCallback;

    /**
     * Object to transform a {@link WeatherEntity} objects into an {@link WeatherCurrent} objects.
     */
    final WeatherEntityDataMapper dataMapper;

    /**
     * Current weather callback used to get current weather data for current location.
     */
    final CurrentWeatherDataLoaderCallback dataLoaderCallback;

    /**
     * Construct a {@link CurrentWeatherCurrentLocationCallback}.
     * @param weatherCurrentDataLoader object to load weather data
     * @param locationHelper object to work with location data
     * @param methodCallback getCurrentWeatherForCurrentLocation method callback
     * @param mapper getCurrentWeatherForCurrentLocation method callback
     */
    CurrentWeatherCurrentLocationCallback(
            WeatherCurrentDataLoader weatherCurrentDataLoader,
            LocationHelper locationHelper,
            WeatherRepository.WeatherCurrentCallback methodCallback,
            WeatherEntityDataMapper mapper) {
        this.weatherCurrentDataLoader = weatherCurrentDataLoader;
        this.locationHelper = locationHelper;
        this.methodCallback = methodCallback;
        dataMapper = mapper;
        dataLoaderCallback = new CurrentWeatherDataLoaderCallback(locationHelper,
                methodCallback, dataMapper);
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
}
