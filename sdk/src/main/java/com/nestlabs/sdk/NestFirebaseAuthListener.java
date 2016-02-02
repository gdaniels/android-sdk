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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The NestFirebaseAuthListener wraps a {@link NestListener.AuthListener}, allowing it to receive
 * updates related to Nest API authentication.
 */
class NestFirebaseAuthListener implements Firebase.AuthResultHandler {
    private final NestListener.AuthListener mListener;
    private final Firebase.AuthStateListener mAuthStateListener;
    private final Firebase mFirebaseRef;
    private final AtomicBoolean mAddedAuthStateListener;

    public NestFirebaseAuthListener(@NonNull Firebase firebaseRef,
            NestListener.AuthListener listener,
            @NonNull Firebase.AuthStateListener authStateListener) {
        mListener = listener;
        mAuthStateListener = authStateListener;
        mFirebaseRef = firebaseRef;
        mAddedAuthStateListener = new AtomicBoolean(false);
    }

    @Override
    public void onAuthenticated(AuthData authData) {
        if (mListener != null) {
            mListener.onAuthSuccess();
        }

        // If the auth state listener hasn't been added to Firebase yet, add it now. This should
        // only be added once so multiple calls to NestListener.AuthListener#onAuthRevoked don't
        // occur. We only add the auth state listener once we are authenticated so we can safely
        // assume that any change to an unauthenticated state results from a revocation of the
        // authentication method (the token).
        if (!mAddedAuthStateListener.getAndSet(true)) {
            mFirebaseRef.addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onAuthenticationError(FirebaseError firebaseError) {
        if (mListener != null) {
            if (firebaseError != null) {
                mListener.onAuthFailure(new NestException(firebaseError.getMessage(),
                        firebaseError.toException()));
            } else {
                mListener.onAuthFailure(new NestException("An unknown error occurred."));
            }
        }
    }
}
