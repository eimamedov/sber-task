package eim.yar.sbertask.data.repository.locationhelper;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eim.yar.sbertask.data.exception.LocationDeterminationException;
import eim.yar.sbertask.data.location.CurrentLocationProvider;
import eim.yar.sbertask.data.location.FineCurrentLocationProvider;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(RobolectricTestRunner.class)
public class FineLocationHelperTest {

    private static final String TEST_COUNTRY_NAME = "Test country";

    private static final String EXCEPTION_TEST_MESSAGE = "Test message";

    private static final double TEST_LATITUDE = 1;

    private static final double TEST_LONGITUDE = 2;

    @Mock private FineCurrentLocationProvider currentLocationProvider;

    @Mock private Geocoder geocoder;

    private FineLocationHelper locationHelper;

    private Location testLocation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        locationHelper = new FineLocationHelper(currentLocationProvider, geocoder);
        testLocation = new Location("test");
        testLocation.setLatitude(TEST_LATITUDE);
        testLocation.setLongitude(TEST_LONGITUDE);
    }

    @Test
    public void testFineLocationHelperHappyCase() {
        doAnswer(invocation -> {
            ((CurrentLocationProvider.CurrentLocationCallback) invocation.getArguments()[0])
                    .onCurrentLocationFound(testLocation);
            return null;
        }).when(currentLocationProvider).findCurrentLocation(
                any(CurrentLocationProvider.CurrentLocationCallback.class));
        locationHelper.getCurrentLocation(
                new LocationHelper.CurrentLocationCallback() {
                    @Override
                    public void onCurrentLocationFound(Location currentLocation) {
                        assertThat(currentLocation.getLatitude()).isEqualTo(TEST_LATITUDE);
                        assertThat(currentLocation.getLongitude()).isEqualTo(TEST_LONGITUDE);
                    }

                    @Override
                    public void onError(Exception exception) {
                    }
                });
    }

    @Test
    public void testFineLocationHelperError() {
        doAnswer(invocation -> {
            ((CurrentLocationProvider.CurrentLocationCallback) invocation.getArguments()[0])
                    .onError(new LocationDeterminationException(EXCEPTION_TEST_MESSAGE));
            return null;
        }).when(currentLocationProvider).findCurrentLocation(
                any(CurrentLocationProvider.CurrentLocationCallback.class));
        locationHelper.getCurrentLocation(
                new LocationHelper.CurrentLocationCallback() {
                    @Override
                    public void onCurrentLocationFound(Location currentLocation) {
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
    public void testReverseGeocodeHappyCase() {
        Address address = new Address(Locale.getDefault());
        address.setCountryName(TEST_COUNTRY_NAME);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        try {
            doReturn(addresses).when(geocoder).getFromLocation(any(Double.class), any(Double.class),
                    any(Integer.class));
            locationHelper.reverseGeocode(new Location("Test"), new LocationHelper.ReverseGeocodeCallback() {
                @Override
                public void onReverseGeocodeFound(List<Address> addresses) {
                    assertThat(addresses.size()).isEqualTo(1);
                    assertThat(addresses.get(0).getCountryName())
                            .isEqualToIgnoringCase(TEST_COUNTRY_NAME);
                }

                @Override
                public void onError(Exception exception) {
                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void testReverseGeocodeNullAddresses() {
        try {
            doReturn(null).when(geocoder).getFromLocation(any(Double.class), any(Double.class),
                    any(Integer.class));
            locationHelper.reverseGeocode(new Location("Test"), new LocationHelper.ReverseGeocodeCallback() {
                @Override
                public void onReverseGeocodeFound(List<Address> addresses) {
                }

                @Override
                public void onError(Exception exception) {
                    assertThat(exception).isInstanceOf(LocationDeterminationException.class);
                    assertThat(exception.getMessage())
                            .isEqualToIgnoringCase("Reverse geocode not found");
                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void testReverseGeocodeThrowException() {
        try {
            doThrow(new IOException()).when(geocoder).getFromLocation(any(Double.class), any(Double.class),
                    any(Integer.class));
            locationHelper.reverseGeocode(new Location("Test"), new LocationHelper.ReverseGeocodeCallback() {
                @Override
                public void onReverseGeocodeFound(List<Address> addresses) {
                }

                @Override
                public void onError(Exception exception) {
                    assertThat(exception).isInstanceOf(IOException.class);
                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}