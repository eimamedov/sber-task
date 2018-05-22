package eim.yar.sbertask.data.net;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ApiConnectionTest {

    private static final String TEST_RESPONSE_BODY = "test";

    private static final String TEST_RESPONSE_ERROR = "error";

    private MockWebServer server;

    private HttpUrl baseUrl;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().equals("/")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(TEST_RESPONSE_BODY);
                } else if (request.getPath().equals("/error")) {
                    return new MockResponse().setResponseCode(401)
                            .setBody(TEST_RESPONSE_ERROR);
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);
        baseUrl = server.url("/");
    }

    @Test
    public void testApiConnectionCallHappyCase() throws IOException {
        ApiConnection apiConnection = ApiConnection.createGET(baseUrl.toString());
        String response = apiConnection.call();
        assertThat(response).isEqualToIgnoringCase(TEST_RESPONSE_BODY);
    }

    @Test
    public void testApiConnectionCallError() throws IOException {
        ApiConnection apiConnection = ApiConnection.createGET(baseUrl.toString() + "error");
        String response = apiConnection.call();
        assertThat(response).isEqualToIgnoringCase(TEST_RESPONSE_ERROR);
    }

    @Test
    public void testApiConnectionCallIncorrect() {
        try {
            ApiConnection apiConnection = ApiConnection.createGET("http://test");
            apiConnection.call();
        } catch (IOException exception) {
            assertThat(exception).isInstanceOf(UnknownHostException.class);
        }
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}