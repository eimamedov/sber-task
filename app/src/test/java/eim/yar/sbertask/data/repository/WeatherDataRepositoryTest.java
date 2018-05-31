package eim.yar.sbertask.data.repository;

import android.location.Address;
import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eim.yar.sbertask.data.entity.Coord;
import eim.yar.sbertask.data.entity.Main;
import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityDataMapper;
import eim.yar.sbertask.data.exception.LocationDeterminationException;
import eim.yar.sbertask.data.exception.NetworkConnectionException;
import eim.yar.sbertask.data.exception.WeatherNotFoundException;
import eim.yar.sbertask.data.repository.dataloader.WeatherCurrentDataLoader;
import eim.yar.sbertask.data.repository.locationhelper.LocationHelper;
import eim.yar.sbertask.domain.model.WeatherCurrent;
import eim.yar.sbertask.domain.repository.WeatherRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@RunWith(RobolectricTestRunner.class)
public class WeatherDataRepositoryTest {

    private static final String EXCEPTION_TEST_MESSAGE = "Test message";

    private static final double TEST_TEMPERATURE = 20.5;

    private static final double TEST_LATITUDE = 1.5;

    private static final double TEST_LONGITUDE = 2.5;

    private static final String TEST_NAME = "TEST_NAME";

    private static final String TEST_COUNTRY_NAME = "Test country";

    private static final String TEST_ADMIN_AREA = "Test area";

    private static final String TEST_SUB_ADMIN_AREA = "Test subarea";

    private static final String TEST_LOCALITY = "Test locality";

    @Mock private WeatherCurrentDataLoader dataLoader;

    @Mock private LocationHelper locationHelper;

    private WeatherEntityDataMapper mapper;

    private WeatherDataRepository repository;

    private WeatherEntity weatherEntity;

