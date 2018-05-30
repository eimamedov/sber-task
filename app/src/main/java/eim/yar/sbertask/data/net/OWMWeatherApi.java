package eim.yar.sbertask.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityJsonMapper;
import eim.yar.sbertask.data.exception.NetworkConnectionException;

/**
 * {@link WeatherApi} implementation for retrieving data from the network
 * base on OpenWeatherMap api.
 */
public class OWMWeatherApi implements WeatherApi {

    /**
     * OpenWeatherMap api key.
     */
    public static final String APPID = "a71487c608d058610e2dcff5e2b43bae";

    /**
     * Base url to OpenWeatherMap api.
     */
    public static final String OWM_API_URL = "api.openweathermap.org/data/2.5/weather" +
            "?units=metric&APPID=" + APPID;

    /**
     * Api url to get current weather data by coordinates.
     */
    public static final String API_BY_COORDINATES = OWM_API_URL + "&lat=%1$f&lon=%2$f";

    /**
     * Base url string.
     */
    private final String baseUrl;

    /**
     * Application context.
     */
    private final Context context;

    /**
     * Object to transform weather data json strings to {@link WeatherEntity}.
     */
    private final WeatherEntityJsonMapper weatherEntityJsonMapper;

    /**
     * Construct a {@link OWMWeatherApi}.
     * @param context application context
     * @param weatherEntityJsonMapper object to transform weather data
     * json strings to {@link WeatherEntity} objects.
     */
    public OWMWeatherApi(Context context, WeatherEntityJsonMapper weatherEntityJsonMapper) {
        this(context, weatherEntityJsonMapper, "http://");
    }

    /**
     * Construct a {@link OWMWeatherApi}.
     * @param context application context
     * @param weatherEntityJsonMapper object to transform weather data
     * json strings to {@link WeatherEntity} objects.
     * @param baseUrl base url
     */
    public OWMWeatherApi(Context context, WeatherEntityJsonMapper weatherEntityJsonMapper,
                         String baseUrl) {
        if (context == null || weatherEntityJsonMapper == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!");
        }
        this.context = context.getApplicationContext();
        this.weatherEntityJsonMapper = weatherEntityJsonMapper;
        this.baseUrl = baseUrl;
    }

    @Override
    public void getCurrentWeatherByCoordinates(double latitude, double longitude,
                                               WeatherCurrentCallback weatherCallback) {
        if (weatherCallback == null) {
            throw new IllegalArgumentException("Callback cannot be null!");
        }
        if (isThereInternetConnection()) {
            try {
                String apiUrl = String.format(baseUrl + API_BY_COORDINATES, latitude, longitude);
                ApiConnection getCurrentWeatherConnection = ApiConnection.createGET(apiUrl);
                String response = getCurrentWeatherConnection.requestSyncCall();
                WeatherEntity weatherEntity = weatherEntityJsonMapper
                        .transformWeatherEntity(response);
                weatherCallback.onWeatherEntityLoaded(weatherEntity);
            } catch (IOException exception) {
                weatherCallback.onError(new NetworkConnectionException(exception.getCause()));
            }
        } else {
            weatherCallback.onError(new NetworkConnectionException(
                    "There is no internet connection"));
        }
    }

    /**
     * Checks if the device has any active internet connection.
     * @return true if the device is with internet connection, otherwise false.
     */
    private boolean isThereInternetConnection() {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        return isConnected;
    }
}
