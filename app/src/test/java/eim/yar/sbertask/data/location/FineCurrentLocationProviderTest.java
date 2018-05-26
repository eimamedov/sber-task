package eim.yar.sbertask.data.location;

import android.Manifest;
import android.location.Location;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class FineCurrentLocationProviderTest {

    private FineCurrentLocationProvider locationProvider;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        ShadowApplication.getInstance().grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        locationProvider = new FineCurrentLocationProvider(RuntimeEnvironment.application);
    }

    @Test
    public void testFineCurrentLocationProviderHappyCase() {
        locationProvider.findCurrentLocation(new CurrentLocationProvider.CurrentLocationCallback() {
            @Override
            public void onCurrentLocationFound(Location currentLocation) {
            }

            @Override
            public void onError(Exception exception) {
            }
        });
    }

    @Test
    public void testFineCurrentLocationProviderNoPermission() {
        ShadowApplication.getInstance().denyPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        locationProvider.findCurrentLocation(new CurrentLocationProvider.CurrentLocationCallback() {
            @Override
            public void onCurrentLocationFound(Location currentLocation) {
            }

            @Override
            public void onError(Exception exception) {
                assertThat(exception.getMessage()).isEqualToIgnoringCase(
                        "Fine location permission is not granted");
            }
        });
    }

    @Test
    public void testFineCurrentLocationProviderNullContext() {
        expectedException.expect(IllegalArgumentException.class);
        new FineCurrentLocationProvider(null);
    }

    @Test
    public void testFineCurrentLocationProviderNullCallback() {
        expectedException.expect(IllegalArgumentException.class);
        locationProvider.findCurrentLocation(null);
    }

}