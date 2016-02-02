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

import com.firebase.client.Firebase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CameraSetter.class, Firebase.class, NestCompletionListener.class, Callback.class})
public class CameraSetterTest {

    Firebase mockFirebase;
    Callback mockCallback;
    NestCompletionListener mockListener;

    @Before
    public void before() throws Exception {
        mockFirebase = mock(Firebase.class);
        mockCallback = mock(Callback.class);
        mockListener = mock(NestCompletionListener.class);

        whenNew(NestCompletionListener.class).withArguments(mockCallback).thenReturn(mockListener);
        when(mockFirebase.child(anyString())).thenReturn(mockFirebase);
    }

    @Test
    public void testSetIsStreaming_shouldSetCorrectValues() {
        String testId = "test-id";
        boolean testValue = true;

        CameraSetter setter = new CameraSetter(mockFirebase);
        setter.setIsStreaming(testId, testValue);

        Mockito.verify(mockFirebase)
                .child("/devices/cameras/" + testId + "/" + Camera.KEY_IS_STREAMING);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetIsStreaming_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        boolean testValue = true;

        CameraSetter setter = new CameraSetter(mockFirebase);
        setter.setIsStreaming(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase)
                .child("/devices/cameras/" + testId + "/" + Camera.KEY_IS_STREAMING);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }
}
