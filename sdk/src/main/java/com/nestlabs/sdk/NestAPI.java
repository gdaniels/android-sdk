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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.firebase.client.Config;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * NestAPI creates an easy-to-use interface for both reading to and writing from the Nest API.
 * <p>
 * To get started, set the {@link Context} with {@link #setAndroidContext(Context)} After a context
 * is set, get the singleton instance {@link NestAPI} object with {@link #getInstance()}
 * <p>
 * You can call {@link #getInstance()} anytime after initialization anywhere in your app to access
 * this object again.
 */
public final class NestAPI {
    private static final String BASE_AUTHORIZATION_URL = "https://home.nest.com/";
    private static final String REVOKE_TOKEN_PATH = "oauth2/access_tokens/";
    private static final String NEST_FIREBASE_URL = "https://developer-api.nest.com";
    private static final String KEY_CLIENT_METADATA = "client_metadata_key";
    private static final String KEY_ACCESS_TOKEN = "access_token_key";

    private static String sBaseAccessTokenUrl = "https://api.home.nest.com/";
    static final String ACCESS_URL = sBaseAccessTokenUrl
            + "oauth2/access_token?code=%s&client_id=%s&client_secret=%s"
            + "&grant_type=authorization_code";

    static final String CLIENT_CODE_URL = BASE_AUTHORIZATION_URL
            + "login/oauth2?client_id=%s&state=%s";

    static final String KEY_DEVICES = "devices";
    static final String KEY_METADATA = "metadata";
    static final String KEY_THERMOSTATS = "thermostats";
    static final String KEY_SMOKE_CO_ALARMS = "smoke_co_alarms";
    static final String KEY_CAMERAS = "cameras";
    static final String KEY_STRUCTURES = "structures";

    private static NestAPI sInstance;

    private final Map<NestListener, ValueEventListener> mListenerMap;
    private final OkHttpClient mHttpClient;
    private final Firebase mFirebaseRef;

    private NestConfig mNestConfig;
    private Firebase.AuthStateListener mAuthStateListener;
    private NestListener.AuthListener mAuthListener;

    public final ThermostatSetter thermostats;
    public final StructureSetter structures;
    public final CameraSetter cameras;

    /**
     * Creates a new instance of the {@link NestAPI}. Don't call this method directly in practice.
     * Use {@link #getInstance()} instead.
     */
    private NestAPI() {
        Config config = new Config();
        config.setAuthenticationServer(sBaseAccessTokenUrl);
        Firebase.setDefaultConfig(config);
        mFirebaseRef = new Firebase(NEST_FIREBASE_URL);

        mListenerMap = new HashMap<>();
        mHttpClient = new OkHttpClient();

        thermostats = new ThermostatSetter(mFirebaseRef);
        structures = new StructureSetter(mFirebaseRef);
        cameras = new CameraSetter(mFirebaseRef);
    }

    /**
     * Sets the {@link Context}. You must call this with a valid {@link Context} object. If this
     * method is not called before trying to use the {@link NestAPI} instance, all interactions with
     * the API will likely fail. You only need to call this once.
     *
     * @param context A valid {@link android.content.Context}.
     */
    public static void setAndroidContext(@NonNull Context context) {
        Firebase.setAndroidContext(context.getApplicationContext());
    }

    /**
     * Returns the instance of the {@link NestAPI}, if one exists, otherwise creates a new one.
     *
     * @return A {@link NestAPI} instance for interacting with the Nest API.
     */
    public static NestAPI getInstance() {
        if (sInstance == null) {
            sInstance = new NestAPI();
        }

        return sInstance;
    }

    /**
     * Sets the Nest configuration values used for authentication.
     *
     * @param clientId     The Nest client ID.
     * @param clientSecret The Nest client secret.
     * @param redirectUrl  The Nest redirect URL.
     */
    public void setConfig(String clientId, String clientSecret, String redirectUrl) {
        mNestConfig = new NestConfig.Builder().clientID(clientId)
                .clientSecret(clientSecret)
                .redirectURL(redirectUrl)
                .build();
    }

    /**
     * Returns a {@link NestConfig} object containing the currently set credentials. If there are no
     * credentials set, returns null.
     *
     * @return a {@link NestConfig} object containing current config values, or null if unset.
     */
    public NestConfig getConfig() {
        return mNestConfig;
    }

    /**
     * Clears the currently stored credentials.
     */
    public void clearConfig() {
        mNestConfig = null;
    }

    /**
     * Requests authentication with a {@link NestToken}.
     *
     * @param token    the NestToken to authenticate with
     * @param listener a listener to be notified when authentication succeeds or fails
     */
    public void authWithToken(@NonNull NestToken token, final NestListener.AuthListener listener) {
        authWithToken(token.getToken(), listener);
    }

    /**
     * Requests authentication with a raw token.
     *
     * @param token        the token String to authenticate with
     * @param authListener a listener notified when authentication succeeds, fails, or is revoked.
     */
    public void authWithToken(@NonNull String token, final NestListener.AuthListener authListener) {
        mAuthListener = authListener;

        // If we have previously called this method and an auth state listener was set, remove it.
        if (mAuthStateListener != null) {
            mFirebaseRef.removeAuthStateListener(mAuthStateListener);
        }

        // Create a new AuthStateListener for when an auth state changes to null (revoking of auth).
        mAuthStateListener = new AuthStateListener(mAuthListener);

        mFirebaseRef.authWithCustomToken(token,
                new NestFirebaseAuthListener(mFirebaseRef, mAuthListener, mAuthStateListener));
    }

    /**
     * Returns a {@link NestToken} embedded in the {@link Intent} that is returned in the result
     * from {@link #launchAuthFlow(Activity, int)}.
     *
     * @param intent the intent to retrieve the NestToken from.
     * @return the {@link NestToken}, if it was contained in the {@link Intent}, otherwise null.
     */
    public static NestToken getAccessTokenFromIntent(Intent intent) {
        return intent.getParcelableExtra(KEY_ACCESS_TOKEN);
    }

    /**
     * Revokes a {@link NestToken} from the Nest API.
     *
     * @param token    The token to revoke.
     * @param callback A callback for the result of the revocation.
     */
    public void revokeToken(NestToken token, @NonNull final Callback callback) {

        Request request = new Request.Builder().url(
                sBaseAccessTokenUrl + REVOKE_TOKEN_PATH + token.getToken()).delete().build();

        mHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(new NestException("Request to revoke token failed.", e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(
                            new NestException("Revoking token failed: " + response.toString()));
                    return;
                }
                callback.onSuccess();
            }
        });
    }

    /**
     * Adds a listener to receive updates when any data changes.
     *
     * @param listener the {@link NestListener.GlobalListener} to receive changes.
     */
    public void addGlobalListener(final NestListener.GlobalListener listener) {
        ValueEventListener fireListener = new GlobalValueListener(listener);
        mFirebaseRef.addValueEventListener(fireListener);
        mListenerMap.put(listener, fireListener);
    }

    /**
     * Adds a listener to receive updates when any {@link Device} changes.
     *
     * @param listener the {@link NestListener.DeviceListener} to receive changes.
     */
    public void addDeviceListener(final NestListener.DeviceListener listener) {
        ValueEventListener fireListener = new DeviceValueListener(listener);
        mFirebaseRef.child(KEY_DEVICES).addValueEventListener(fireListener);
        mListenerMap.put(listener, fireListener);
    }

    /**
     * Adds a listener to receive updates when any {@link Thermostat} changes.
     *
     * @param listener the {@link NestListener.ThermostatListener} to receive  dchanges.
     */
    public void addThermostatListener(final NestListener.ThermostatListener listener) {
        ValueEventListener fireListener = new ThermostatValueListener(listener);
        String path = new Utils.PathBuilder().append(KEY_DEVICES).append(KEY_THERMOSTATS).build();

        mFirebaseRef.child(path).addValueEventListener(fireListener);
        mListenerMap.put(listener, fireListener);
    }

    /**
     * Adds a listener to receive updates when any {@link Camera} changes.
     *
     * @param listener the {@link NestListener.CameraListener} to receive changes.
     */
    public void addCameraListener(final NestListener.CameraListener listener) {
        ValueEventListener fireListener = new CameraValueListener(listener);
        String path = new Utils.PathBuilder().append(KEY_DEVICES).append(KEY_CAMERAS).build();

        mFirebaseRef.child(path).addValueEventListener(fireListener);
        mListenerMap.put(listener, fireListener);
    }

    /**
     * Adds a listener to receive updates when any {@link Structure} changes.
     *
     * @param listener the {@link NestListener.StructureListener} to receive changes.
     */
    public void addStructureListener(final NestListener.StructureListener listener) {
        ValueEventListener fireListener = new StructureValueListener(listener);
        mFirebaseRef.child(KEY_STRUCTURES).addValueEventListener(fireListener);
        mListenerMap.put(listener, fireListener);
    }

    /**
     * Adds a listener to receive updates when any {@link SmokeCOAlarm} changes.
     *
     * @param listener the {@link NestListener.SmokeCOAlarmListener} to receive changes.
     */
    public void addSmokeCOAlarmListener(final NestListener.SmokeCOAlarmListener listener) {
        ValueEventListener fireListener = new SmokeCOAlarmValueListener(listener);

        String path =
                new Utils.PathBuilder().append(KEY_DEVICES).append(KEY_SMOKE_CO_ALARMS).build();
        mFirebaseRef.child(path).addValueEventListener(fireListener);
        mListenerMap.put(listener, fireListener);
    }

    /**
     * Adds a listener to receive updates when the {@link Metadata} changes.
     *
     * @param listener the {@link NestListener.MetadataListener} to receive changes.
     */
    public void addMetadataListener(final NestListener.MetadataListener listener) {
        MetadataValueListener fireListener = new MetadataValueListener(listener);
        mFirebaseRef.child(KEY_METADATA).addValueEventListener(fireListener);
        mListenerMap.put(listener, fireListener);
    }

    /**
     * Removes a listener, turning off any notification of changes. Must pass in the same listener
     * object that was initially added.
     *
     * @param listener the {@link NestListener} to remove and turn off notifications for.
     * @return true if the listener was successfully removed, false if the listener didn't exist.
     */
    public boolean removeListener(NestListener listener) {
        // If the listener passed into authWithToken is removed, remove authStateChanged listener.
        if (listener == mAuthListener) {
            mFirebaseRef.removeAuthStateListener(mAuthStateListener);
            mAuthStateListener = null;
            return true;
        }

        if (mListenerMap.containsKey(listener)) {
            ValueEventListener fireListener = mListenerMap.get(listener);
            mFirebaseRef.removeEventListener(fireListener);
            mListenerMap.remove(listener);
            return true;
        }
        return false;
    }

    /**
     * Removes all listeners, turning off all updates to changes from the Nest API.
     */
    public void removeAllListeners() {
        if (mAuthStateListener != null) {
            mFirebaseRef.removeAuthStateListener(mAuthStateListener); // Remove auth state listener.
            mAuthStateListener = null;
        }
        for (ValueEventListener fireListener : mListenerMap.values()) {
            mFirebaseRef.removeEventListener(fireListener);
        }
        mListenerMap.clear();
    }

    /**
     * Start an {@link Activity} which will guide a user through the authentication process.
     *
     * @param activity    the {@link Activity} return the result. Typically the current {@link
     *                    Activity}.
     * @param requestCode the request code for which a result will be returned.
     */
    public void launchAuthFlow(Activity activity, int requestCode) {
        final Intent authFlowIntent = new Intent(activity, NestAuthActivity.class);
        authFlowIntent.putExtra(KEY_CLIENT_METADATA, mNestConfig);
        activity.startActivityForResult(authFlowIntent, requestCode);
    }
}
