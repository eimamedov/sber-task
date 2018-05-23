
package eim.yar.sbertask.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("PMD.DataClass")
public class Main {

    @SerializedName("temp")
    @Expose
    private Double temperature;
    @SerializedName("pressure")
    @Expose
    private Double pressure;
    @SerializedName("humidity")
    @Expose
    private Double humidity;
    @SerializedName("temp_min")
    @Expose
    private Double temperatureMin;
    @SerializedName("temp_max")
    @Expose
    private Double temperatureMax;
    @SerializedName("sea_level")
    @Expose
    private Double seaLevel;
    @SerializedName("grnd_level")
    @Expose
    private Double grndLevel;

    public Main(Double temperature, Double pressure, Double humidity, Double temperatureMin,
                Double temperatureMax) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double tempMin) {
        this.temperatureMin = tempMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double tempMax) {
        this.temperatureMax = tempMax;
    }

    public Double getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public Double getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(Double grndLevel) {
        this.grndLevel = grndLevel;
    }

}
