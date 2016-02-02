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

public class NestTokenTest {

    public static final String TEST_TOKEN = "test-token";
    public static final long TEST_EXPIRES_IN = 123;

    @Test
    public void testNestToken_shouldReturnCorrectValuesWhenSet() {
        NestToken token = new NestToken(TEST_TOKEN, TEST_EXPIRES_IN);

        assertEquals(TEST_TOKEN, token.getToken());
        assertEquals(TEST_EXPIRES_IN, token.getExpiresIn());
    }

    @Test
    public void testNestToken_shouldReturnNullTokenWhenNotSet() {
        NestToken token = new NestToken();

        assertEquals(null, token.getToken());
    }

    @Test
    public void testNestToken_shouldReturnZeroExpiresInWhenNotSet() {
        NestToken token = new NestToken();

        assertEquals(0, token.getExpiresIn());
    }

    @Test
    public void testDescribeContents_shouldReturnZero() {
        NestToken t = new NestToken();
        assertEquals(t.describeContents(), 0);
    }

    @Test
    public void testNewArray_shouldReturnArrayOfCorrectSize() {
        int tokenSize = new Random().nextInt(9) + 1;

        NestToken[] tokens = NestToken.CREATOR.newArray(tokenSize);
        assertEquals(tokens.length, tokenSize);
    }

    @Test
    public void testEquals_shouldReturnFalseWithNonNestToken() {
        Object o = new Object();
        NestToken t = new NestToken();
        assertFalse(t.equals(o));
    }
}
