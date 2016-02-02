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
@PrepareForTest({GlobalValueListener.class, DataSnapshot.class})
public class GlobalValueListenerTest {

    @Test
    public void testOnDataChange_shouldCallListenerOnUpdateWithCorrectValues()
            throws InterruptedException {
        NestListener.GlobalListener mockListener = mock(NestListener.GlobalListener.class);
        GlobalValueListener valueListener = new GlobalValueListener(mockListener);

        // Real objects.
        final Camera camera = new Camera();
        final Metadata metadata = new Metadata();
        final Structure structure = new Structure();
        final Thermostat thermostat = new Thermostat();
        final SmokeCOAlarm smokeCOAlarm = new SmokeCOAlarm();

        // Top level data snapshot.
        DataSnapshot topLevelSnap = mock(DataSnapshot.class);

        // Snapshots that hold top-level object types.
        DataSnapshot mockDevicesSnap = mock(DataSnapshot.class);
        DataSnapshot mockStructuresSnap = mock(DataSnapshot.class);
        DataSnapshot mockMetadataSnap = mock(DataSnapshot.class);

        // Snapshots that hold device types.
        DataSnapshot mockThermostatsSnap = mock(DataSnapshot.class);
        DataSnapshot mockCamerasSnap = mock(DataSnapshot.class);
        DataSnapshot mockSmokeCOAlarmsSnap = mock(DataSnapshot.class);

        // Snapshot for a structure.
        DataSnapshot mockStructureSnap = mock(DataSnapshot.class);

        // Snapshot for a thermostat.
        DataSnapshot mockThermostatSnap = mock(DataSnapshot.class);

        // Snapshot for a camera.
        DataSnapshot mockCameraSnap = mock(DataSnapshot.class);

        // Snapshot for a smoke alarm.
        DataSnapshot mockSmokeAlarmSnap = mock(DataSnapshot.class);

        ArrayList<DataSnapshot> objectSnapshots = new ArrayList<>();
        objectSnapshots.add(mockDevicesSnap);
        objectSnapshots.add(mockStructuresSnap);
        objectSnapshots.add(mockMetadataSnap);

        ArrayList<DataSnapshot> deviceSnapshots = new ArrayList<>();
        deviceSnapshots.add(mockThermostatsSnap);
        deviceSnapshots.add(mockCamerasSnap);
        deviceSnapshots.add(mockSmokeCOAlarmsSnap);

        // Structure snapshot to return.
        ArrayList<DataSnapshot> structureSnapshots = new ArrayList<>();
        structureSnapshots.add(mockStructureSnap);

        // Thermostat snapshot to return.
        ArrayList<DataSnapshot> thermostatSnapshots = new ArrayList<>();
        thermostatSnapshots.add(mockThermostatSnap);

        // Camera snapshot to return.
        ArrayList<DataSnapshot> cameraSnapshots = new ArrayList<>();
        cameraSnapshots.add(mockCameraSnap);

        // Smoke alarm snapshot to return.
        ArrayList<DataSnapshot> smokeCoAlarmSnapshots = new ArrayList<>();
        smokeCoAlarmSnapshots.add(mockSmokeAlarmSnap);

        // Returning names.
        when(mockDevicesSnap.getName()).thenReturn("devices");
        when(mockStructuresSnap.getName()).thenReturn("structures");
        when(mockMetadataSnap.getName()).thenReturn("metadata");
        when(mockThermostatsSnap.getName()).thenReturn("thermostats");
        when(mockCamerasSnap.getName()).thenReturn("cameras");
        when(mockSmokeCOAlarmsSnap.getName()).thenReturn("smoke_co_alarms");

        // Returning children.
        when(topLevelSnap.getChildren()).thenReturn(objectSnapshots);
        when(mockDevicesSnap.getChildren()).thenReturn(deviceSnapshots);
        when(mockStructuresSnap.getChildren()).thenReturn(structureSnapshots);
        when(mockThermostatsSnap.getChildren()).thenReturn(thermostatSnapshots);
        when(mockCamerasSnap.getChildren()).thenReturn(cameraSnapshots);
        when(mockSmokeCOAlarmsSnap.getChildren()).thenReturn(smokeCoAlarmSnapshots);

        // Returning values.
        when(mockMetadataSnap.getValue(Metadata.class)).thenReturn(metadata);
        when(mockStructureSnap.getValue(Structure.class)).thenReturn(structure);
        when(mockThermostatSnap.getValue(Thermostat.class)).thenReturn(thermostat);
        when(mockCameraSnap.getValue(Camera.class)).thenReturn(camera);
        when(mockSmokeAlarmSnap.getValue(SmokeCOAlarm.class)).thenReturn(smokeCOAlarm);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                GlobalUpdate update = (GlobalUpdate) invocation.getArguments()[0];
                assertEquals(update.getCameras().size(), 1);
                assertSame(update.getCameras().get(0), camera);

                assertEquals(update.getThermostats().size(), 1);
                assertSame(update.getThermostats().get(0), thermostat);

                assertEquals(update.getSmokeCOAlarms().size(), 1);
                assertSame(update.getSmokeCOAlarms().get(0), smokeCOAlarm);

                assertEquals(update.getStructures().size(), 1);
                assertSame(update.getStructures().get(0), structure);

                assertSame(update.getMetadata(), metadata);
                return null;
            }
        }).when(mockListener).onUpdate(any(GlobalUpdate.class));

        valueListener.onDataChange(topLevelSnap);
    }

    @Test
    public void testOnCancelled_shouldNotDoAnything() {
        NestListener.GlobalListener mockListener = mock(NestListener.GlobalListener.class);
        FirebaseError mockError = mock(FirebaseError.class);
        GlobalValueListener valueListener = new GlobalValueListener(mockListener);
        valueListener.onCancelled(mockError);

        verifyZeroInteractions(mockListener, mockError);
    }
}
