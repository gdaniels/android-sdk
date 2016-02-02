/*
 * Copyright 2016, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nestlabs.sdk;

import android.content.Intent;
import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "javax.net.ssl.*", "javax.*"})
public class NestAPIAndroidTest {

    // Needed to start PowerMock without using it with the @RunWith annotation.
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    String sOriginalUrl;

    @Before
    public void before() throws Exception {
        // Get internal variable before each test.
        sOriginalUrl = Whitebox.getInternalState(NestAPI.class, "sBaseAccessTokenUrl");
    }

    @After
    public void after() {
        // Reset internal variable before each test.
        Whitebox.setInternalState(NestAPI.class, "sBaseAccessTokenUrl", sOriginalUrl);
    }

    @Test
    public void testGetAccessTokenFromIntent_shouldReturnToken() {
        Intent intent = new Intent();
        NestToken token = new NestToken("token", 123);
        intent.putExtra("access_token_key", token);

        NestToken tokenFromIntent = NestAPI.getAccessTokenFromIntent(intent);
        assertNotNull(tokenFromIntent);
        assertEquals(token, tokenFromIntent);
    }

    @Test
    public void testRevokeToken_shouldMakeCorrectlyFormattedRequest() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean failureRef = new AtomicBoolean();

        NestToken token = new NestToken("test-token", 12345);
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("OK").setResponseCode(200));
        server.start();

        HttpUrl baseUrl = server.url("/");
        System.out.println(baseUrl.toString());

        String originalUrl = Whitebox.getInternalState(NestAPI.class, "sBaseAccessTokenUrl");
        assertEquals(originalUrl, "https://api.home.nest.com/");

        Whitebox.setInternalState(NestAPI.class, "sBaseAccessTokenUrl", baseUrl.toString());

        NestAPI.setAndroidContext(RuntimeEnvironment.application);
        NestAPI nest = NestAPI.getInstance();

        nest.revokeToken(token, new Callback() {
            @Override
            public void onSuccess() {
                latch.countDown();
            }

            @Override
            public void onFailure(NestException exception) {
                failureRef.set(true);
                latch.countDown();
            }
        });

        RecordedRequest request = server.takeRequest();
        assertEquals(request.getBody().readUtf8(), ""); // Request body should be empty.
        assertEquals(request.getMethod(), "DELETE");
        assertEquals(request.getPath(), "/oauth2/access_tokens/test-token");

        latch.await();
        assertFalse(failureRef.get());
    }

    @Test
    public void testRevokeToken_shouldHandleErrorResponsesCorrectly() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean failureRef = new AtomicBoolean();

        NestToken token = new NestToken("test-token", 12345);
        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("NOT OK").setResponseCode(451));
        server.start();

        HttpUrl baseUrl = server.url("/");
        System.out.println(baseUrl.toString());

        String originalUrl = Whitebox.getInternalState(NestAPI.class, "sBaseAccessTokenUrl");
        assertEquals(originalUrl, "https://api.home.nest.com/");

        Whitebox.setInternalState(NestAPI.class, "sBaseAccessTokenUrl", baseUrl.toString());

        NestAPI.setAndroidContext(RuntimeEnvironment.application);
        NestAPI nest = NestAPI.getInstance();

        nest.revokeToken(token, new Callback() {
            @Override
            public void onSuccess() {
                failureRef.set(true);
                latch.countDown();
            }

            @Override
            public void onFailure(NestException exception) {
                assertTrue(exception.getMessage().startsWith("Revoking token failed: "));
                latch.countDown();
            }
        });

        server.takeRequest(); // Process the request.

        latch.await();
        assertFalse(failureRef.get());
    }

    @Test
    public void testRevokeToken_shouldHandleConnectionErrorCorrectly() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean failureRef = new AtomicBoolean();

        NestToken token = new NestToken("test-token", 12345);
        final MockWebServer server = new MockWebServer();

        // Disconnect without sending a response, creating a fake connection error.
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST));
        server.start();

        HttpUrl baseUrl = server.url("/");
        System.out.println(baseUrl.toString());

        Whitebox.setInternalState(NestAPI.class, "sBaseAccessTokenUrl", baseUrl.toString());

        NestAPI.setAndroidContext(RuntimeEnvironment.application);
        NestAPI nest = NestAPI.getInstance();

        nest.revokeToken(token, new Callback() {
            @Override
            public void onSuccess() {
                failureRef.set(true);
                latch.countDown();
            }

            @Override
            public void onFailure(NestException exception) {
                assertTrue(exception.getMessage().equals("Request to revoke token failed."));
                latch.countDown();
            }
        });

        server.takeRequest(); // Process the request.

        latch.await();
        assertFalse(failureRef.get());
    }
}
