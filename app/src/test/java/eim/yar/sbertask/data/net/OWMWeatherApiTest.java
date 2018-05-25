package eim.yar.sbertask.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowNetworkInfo;

import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.entity.mapper.WeatherEntityJsonMapper;
import eim.yar.sbertask.data.exception.NetworkConnectionException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class OWMWeatherApiTest {

    private static final double TEST_LATITUDE = 1;

    private static final double TEST_LONGITUDE = 2;

    private static final String TEST_RESPONSE_BODY = "{\"coord\":\n" +
            "{\"lon\":145.77,\"lat\":-16.92},\n" +
            "\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":" +
            "\"broken clouds\",\"icon\":\"04n\"}],\n" +
            "\"base\":\"cmc stations\",\n" +
            "\"main\":{\"temp\":23.32,\"pressure\":1019,\"humidity\":83," +
            "\"temp_min\":289.82,\"temp_max\":295.37},\n" +
            "\"wind\":{\"speed\":5.1,\"deg\":150},\n" +
            "\"clouds\":{\"all\":75},\n" +
            "\"rain\":{\"3h\":3},\n" +
            "\"dt\":1435658272,\n" +
            "\"sys\":{\"type\":1,\"id\":8166,\"message\":0.0166,\"country\":" +
            "\"AU\",\"sunrise\":1435610796,\"sunset\":1435650870},\n" +
            "\"id\":2172797,\n" +
            "\"name\":\"Cairns\",\n" +
            "\"cod\":200}";

    private MockWebServer server;

    private HttpUrl baseUrl;

    private ConnectivityManager connectivityManager;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        connectivityManager = (ConnectivityManager) RuntimeEnvironment
                .application.getSystemService(Context.CONNECTIVITY_SERVICE);
        server = new MockWebServer();
        server.start();
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/" + String.format(OWMWeatherApi.API_BY_COORDINATES,
                        TEST_LATITUDE, TEST_LONGITUDE))) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(TEST_RESPONSE_BODY);
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);
        baseUrl = server.url("/");
    }

    @Test
    public void testOWMWeatherApiHappyCase() {
        OWMWeatherApi weatherApi = new OWMWeatherApi(ShadowApplication.getInstance()
                .getApplicationContext(), new WeatherEntityJsonMapper(), baseUrl.toString());
        weatherApi.getCurrentWeatherByCoordinates(TEST_LATITUDE, TEST_LONGITUDE,
                new WeahterApi.WeatherCurrentCallback() {
            @Override
            public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                assertThat(weatherEntity.getCod()).isEqualTo(200);
                assertThat(weatherEntity.getMain().getTemperature()).isEqualTo(23.32);
                assertThat(weatherEntity.getMain().getPressure()).isEqualTo(1019);
            }

            @Override
            public void onError(Exception exception) {
            }
        });
    }

    @Test
    public void testOWMWeatherApiNullMapper() {
        expectedException.expect(IllegalArgumentException.class);
        new OWMWeatherApi(ShadowApplication.getInstance().getApplicationContext(), null);
    }

    @Test
    public void testOWMWeatherApiNullContext() {
        expectedException.expect(IllegalArgumentException.class);
        new OWMWeatherApi(null, new WeatherEntityJsonMapper());
    }

    @Test
    public void testOWMWeatherApiNullCallback() {
        expectedException.expect(IllegalArgumentException.class);
        OWMWeatherApi weatherApi = new OWMWeatherApi(ShadowApplication.getInstance()
                .getApplicationContext(), new WeatherEntityJsonMapper());
        weatherApi.getCurrentWeatherByCoordinates(TEST_LATITUDE, TEST_LONGITUDE, null);
    }

    @Test
    public void testOWMWeatherApiDisconnectedState() {
        ShadowNetworkInfo shadowOfActiveNetworkInfo =
                shadowOf(connectivityManager.getActiveNetworkInfo());
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED);
        OWMWeatherApi weatherApi = new OWMWeatherApi(ShadowApplication.getInstance()
                .getApplicationContext(), new WeatherEntityJsonMapper(), baseUrl.toString());
        weatherApi.getCurrentWeatherByCoordinates(TEST_LATITUDE, TEST_LONGITUDE,
                new WeahterApi.WeatherCurrentCallback() {
                    @Override
                    public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                    }

                    @Override
                    public void onError(Exception exception) {
                        assertThat(exception.getMessage()).isEqualToIgnoringCase(
                                "There is no internet connection");
                    }
                });
    }

    @Test
    public void testOWMWeatherApiWrongUrl() {
        OWMWeatherApi weatherApi = new OWMWeatherApi(ShadowApplication.getInstance()
                .getApplicationContext(), new WeatherEntityJsonMapper(), "/");
        weatherApi.getCurrentWeatherByCoordinates(TEST_LATITUDE, TEST_LONGITUDE,
                new WeahterApi.WeatherCurrentCallback() {
                    @Override
                    public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                    }

                    @Override
                    public void onError(Exception exception) {
                        assertThat(exception).isInstanceOf(NetworkConnectionException.class);
                    }
                });
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}