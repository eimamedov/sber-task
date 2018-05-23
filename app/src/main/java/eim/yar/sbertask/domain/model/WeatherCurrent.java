package eim.yar.sbertask.domain.model;

/**
 * Class that represents a current weather data in the domain layer.
 */
@SuppressWarnings("PMD.DataClass")
public class WeatherCurrent {

    private double temperature;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
