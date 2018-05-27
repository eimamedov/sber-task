package eim.yar.sbertask.data.repository.locationhelper;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import eim.yar.sbertask.data.exception.LocationDeterminationException;
import eim.yar.sbertask.data.location.CurrentLocationProvider;
import eim.yar.sbertask.data.location.FineCurrentLocationProvider;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class FineLocationHelperTest {

    private static final String EXCEPTION_TEST_MESSAGE = "Test message";

    private static final double TEST_LATITUDE = 1;

    private static final double TEST_LONGITUDE = 2;

    @Mock private FineCurrentLocationProvider currentLocationProvider;

    private FineLocationHelper locationHelper;

    private Location testLocation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        locationHelper = new FineLocationHelper(currentLocationProvider);
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
}