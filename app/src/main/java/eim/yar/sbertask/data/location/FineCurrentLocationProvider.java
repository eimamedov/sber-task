package eim.yar.sbertask.data.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import eim.yar.sbertask.data.exception.LocationDeterminationException;

/**
 * {@link CurrentLocationProvider} implementation based on fine LocationProviders.
 */
public class FineCurrentLocationProvider implements CurrentLocationProvider {

    /**
     * Location request expiration duration in milliseconds.
     */
    private static final int LOCATION_REQUEST_DURATION = 10000;

    /**
     * Criteria for selecting a location provider.
     */
    private Criteria criteria;

    /**
     * Application context.
     */
    private final Context context;

    /**
     * Object providing access to the system location services.
     */
    final LocationManager locationManager;

    /**
     * Listener to handle location requests.
     */
    private LocationListener createLocationListener(final Handler handler,
            final CurrentLocationCallback currentLocationCallback) {
        return new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                handler.removeCallbacksAndMessages(null);
                if (currentLocationCallback != null) {
                    if (location == null) {
                        currentLocationCallback.onError(new LocationDeterminationException(
                                "Current location is not found"));
                    } else {
                        currentLocationCallback.onCurrentLocationFound(location);
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // empty
            }

            @Override
            public void onProviderEnabled(String provider) {
                // empty
            }

            @Override
            public void onProviderDisabled(String provider) {
                // empty
            }
        };
    }

    /**
     * Runnable object to emit location error when request timeout duration is finished.
     */
    private Runnable createRequestTimeoutFinishedRunnable(final LocationListener locationListener,
            final CurrentLocationCallback currentLocationCallback) {
        return () -> {
            locationManager.removeUpdates(locationListener);
            if (currentLocationCallback != null) {
                currentLocationCallback.onError(new LocationDeterminationException(
                        "Location request timeout is finished"));
            }
        };
    }

    /**
     * Construct a {@link FineCurrentLocationProvider}.
     * @param context application context
     */
    public FineCurrentLocationProvider(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!");
        }
        this.context = context.getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        initCriteria();
    }

    /**
     * Initialize criteria to get file location providers.
     */
    private void initCriteria() {
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
    }

    @Override
    public void findCurrentLocation(CurrentLocationCallback currentLocationCallback) {
        if (currentLocationCallback == null) {
            throw new IllegalArgumentException("Callback cannot be null!");
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            final Handler handler = new Handler();
            final LocationListener locationListener = createLocationListener(handler,
                    currentLocationCallback);
            handler.postDelayed(createRequestTimeoutFinishedRunnable(locationListener,
                    currentLocationCallback), LOCATION_REQUEST_DURATION);
            locationManager.requestSingleUpdate(criteria, locationListener, null);
        } else {
            currentLocationCallback.onError(new LocationDeterminationException(
                    "Fine location permission is not granted"));
        }
    }
}
