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

import android.os.Build;
import android.os.Parcel;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class NestConfigAndroidTest {

    public static final String TEST_CLIENT_ID = "test-client-id";
    public static final String TEST_CLIENT_SECRET = "test-client-secret";
    public static final String TEST_REDIRECT_URL = "test-redirect-url";
    public static final String TEST_CONFIG_JSON = "/test-nest-config.json";

    @Test
    public void testNestConfigToParcel() {
        String testClientId = "test-id";
        String testClientSecret = "test-secret";
        String testRedirectURL = "test-redirect-url";

        NestConfig config = new NestConfig.Builder()
                .clientID(testClientId)
                .clientSecret(testClientSecret)
                .redirectURL(testRedirectURL)
                .build();

        Parcel parcel = Parcel.obtain();
        config.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        NestConfig configFromParcel = NestConfig.CREATOR.createFromParcel(parcel);
        assertEquals(config, configFromParcel);
    }

    @Test
    public void testToString_shouldReturnNicelyFormattedString() {
        NestConfig config = new NestConfig.Builder()
                .clientID(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .redirectURL(TEST_REDIRECT_URL)
                .build();

        try {
            String jsonString = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_CONFIG_JSON), "utf-8").trim();
            JSONObject obj = new JSONObject(jsonString);
            obj.put(NestConfig.KEY_STATE_VALUE, config.getStateValue()); // Set ephemeral state.
            assertEquals(obj.toString(), config.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
