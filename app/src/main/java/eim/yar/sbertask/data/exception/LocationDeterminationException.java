package eim.yar.sbertask.data.exception;

/**
 * Exception throw when there is an error during location determination.
 */
public class LocationDeterminationException extends Exception {

    public LocationDeterminationException() {
    super();
  }

    public LocationDeterminationException(final String message) {
    super(message);
  }

    public LocationDeterminationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LocationDeterminationException(final Throwable cause) {
    super(cause);
  }
}
