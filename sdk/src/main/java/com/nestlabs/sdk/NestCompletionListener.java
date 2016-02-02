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
import com.firebase.client.FirebaseError;

/**
 * A NestCompletionListener wraps {@link Firebase.CompletionListener} and accepts a {@link Callback}
 * that gets called {@link Callback#onSuccess()} when the listener completes without error. If an
 * error occurs, {@link Callback#onFailure(NestException)} is called instead.
 */
class NestCompletionListener implements Firebase.CompletionListener {
    private Callback mCallback;

    public NestCompletionListener(@NonNull Callback listener) {
        mCallback = listener;
    }

    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        if (mCallback == null) {
            return;
        }

        if (firebaseError == null) {
            mCallback.onSuccess();
        } else {
            mCallback.onFailure(new NestException(firebaseError.getMessage()));
        }
    }
}
