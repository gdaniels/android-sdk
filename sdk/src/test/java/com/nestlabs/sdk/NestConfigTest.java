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

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NestConfigTest {

    public static final String TEST_CLIENT_ID = "test-client-id";
    public static final String TEST_CLIENT_SECRET = "test-client-secret";
    public static final String TEST_REDIRECT_URL = "test-redirect-url";

    @Test
    public void testNestConfig_shouldReturnCorrectValuesWhenSet() {

        NestConfig config = new NestConfig.Builder()
                .clientID(TEST_CLIENT_ID)
                .clientSecret(TEST_CLIENT_SECRET)
                .redirectURL(TEST_REDIRECT_URL)
                .build();

        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertEquals(TEST_CLIENT_SECRET, config.getClientSecret());
        assertEquals(TEST_REDIRECT_URL, config.getRedirectURL());
    }

    @Test
    public void testNestConfig_shouldReturnNullValuesWhenNotSet() {
        NestConfig config = new NestConfig.Builder().build();

        assertEquals(null, config.getClientID());
        assertEquals(null, config.getClientSecret());
        assertEquals(null, config.getRedirectURL());
    }

    @Test
    public void testNestConfig_setStateValue_shouldReturnCorrectlyFormattedString() {
        NestConfig config = new NestConfig.Builder()
                .build();

        assertTrue(config.getStateValue().matches("^app-state\\d+-\\d+$"));
    }

    @Test
    public void testDescribeContents_shouldReturnZero() {
        NestConfig config = new NestConfig.Builder().build();
        assertEquals(config.describeContents(), 0);
    }

    @Test
    public void testNewArray_shouldReturnArrayOfCorrectSize() {
        int configSize = new Random().nextInt(9) + 1;

        NestConfig[] configs = NestConfig.CREATOR.newArray(configSize);
        assertEquals(configs.length, configSize);
    }

    @Test
    public void testEquals_shouldReturnFalseWithNonNestConfig() {
        Object o = new Object();
        NestConfig config = new NestConfig.Builder().build();
        assertFalse(config.equals(o));
    }
}
