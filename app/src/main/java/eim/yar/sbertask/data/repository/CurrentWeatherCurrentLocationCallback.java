package eim.yar.sbertask.data.repository;

import android.location.Address;
import android.location.Location;

import java.util.List;

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
     * Loaded current weather data.
     */
    WeatherCurrent weatherCurrent;

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
                        weatherCurrent = dataMapper.transform(weatherEntity);
                        if (weatherCurrent == null) {
                            methodCallback.onError(
                                    new WeatherNotFoundException("Weather data not found"));
                        } else {
                            Location weatherEntityLocation = new Location("Weather");
                            weatherEntityLocation.setLatitude(weatherCurrent.getLatitude());
                            weatherEntityLocation.setLongitude(weatherCurrent.getLongitude());
                            locationHelper.reverseGeocode(weatherEntityLocation,
                                    reverseGeocodeCallback);
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

    /**
     * Reverse geocode callback used to get address string for current weather data.
     */
    final LocationHelper.ReverseGeocodeCallback reverseGeocodeCallback =
            new LocationHelper.ReverseGeocodeCallback() {

                /**
                 * Build address string from a {@link Address} object.
                 * @param address a {@link Address} object to build string from
                 * @return address string
                 */
                private String buildAddressString(Address address) {
                    StringBuilder addressString = new StringBuilder();
                    if (address.getCountryName() != null) {
                        addressString.append(address.getCountryName());
                    }
                    if (address.getAdminArea() != null) {
                        addressString.append((addressString.length() > 0 ? ", " : "")
                                + address.getAdminArea());
                    }
                    if (address.getSubAdminArea() != null) {
                        addressString.append((addressString.length() > 0 ? ", " : "")
                                + address.getSubAdminArea());
                    }
                    if (address.getLocality() != null) {
                        addressString.append((addressString.length() > 0 ? ", " : "")
                                + address.getLocality());
                    }
                    return addressString.toString();
                }

                @Override
                public void onReverseGeocodeFound(List<Address> addresses) {
                    Address address = addresses.get(0);
                    String addressString = buildAddressString(address);
                    if (addressString.length() > 0) {
                        weatherCurrent.setAddress(addressString);
                    }
                    methodCallback.onCurrentWeatherLoaded(weatherCurrent);
                }

                @Override
                public void onError(Exception exception) {
                    methodCallback.onCurrentWeatherLoaded(weatherCurrent);
                }
            };
}
