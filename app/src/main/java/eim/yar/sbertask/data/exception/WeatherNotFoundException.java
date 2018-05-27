package eim.yar.sbertask.data.exception;

/**
 * Exception throw when weather data is not found.
 */
public class WeatherNotFoundException extends Exception {

    public WeatherNotFoundException() {
    super();
  }

    public WeatherNotFoundException(final String message) {
    super(message);
  }

    public WeatherNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WeatherNotFoundException(final Throwable cause) {
    super(cause);
  }
}
