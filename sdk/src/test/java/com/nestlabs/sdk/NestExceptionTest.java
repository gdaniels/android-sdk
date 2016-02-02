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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@SuppressWarnings("ThrowableInstanceNeverThrown")
public class NestExceptionTest {

    @Test
    public void testNestException_shouldExtendException() {
        assertEquals(NestException.class.getSuperclass(), Exception.class);
    }

    @Test
    public void testNestException_shouldAcceptAMessage() {
        String testMessage = "test-message";
        NestException e = new NestException(testMessage);

        assertEquals(testMessage, e.getMessage());
    }

    @Test
    public void testNestException_shouldAcceptNothing() {
        NestException e = new NestException();

        assertEquals(null, e.getMessage());
    }

    @Test
    public void testNestException_shouldAcceptAThrowable() {
        Throwable t = new Throwable();
        NestException e = new NestException(t);

        assertSame(t, e.getCause());
    }

    @Test
    public void testNestException_shouldAcceptAMessageAndThrowable() {
        String testMessage = "test-message-2";
        Throwable t = new Throwable();
        NestException e = new NestException(testMessage, t);

        assertEquals(testMessage, e.getMessage());
        assertSame(t, e.getCause());
    }

}
