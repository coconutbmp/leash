package com.coconutbmp.leash;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class InternetRequestTests {
    MockWebServer mockWebServer;
    @Before
    public void setUp() throws IOException{
        mockWebServer = new MockWebServer();
        MockResponse mockSuccessGet = new MockResponse();
        mockSuccessGet.setResponseCode(HttpURLConnection.HTTP_OK);
        HttpUrl url = mockWebServer.url("/html/");
        mockWebServer.start();
    }

    @After
    public void shutDown() throws IOException {
        mockWebServer.shutdown();
    }
}
