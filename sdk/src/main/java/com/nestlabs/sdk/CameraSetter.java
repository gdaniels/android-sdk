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

import com.firebase.client.Firebase;

/**
 * CameraSetter provides methods for setting values on {@link Camera}s.
 */
public class CameraSetter {
    private final Firebase mFirebaseRef;

    private static String getPath(@NonNull String cameraId, @NonNull String attribute) {
        return new Utils.PathBuilder().append(NestAPI.KEY_DEVICES)
                .append(NestAPI.KEY_CAMERAS)
                .append(cameraId)
                .append(attribute)
                .build();
    }

    CameraSetter(@NonNull final Firebase firebaseRef) {
        mFirebaseRef = firebaseRef;
    }

    /**
     * Sets the {@link Camera} streaming status on or off.
     *
     * @param cameraId    The unique identifier of the camera.
     * @param isStreaming true to turn streaming on, false to turn streaming off.
     * @param callback    A {@link Callback} to receive whether the change was successful.
     */
    public void setIsStreaming(@NonNull String cameraId, boolean isStreaming,
            @NonNull Callback callback) {
        String path = getPath(cameraId, Camera.KEY_IS_STREAMING);
        mFirebaseRef.child(path).setValue(isStreaming, new NestCompletionListener(callback));
    }

    /**
     * Sets the {@link Camera} streaming status on or off.
     *
     * @param cameraId    The unique identifier of the camera.
     * @param isStreaming true to turn streaming on, false to turn streaming off.
     */
    public void setIsStreaming(@NonNull String cameraId, boolean isStreaming) {
        String path = getPath(cameraId, Camera.KEY_IS_STREAMING);
        mFirebaseRef.child(path).setValue(isStreaming);
    }
}
