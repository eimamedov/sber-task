package eim.yar.sbertask.data.location;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLocationManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class FineCurrentLocationProviderTest {

    private FineCurrentLocationProvider locationProvider;

    private ShadowLocationManager shadowLocationManager;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        ShadowApplication.getInstance().grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        locationProvider = new FineCurrentLocationProvider(RuntimeEnvironment.application);
        LocationManager locationManager = (LocationManager) RuntimeEnvironment.application
                .getSystemService(Context.LOCATION_SERVICE);
        shadowLocationManager = Shadows.shadowOf(locationManager);
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

    @Test
    public void testLastKnownLocation() {
        List<Criteria> criteriaList = new ArrayList();
        criteriaList.add(locationProvider.getCriteria());
        try {
            shadowLocationManager.setBestProvider(LocationManager.GPS_PROVIDER, true, criteriaList);
            final Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(1);
            location.setLongitude(1);
            location.setTime(System.currentTimeMillis());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }
            shadowLocationManager.setLastKnownLocation(LocationManager.GPS_PROVIDER, location);
            locationProvider.findCurrentLocation(new CurrentLocationProvider.CurrentLocationCallback() {
                @Override
                public void onCurrentLocationFound(Location currentLocation) {
                    assertThat(currentLocation.getLatitude()).isEqualTo(location.getLatitude());
                    assertThat(currentLocation.getLongitude()).isEqualTo(location.getLongitude());
                    assertThat(currentLocation.getTime()).isEqualTo(location.getTime());
                }

                @Override
                public void onError(Exception exception) {
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}