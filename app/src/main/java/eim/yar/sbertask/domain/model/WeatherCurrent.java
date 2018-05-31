package eim.yar.sbertask.domain.model;

/**
 * Class that represents a current weather data in the domain layer.
 */
@SuppressWarnings("PMD.DataClass")
public class WeatherCurrent {

    private Double temperature;

    private Double latitude;

    private Double longitude;

    private String address;

    private Long timestamp;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
