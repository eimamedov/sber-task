package eim.yar.sbertask.data.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

/**
 * Api connection class used to retrieve data from the web.
 */
public final class ApiConnection implements Callable<String> {

    /**
     * Default read timeout for the connection in milliseconds.
     */
    private static final int READ_TIMEOUT = 10000;

    /**
     * Default connect timeout for the connection in milliseconds.
     */
    private static final int CONNECT_TIMEOUT = 15000;

    /**
     * The default keyword by which the api connection request is known.
     */
    private static final String CONTENT_TYPE_LABEL = "Content-Type";

    /**
     * The default value associated with the api connection request.
     */
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    /**
     * The GET HTTP method for the URL request.
     */
    public static final String REQUEST_METHOD_GET = "GET";

    /**
     * URL value for the api connection request.
     */
    private final URL url;

    /**
     * The HTTP method for the api connection request.
     */
    private final String requestVerb;

    /**
     * Content from an HTTP response.
     */
    private String response = "";

    /**
     * Construct a {@link ApiConnection}.
     * @param url URL to remote source
     * @param requestVerb the HTTP method
     * @throws MalformedURLException then malformed URL has occurred
     */
    private ApiConnection(String url, String requestVerb) throws MalformedURLException {
        this.url = new URL(url);
        this.requestVerb = requestVerb;
    }

    /**
     * Create a {@link ApiConnection} with GET HTTP method by default.
     * @param url URL to remote source
     * @return {@link ApiConnection} object
     * @throws MalformedURLException then malformed URL has occurred
     */
    public static ApiConnection createGET(String url) throws MalformedURLException {
        return new ApiConnection(url, REQUEST_METHOD_GET);
    }

    /**
     * Synchronous request to the api.
     * @return A response string
     * @throws IOException If an I/O error occurs
     */
    public String requestSyncCall() throws IOException {
        connectToApi();
        return response;
    }

    /**
     * Connect to the api. Get response from specified url.
     * @throws IOException If an I/O error occurs
     */
    private void connectToApi() throws IOException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            setupConnection(urlConnection);
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = readStringFromInputStream(urlConnection.getInputStream());
            } else {
                response = readStringFromInputStream(urlConnection.getErrorStream());
            }
        } catch (IOException exception) {
            throw exception;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * Read from an {@link InputStream}.
     * @param inputStream {@link InputStream} to read from
     * @return content of the inputStream
     * @throws IOException If an I/O error occurs
     */
    private String readStringFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                Charset.forName("UTF-8")));
        StringBuilder stringBuilderResult = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilderResult.append(line);
        }
        return stringBuilderResult.toString();
    }

    /**
     * Setup http url connection.
     * @param connection connection to setup
     * @throws IOException If an I/O error occurs then trying to set request method
     */
    private void setupConnection(HttpURLConnection connection) throws IOException {
        if (connection != null) {
            connection.setRequestMethod(requestVerb);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setDoInput(true);
            connection.setRequestProperty(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON);
        }
    }

    @Override public String call() throws IOException {
        return requestSyncCall();
    }
}
