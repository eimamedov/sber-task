package eim.yar.sbertask.data.entity.mapper;

import com.google.gson.JsonSyntaxException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import eim.yar.sbertask.data.entity.WeatherEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherEntityJsonMapperTest {

    private static final String JSON_RESPONSE_WEATHER_DATA = "{\"coord\":\n" +
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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private WeatherEntityJsonMapper weatherEntityJsonMapper;

    @Before
    public void setUp() {
        weatherEntityJsonMapper = new WeatherEntityJsonMapper();
    }

    @Test
    public void testTransformWeatherEntityHappyCase() {
        WeatherEntity weatherEntity = weatherEntityJsonMapper
                .transformWeatherEntity(JSON_RESPONSE_WEATHER_DATA);
        assertThat(weatherEntity.getCod()).isEqualTo(200);
        assertThat(weatherEntity.getId()).isEqualTo(2172797);
        assertThat(weatherEntity.getMain().getTemp()).isEqualTo(23.32);
    }

    @Test
    public void testTransformWeatherEntityNotValidResponse() {
        expectedException.expect(JsonSyntaxException.class);
        weatherEntityJsonMapper.transformWeatherEntity("uncorrected");
    }
}
