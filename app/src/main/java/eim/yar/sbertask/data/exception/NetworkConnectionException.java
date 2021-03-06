package eim.yar.sbertask.data.exception;

/**
 * Exception throw when there is a network connection error.
 */
public class NetworkConnectionException extends Exception {

    public NetworkConnectionException() {
    super();
  }

    public NetworkConnectionException(final String message) {
    super(message);
  }

    public NetworkConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetworkConnectionException(final Throwable cause) {
    super(cause);
  }
}
