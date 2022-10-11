package com.coconutbmp.leash;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class EditProfileDialogTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        this.server = new MockWebServer();
        this.server.start();
    }

    @After
    public void tearDown() throws IOException {
        this.server.shutdown();
    }

    @Test
    public void Test_SUCCESS() throws Exception {
        String json = "Success";
        HttpUrl baseUrl = this.server.url("v1/test/");
        this.server.enqueue(new MockResponse().setResponseCode(200).setBody(json));
        Request request = new Request.Builder().url(baseUrl).build();
        OkHttpClient client = new OkHttpClient();
        String r = client.newCall(request).execute().body().string();
        assertEquals("Success", r);
    }
}
