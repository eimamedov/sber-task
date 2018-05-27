package eim.yar.sbertask.data.entity.mapper;


import org.junit.Before;
import org.junit.Test;

import eim.yar.sbertask.data.entity.Coord;
import eim.yar.sbertask.data.entity.Main;
import eim.yar.sbertask.data.entity.WeatherEntity;
import eim.yar.sbertask.domain.model.WeatherCurrent;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherEntityDataMapperTest {

    private static final double TEST_TEMPERATURE = 20.5;

    private static final double TEST_LATITUDE = 1.5;

    private static final double TEST_LONGITUDE = 2.5;

    private static final String TEST_NAME = "TEST_NAME";

    private WeatherEntityDataMapper weatherEntityDataMapper;

    private WeatherEntity weatherEntity;

    @Before
    public void setUp() {
        weatherEntityDataMapper = new WeatherEntityDataMapper();
        weatherEntity = new WeatherEntity();
        weatherEntity.setMain(new Main(TEST_TEMPERATURE, 0d, 0d, 0d, 0d));
        weatherEntity.setCoord(new Coord(TEST_LATITUDE, TEST_LONGITUDE));
        weatherEntity.setName(TEST_NAME);
    }

    @Test
    public void testTransformWeatherEntityHappyCase() {
        WeatherCurrent weatherCurrent = weatherEntityDataMapper
                .transform(weatherEntity);
        assertThat(weatherCurrent.getTemperature()).isEqualTo(TEST_TEMPERATURE);
        assertThat(weatherCurrent.getLatitude()).isEqualTo(TEST_LATITUDE);
        assertThat(weatherCurrent.getLongitude()).isEqualTo(TEST_LONGITUDE);
        assertThat(weatherCurrent.getAddress()).isEqualTo(TEST_NAME);
    }

    @Test
    public void testTransformNullWeatherEntity() {
        WeatherCurrent weatherCurrent = weatherEntityDataMapper
                .transform(null);
        assertThat(weatherCurrent).isNull();
    }

}