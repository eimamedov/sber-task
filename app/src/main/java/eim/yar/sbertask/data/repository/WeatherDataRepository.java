package eim.yar.sbertask.data.repository;

import android.location.Location;
import android.support.annotation.NonNull;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityDataMapper;
import eim.yar.sbertask.data.exception.WeatherNotFoundException;
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
     * Object to transform a {@link eim.yar.sbertask.data.entity.WeatherEntity} objects
     * into an {@link eim.yar.sbertask.domain.model.WeatherCurrent} objects.
     */
    final WeatherEntityDataMapper dataMapper;

    /**
     * Callback used to be notified when either a current weather data for current location
     * has been loaded or an error happened.
     */
    WeatherCurrentCallback weatherCurrentForCurrentLocationCallback;

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
        weatherCurrentForCurrentLocationCallback = weatherCurrentCallback;
        locationHelper.getCurrentLocation(currentWeatherCurrentLocationCallback);
    }

    /**
     * Current location callback used to get current weather data for current location.
     */
    final LocationHelper.CurrentLocationCallback currentWeatherCurrentLocationCallback =
            new LocationHelper.CurrentLocationCallback() {
                @Override
                public void onCurrentLocationFound(Location currentLocation) {
                    weatherCurrentDataLoader.loadCurrentWeatherByCoordinates(
                            currentLocation.getLatitude(), currentLocation.getLongitude(),
                            currentWeatherCurrentLocationDataLoaderCallback);
                }

                @Override
                public void onError(Exception exception) {
                    if (weatherCurrentForCurrentLocationCallback != null) {
                        weatherCurrentForCurrentLocationCallback.onError(exception);
                        weatherCurrentForCurrentLocationCallback = null;
                    }
                }
            };

    /**
     * Current weather callback used to get current weather data for current location.
     */
    final WeatherCurrentDataLoader.WeatherCurrentCallback
            currentWeatherCurrentLocationDataLoaderCallback =
            new WeatherCurrentDataLoader.WeatherCurrentCallback() {
                @Override
                public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                    if (weatherCurrentForCurrentLocationCallback != null) {
                        WeatherCurrent weatherCurrent = dataMapper.transform(weatherEntity);
                        if (weatherCurrent == null) {
                            weatherCurrentForCurrentLocationCallback.onError(
                                    new WeatherNotFoundException("Weather data not found"));
                        } else {
                            weatherCurrentForCurrentLocationCallback
                                    .onCurrentWeatherLoaded(weatherCurrent);
                        }
                        weatherCurrentForCurrentLocationCallback = null;
                    }
                }

                @Override
                public void onError(Exception exception) {
                    if (weatherCurrentForCurrentLocationCallback != null) {
                        weatherCurrentForCurrentLocationCallback.onError(exception);
                        weatherCurrentForCurrentLocationCallback = null;
                    }
                }
            };
}
