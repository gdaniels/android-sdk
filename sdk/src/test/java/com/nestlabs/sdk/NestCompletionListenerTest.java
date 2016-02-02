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
import com.firebase.client.FirebaseError;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NestCompletionListener.class, Firebase.class, FirebaseError.class, Callback.class})
public class NestCompletionListenerTest {

    Callback mockCallback;
    FirebaseError mockFirebaseError;
    Firebase mockFirebase;
    NestException mockException;

    @Before
    public void before() {
        mockCallback = mock(Callback.class);
        mockFirebaseError = mock(FirebaseError.class);
        mockFirebase = mock(Firebase.class);
        mockException = mock(NestException.class);
    }

    @Test
    public void testOnCompleteWithNull_shouldCallCallbackOnSuccess() {
        NestCompletionListener listener = new NestCompletionListener(mockCallback);
        listener.onComplete(null, mockFirebase);
        Mockito.verify(mockCallback).onSuccess();
    }

    @Test
    public void testOnCompleteWithError_shouldCallCallbackOnFailure() throws Exception {
        String testErrorMessage = "test-error-message";
        when(mockFirebaseError.getMessage()).thenReturn(testErrorMessage);
        when(mockException.getMessage()).thenReturn(testErrorMessage);
        whenNew(NestException.class).withArguments(testErrorMessage).thenReturn(mockException);

        NestCompletionListener listener = new NestCompletionListener(mockCallback);
        listener.onComplete(mockFirebaseError, mockFirebase);
        Mockito.verify(mockCallback).onFailure(mockException);
    }
}