    private Location testLocation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mapper = new WeatherEntityDataMapper();
        repository = new WeatherDataRepository(dataLoader, locationHelper, mapper);
        weatherEntity = new WeatherEntity();
        weatherEntity.setMain(new Main(TEST_TEMPERATURE, 0d, 0d, 0d, 0d));
        weatherEntity.setCoord(new Coord(TEST_LATITUDE, TEST_LONGITUDE));
        weatherEntity.setName(TEST_NAME);
        testLocation = new Location("test");
        testLocation.setLatitude(TEST_LATITUDE);
        testLocation.setLongitude(TEST_LONGITUDE);
    }

    @Test
    public void testGetCurrentWeatherForCurrentLocationHappyCase() {
        doAnswer(invocation -> {
            ((LocationHelper.CurrentLocationCallback) invocation.getArguments()[0])
                    .onCurrentLocationFound(testLocation);
            return null;
        }).when(locationHelper).getCurrentLocation(
                any(LocationHelper.CurrentLocationCallback.class));
        doAnswer(invocation -> {
            ((WeatherCurrentDataLoader.WeatherCurrentCallback) invocation.getArguments()[2])
                    .onWeatherEntityLoaded(weatherEntity);
            return null;
        }).when(dataLoader).loadCurrentWeatherByCoordinates(eq(TEST_LATITUDE), eq(TEST_LONGITUDE),
                any(WeatherCurrentDataLoader.WeatherCurrentCallback.class));
        Address address = new Address(Locale.getDefault());
        address.setCountryName(TEST_COUNTRY_NAME);
        address.setAdminArea(TEST_ADMIN_AREA);
        address.setSubAdminArea(TEST_SUB_ADMIN_AREA);
        address.setLocality(TEST_LOCALITY);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        doAnswer(invocation -> {
            ((LocationHelper.ReverseGeocodeCallback) invocation.getArguments()[1])
                    .onReverseGeocodeFound(addresses);
            return null;
        }).when(locationHelper).reverseGeocode(any(Location.class),
                any(LocationHelper.ReverseGeocodeCallback.class));
        repository.getCurrentWeatherForCurrentLocation(
                new WeatherRepository.WeatherCurrentCallback() {
                    @Override
                    public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                        assertThat(weatherCurrent.getTemperature()).isEqualTo(TEST_TEMPERATURE);
                        assertThat(weatherCurrent.getLatitude()).isEqualTo(TEST_LATITUDE);
                        assertThat(weatherCurrent.getLongitude()).isEqualTo(TEST_LONGITUDE);
                        assertThat(weatherCurrent.getAddress()).isEqualTo(TEST_COUNTRY_NAME + ", "
                                + TEST_ADMIN_AREA + ", " + TEST_SUB_ADMIN_AREA + ", "
                                + TEST_LOCALITY);
                    }

                    @Override
                    public void onError(Exception exception) {
                    }
                });
    }

    @Test
    public void testGetCurrentWeatherForCurrentLocationGeocoderError() {
        doAnswer(invocation -> {
            ((LocationHelper.CurrentLocationCallback) invocation.getArguments()[0])
                    .onCurrentLocationFound(testLocation);
            return null;
        }).when(locationHelper).getCurrentLocation(
                any(LocationHelper.CurrentLocationCallback.class));
        doAnswer(invocation -> {
            ((WeatherCurrentDataLoader.WeatherCurrentCallback) invocation.getArguments()[2])
                    .onWeatherEntityLoaded(weatherEntity);
            return null;
        }).when(dataLoader).loadCurrentWeatherByCoordinates(eq(TEST_LATITUDE), eq(TEST_LONGITUDE),
                any(WeatherCurrentDataLoader.WeatherCurrentCallback.class));
        doAnswer(invocation -> {
            ((LocationHelper.ReverseGeocodeCallback) invocation.getArguments()[1])
                    .onError(new LocationDeterminationException());
            return null;
        }).when(locationHelper).reverseGeocode(any(Location.class),
                any(LocationHelper.ReverseGeocodeCallback.class));
        repository.getCurrentWeatherForCurrentLocation(
                new WeatherRepository.WeatherCurrentCallback() {
                    @Override
                    public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                        assertThat(weatherCurrent.getTemperature()).isEqualTo(TEST_TEMPERATURE);
                        assertThat(weatherCurrent.getLatitude()).isEqualTo(TEST_LATITUDE);
                        assertThat(weatherCurrent.getLongitude()).isEqualTo(TEST_LONGITUDE);
                        assertThat(weatherCurrent.getAddress()).isEqualTo(TEST_NAME);
                    }

                    @Override
                    public void onError(Exception exception) {
                    }
                });
    }

    @Test
    public void testGetCurrentWeatherForCurrentLocationLocationDeterminationException() {
        doAnswer(invocation -> {
            ((LocationHelper.CurrentLocationCallback) invocation.getArguments()[0])
                    .onError(new LocationDeterminationException(EXCEPTION_TEST_MESSAGE));
            return null;
        }).when(locationHelper).getCurrentLocation(
                any(LocationHelper.CurrentLocationCallback.class));
        repository.getCurrentWeatherForCurrentLocation(
                new WeatherRepository.WeatherCurrentCallback() {
                    @Override
                    public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                    }

                    @Override
                    public void onError(Exception exception) {
                        assertThat(exception).isInstanceOf(LocationDeterminationException.class);
                        assertThat(exception.getMessage())
                                .isEqualToIgnoringCase(EXCEPTION_TEST_MESSAGE);
                    }
                });
    }

    @Test
    public void testGetCurrentWeatherForCurrentLocationNetworkConnectionException() {
        doAnswer(invocation -> {
            ((LocationHelper.CurrentLocationCallback) invocation.getArguments()[0])
                    .onCurrentLocationFound(testLocation);
            return null;
        }).when(locationHelper).getCurrentLocation(
                any(LocationHelper.CurrentLocationCallback.class));
        doAnswer(invocation -> {
            ((WeatherCurrentDataLoader.WeatherCurrentCallback) invocation.getArguments()[2])
                    .onError(new NetworkConnectionException(EXCEPTION_TEST_MESSAGE));
            return null;
        }).when(dataLoader).loadCurrentWeatherByCoordinates(eq(TEST_LATITUDE), eq(TEST_LONGITUDE),
                any(WeatherCurrentDataLoader.WeatherCurrentCallback.class));
        repository.getCurrentWeatherForCurrentLocation(
                new WeatherRepository.WeatherCurrentCallback() {
                    @Override
                    public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                    }

                    @Override
                    public void onError(Exception exception) {
                        assertThat(exception).isInstanceOf(NetworkConnectionException.class);
                        assertThat(exception.getMessage())
                                .isEqualToIgnoringCase(EXCEPTION_TEST_MESSAGE);
                    }
                });
    }

    @Test
    public void testGetCurrentWeatherForCurrentLocationWeatherNotFoundException() {
        doAnswer(invocation -> {
            ((LocationHelper.CurrentLocationCallback) invocation.getArguments()[0])
                    .onCurrentLocationFound(testLocation);
            return null;
        }).when(locationHelper).getCurrentLocation(
                any(LocationHelper.CurrentLocationCallback.class));
        doAnswer(invocation -> {
            ((WeatherCurrentDataLoader.WeatherCurrentCallback) invocation.getArguments()[2])
                    .onWeatherEntityLoaded(null);
            return null;
        }).when(dataLoader).loadCurrentWeatherByCoordinates(eq(TEST_LATITUDE), eq(TEST_LONGITUDE),
                any(WeatherCurrentDataLoader.WeatherCurrentCallback.class));
        repository.getCurrentWeatherForCurrentLocation(
                new WeatherRepository.WeatherCurrentCallback() {
                    @Override
                    public void onCurrentWeatherLoaded(WeatherCurrent weatherCurrent) {
                    }

                    @Override
                    public void onError(Exception exception) {
                        assertThat(exception).isInstanceOf(WeatherNotFoundException.class);
                    }
                });
    }
}