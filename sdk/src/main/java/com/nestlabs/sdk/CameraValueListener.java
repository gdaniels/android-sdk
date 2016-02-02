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
 * CameraValueListener accepts a {@link NestListener.CameraListener} that will receive {@link
 * NestListener.CameraListener#onUpdate(ArrayList)})} events when this listener receives events from
 * Nest.
 */
class CameraValueListener implements ValueEventListener {

    private final NestListener.CameraListener mListener;

    CameraValueListener(@NonNull NestListener.CameraListener listener) {
        mListener = listener;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final ArrayList<Camera> cameras = new ArrayList<>();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Camera camera = postSnapshot.getValue(Camera.class);
            cameras.add(camera);
        }
        mListener.onUpdate(cameras);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        // Do nothing.
    }
}
