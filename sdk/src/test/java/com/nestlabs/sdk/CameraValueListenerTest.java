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

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NestListener.CameraListener.class, DataSnapshot.class})
public class CameraValueListenerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testOnDataChange_shouldCallListenerOnUpdateWithCorrectValues()
            throws InterruptedException {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        DataSnapshot mockSnap = mock(DataSnapshot.class);

        final Camera camera = new Camera();

        // Create iterable with 3 snapshots.
        ArrayList<DataSnapshot> dataSnapshots = new ArrayList<>();
        dataSnapshots.add(mockSnap);
        dataSnapshots.add(mockSnap);
        dataSnapshots.add(mockSnap);

        when(mockSnap.getChildren()).thenReturn(dataSnapshots);
        when(mockSnap.getValue(Camera.class)).thenReturn(camera);

        CameraValueListener valueListener = new CameraValueListener(mockListener);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                ArrayList<Camera> cameras = (ArrayList<Camera>) invocation.getArguments()[0];
                assertEquals(cameras.size(), 3);
                for (Camera cam : cameras) {
                    assertSame(cam, camera);
                }
                return null;
            }
        }).when(mockListener).onUpdate(any(ArrayList.class));

        valueListener.onDataChange(mockSnap);
    }

    @Test
    public void testOnCancelled_shouldNotDoAnything() {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        FirebaseError mockError = mock(FirebaseError.class);
        CameraValueListener valueListener = new CameraValueListener(mockListener);
        valueListener.onCancelled(mockError);

        verifyZeroInteractions(mockListener, mockError);
    }
}
