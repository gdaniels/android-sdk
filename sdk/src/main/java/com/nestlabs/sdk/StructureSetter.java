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
 * StructureSetter provides methods for setting values on {@link Structure}s.
 */
public class StructureSetter {

    private final Firebase mFirebaseRef;

    private static String getPath(@NonNull String structureId, @NonNull String attribute) {
        return new Utils.PathBuilder()
                .append(NestAPI.KEY_STRUCTURES)
                .append(structureId)
                .append(attribute).build();
    }

    public StructureSetter(final Firebase firebaseRef) {
        mFirebaseRef = firebaseRef;
    }

    /**
     * Sets the state of the structure. In order for a structure to be in the Auto-Away state, all
     * devices must also be in Auto-Away state. When any device leaves the Auto-Away state, then the
     * structure also leaves the Auto-Away state.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param awayState   The state of the structure. Values can be "home", "away", or "auto-away".
     */
    public void setAway(@NonNull String structureId, String awayState) {
        String path = getPath(structureId, Structure.KEY_AWAY);
        mFirebaseRef.child(path).setValue(awayState);
    }

    /**
     * Sets the state of the structure. In order for a structure to be in the Auto-Away state, all
     * devices must also be in Auto-Away state. When any device leaves the Auto-Away state, then the
     * structure also leaves the Auto-Away state.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param awayState   The state of the structure. Values can be "home", "away", or "auto-away".
     * @param callback    A {@link Callback} to receive whether the change was successful.
     */
    public void setAway(@NonNull String structureId, String awayState, @NonNull Callback callback) {
        String path = getPath(structureId, Structure.KEY_AWAY);
        mFirebaseRef.child(path).setValue(awayState, new NestCompletionListener(callback));
    }

    /**
     * Sets the ETA on a structure. It is used to let Nest know that a user is expected to return
     * home at a specific time.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param eta         The {@link Structure.ETA} object containing the ETA values.
     */
    public void setEta(@NonNull String structureId, Structure.ETA eta) {
        String path = getPath(structureId, Structure.KEY_ETA);
        mFirebaseRef.child(path).setValue(eta.toString());
    }

    /**
     * Sets the ETA on a structure. It is used to let Nest know that a user is expected to return
     * home at a specific time.
     *
     * @param structureId The unique identifier for the {@link Structure}.
     * @param eta         The {@link Structure.ETA} object containing the ETA values.
     * @param callback    A {@link Callback} to receive whether the change was successful.
     */
    public void setEta(@NonNull String structureId, Structure.ETA eta, @NonNull Callback callback) {
        String path = getPath(structureId, Structure.KEY_ETA);
        mFirebaseRef.child(path).setValue(eta.toString(), new NestCompletionListener(callback));
    }
}
