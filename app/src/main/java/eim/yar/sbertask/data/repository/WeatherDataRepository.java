package eim.yar.sbertask.data.repository;

import android.support.annotation.NonNull;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityDataMapper;
import eim.yar.sbertask.data.repository.dataloader.WeatherCurrentDataLoader;
import eim.yar.sbertask.data.repository.locationhelper.LocationHelper;
import eim.yar.sbertask.domain.model.WeatherCurrent;
import eim.yar.sbertask.domain.repository.WeatherRepository;

/**
 * {@link WeatherRepository} for retrieving weather data.
 */
public class WeatherDataRepository implements WeatherRepository {

    /**
     * Object to load weather data.
     */
    final WeatherCurrentDataLoader weatherCurrentDataLoader;

    /**
     * Object to work with location data.
     */
    final LocationHelper locationHelper;

    /**
     * Object to transform a {@link WeatherEntity} objects into an {@link WeatherCurrent} objects.
     */
    final WeatherEntityDataMapper dataMapper;

    /**
     * Constructs a {@link WeatherRepository}.
     * @param weatherCurrentDataLoader object to load weather data
     * @param locationHelper object to work with location data
     */
    public WeatherDataRepository(WeatherCurrentDataLoader weatherCurrentDataLoader,
                                 LocationHelper locationHelper,
                                 WeatherEntityDataMapper mapper) {
        this.weatherCurrentDataLoader = weatherCurrentDataLoader;
        this.locationHelper = locationHelper;
        dataMapper = mapper;
    }

    @Override
    public void getCurrentWeatherForCurrentLocation(
            @NonNull final WeatherCurrentCallback weatherCurrentCallback) {
        CurrentWeatherCurrentLocationCallback locationCallback =
                new CurrentWeatherCurrentLocationCallback(weatherCurrentDataLoader,
                        weatherCurrentCallback, dataMapper);
        locationHelper.getCurrentLocation(locationCallback);
    }
}
