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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * AuthStateListener is a {@link Firebase.AuthStateListener} that accepts a {@link
 * NestListener.AuthListener}. It listens for events where the the auth state has turned to null,
 * and calls {@link NestListener.AuthListener#onAuthRevoked()} as a result.
 */
class AuthStateListener implements Firebase.AuthStateListener {

    private final NestListener.AuthListener mAuthListener;

    AuthStateListener(final NestListener.AuthListener authListener) {
        mAuthListener = authListener;
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        if (authData == null && mAuthListener != null) {
            mAuthListener.onAuthRevoked();
        }
    }
}
