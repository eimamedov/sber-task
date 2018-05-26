package eim.yar.sbertask.data.repository.dataloader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eim.yar.sbertask.data.entity.Main;
import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.data.exception.NetworkConnectionException;
import eim.yar.sbertask.data.net.OWMWeatherApi;
import eim.yar.sbertask.data.net.WeahterApi;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OWMWeatherCurrentDataLoaderTest {

    private static final String EXCEPTION_TEST_MESSAGE = "Test message";

    @Mock private OWMWeatherApi weahterApi;

    private OWMWeatherCurrentDataLoader weatherCurrentDataLoader;

    private WeatherEntity testWeatherEntity;

    @Before
    public void setUp() {
        weatherCurrentDataLoader = new OWMWeatherCurrentDataLoader(weahterApi);
        testWeatherEntity = new WeatherEntity();
        testWeatherEntity.setId(10);
        testWeatherEntity.setCod(200);
        testWeatherEntity.setMain(new Main(15.5, 1001d, 82d, 0d, 43.3));
    }

    @Test
    public void testOWMWeatherCurrentDataLoaderHappyCase() {
        doAnswer(invocation -> {
            ((WeahterApi.WeatherCurrentCallback) invocation.getArguments()[2])
                    .onWeatherEntityLoaded(testWeatherEntity);
            return null;
        }).when(weahterApi).getCurrentWeatherByCoordinates(any(Double.class), any(Double.class),
                any(WeahterApi.WeatherCurrentCallback.class));
        weatherCurrentDataLoader.loadCurrentWeatherByCoordinates(1, 2,
                new WeatherCurrentDataLoader.WeatherCurrentCallback() {
                    @Override
                    public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                        assertThat(weatherEntity.getCod()).isEqualTo(200);
                        assertThat(weatherEntity.getId()).isEqualTo(10);
                        assertThat(weatherEntity.getMain().getTemperature()).isEqualTo(15.5);
                        assertThat(weatherEntity.getMain().getPressure()).isEqualTo(1001);
                    }

                    @Override
                    public void onError(Exception exception) {
                    }
                });
    }

    @Test
    public void testOWMWeatherCurrentDataLoaderError() {
        doAnswer(invocation -> {
            ((WeahterApi.WeatherCurrentCallback) invocation.getArguments()[2])
                    .onError(new NetworkConnectionException(EXCEPTION_TEST_MESSAGE));
            return null;
        }).when(weahterApi).getCurrentWeatherByCoordinates(any(Double.class), any(Double.class),
                any(WeahterApi.WeatherCurrentCallback.class));
        weatherCurrentDataLoader.loadCurrentWeatherByCoordinates(1, 2,
                new WeatherCurrentDataLoader.WeatherCurrentCallback() {
                    @Override
                    public void onWeatherEntityLoaded(WeatherEntity weatherEntity) {
                    }

                    @Override
                    public void onError(Exception exception) {
                        assertThat(exception).isInstanceOf(NetworkConnectionException.class);
                        assertThat(exception.getMessage())
                                .isEqualToIgnoringCase(EXCEPTION_TEST_MESSAGE);
                    }
                });
    }
}