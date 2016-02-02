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

import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * GlobalValueListener accepts a {@link NestListener.GlobalListener} that will receive {@link
 * NestListener.GlobalListener#onUpdate(GlobalUpdate)} events when this listener receives events
 * from Nest.
 */
class GlobalValueListener implements ValueEventListener {
    private final NestListener.GlobalListener mListener;

    GlobalValueListener(@NonNull NestListener.GlobalListener listener) {
        mListener = listener;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<Thermostat> thermostats = new ArrayList<>();
        ArrayList<Camera> cameras = new ArrayList<>();
        ArrayList<SmokeCOAlarm> smokeAlarms = new ArrayList<>();
        ArrayList<Structure> structures = new ArrayList<>();
        Metadata metadata = null;

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            String name = postSnapshot.getName();

            if (NestAPI.KEY_DEVICES.equals(name)) {
                for (DataSnapshot deviceSnapshot : postSnapshot.getChildren()) {
                    String deviceName = deviceSnapshot.getName();

                    if (NestAPI.KEY_THERMOSTATS.equals(deviceName)) {
                        for (DataSnapshot thermostatSnap : deviceSnapshot.getChildren()) {
                            Thermostat t = thermostatSnap.getValue(Thermostat.class);
                            thermostats.add(t);
                        }
                    } else if (NestAPI.KEY_CAMERAS.equals(deviceName)) {
                        for (DataSnapshot cameraSnap : deviceSnapshot.getChildren()) {
                            Camera c = cameraSnap.getValue(Camera.class);
                            cameras.add(c);
                        }
                    } else if (NestAPI.KEY_SMOKE_CO_ALARMS.equals(deviceName)) {
                        for (DataSnapshot smokeAlarmSnap : deviceSnapshot.getChildren()) {
                            SmokeCOAlarm s = smokeAlarmSnap.getValue(SmokeCOAlarm.class);
                            smokeAlarms.add(s);
                        }
                    }
                }
            } else if (NestAPI.KEY_STRUCTURES.equals(name)) {
                for (DataSnapshot structureSnap : postSnapshot.getChildren()) {
                    Structure s = structureSnap.getValue(Structure.class);
                    structures.add(s);
                }
            } else if (NestAPI.KEY_METADATA.equals(name)) {
                metadata = postSnapshot.getValue(Metadata.class);
            }
        }
        mListener.onUpdate(
                new GlobalUpdate(thermostats, smokeAlarms, cameras, structures, metadata));
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        // Do nothing.
    }
}
