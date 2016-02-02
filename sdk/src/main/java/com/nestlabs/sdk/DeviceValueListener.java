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
 * DeviceValueListener accepts a {@link NestListener.DeviceListener} that will receive {@link
 * NestListener.DeviceListener#onUpdate(DeviceUpdate)} events when this listener receives events
 * from Nest.
 */
class DeviceValueListener implements ValueEventListener {

    private final NestListener.DeviceListener mListener;

    DeviceValueListener(@NonNull NestListener.DeviceListener listener) {
        mListener = listener;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final ArrayList<Thermostat> thermostats = new ArrayList<>();
        final ArrayList<Camera> cameras = new ArrayList<>();
        final ArrayList<SmokeCOAlarm> smokeAlarms = new ArrayList<>();

        for (DataSnapshot deviceSnapshot : dataSnapshot.getChildren()) {
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

        mListener.onUpdate(new DeviceUpdate(thermostats, smokeAlarms, cameras));
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        // Do nothing.
    }
}
