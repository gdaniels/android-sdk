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

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import okhttp3.OkHttpClient;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NestAPI.class, Firebase.class, OkHttpClient.class, NestFirebaseAuthListener.class,
        NestListener.AuthListener.class, Context.class})
public class NestAPITest {
    public static final String TEST_CLIENT_ID = "test-client-id";
    public static final String TEST_CLIENT_SECRET = "test-client-secret";
    public static final String TEST_REDIRECT_URL = "test-redirect-url";

    static Firebase mockFirebase;
    static OkHttpClient mockHttpClient;
    static NestAPI realNest;
    static Map<NestListener, ValueEventListener> listenerMap;

    @BeforeClass
    public static void beforeClass() throws Exception {
        PowerMockito.mockStatic(Firebase.class);

        mockFirebase = mock(Firebase.class);
        mockHttpClient = mock(OkHttpClient.class);

        whenNew(Firebase.class).withArguments(anyString()).thenReturn(mockFirebase);
        when(mockFirebase.child(anyString())).thenReturn(mockFirebase);
        whenNew(OkHttpClient.class).withNoArguments().thenReturn(mockHttpClient);

        NestAPI.setAndroidContext(mock(Context.class));

        realNest = NestAPI.getInstance();
        when(NestAPI.getInstance()).thenReturn(realNest);

        listenerMap = Whitebox.getInternalState(realNest, "mListenerMap");
    }

    @Before
    public void before() {
        // Nothing right now.
        realNest.removeAllListeners();
    }

    @Test
    public void testGetInstance_shouldReturnTheSameObject() {
        NestAPI nest = NestAPI.getInstance();
        NestAPI nest2 = NestAPI.getInstance();

        assertSame(nest, nest2);
        assertNotNull(nest);
        assertNotNull(nest2);
    }

    @Test
    public void testSetCredentials_shouldReturnNullWhenNoCredentialsSet() {
        NestAPI nest = NestAPI.getInstance();
        NestConfig config = nest.getConfig();

        assertEquals(null, config);
    }

    @Test
    public void testSetCredentials_shouldCreateNewCredentials() {
        NestAPI nest = NestAPI.getInstance();
        nest.setConfig(TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_REDIRECT_URL);

        NestConfig config = nest.getConfig();
        assertEquals(TEST_CLIENT_ID, config.getClientID());
        assertEquals(TEST_CLIENT_SECRET, config.getClientSecret());
        assertEquals(TEST_REDIRECT_URL, config.getRedirectURL());
    }

    @Test
    public void testClearConfig_shouldSetConfigToNull() {
        NestAPI nest = NestAPI.getInstance();
        nest.setConfig(TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_REDIRECT_URL);
        assertNotNull(nest.getConfig());
        nest.clearConfig();
        assertNull(nest.getConfig());
    }

    @Test
    public void testAuthWithNestToken_shouldCallAuthWithString() {
        NestListener.AuthListener mockListener = mock(NestListener.AuthListener.class);
        String testToken = "test-token";
        long testExpiresIn = 123;

        NestToken token = new NestToken(testToken, testExpiresIn);

        NestAPI nest = NestAPI.getInstance();
        NestAPI mockNest = Mockito.spy(nest);
        Mockito.doCallRealMethod().when(mockNest).authWithToken(token, mockListener);
        mockNest.authWithToken(token, mockListener);
        verify(mockNest).authWithToken(testToken, mockListener);
    }

    @Test
    public void testAuthWithStringToken_shouldAuthWithFirebase() throws Exception {
        NestListener.AuthListener mockListener = mock(NestListener.AuthListener.class);
        NestFirebaseAuthListener mockFirebaseListener = mock(NestFirebaseAuthListener.class);
        String testToken = "test-token";

        whenNew(NestFirebaseAuthListener.class)
                .withArguments(
                        eq(mockFirebase),
                        eq(mockListener),
                        any(Firebase.AuthStateListener.class)
                )
                .thenReturn(mockFirebaseListener);

        NestAPI nest = NestAPI.getInstance();
        nest.authWithToken(testToken, mockListener);

        verify(mockFirebase).authWithCustomToken(testToken, mockFirebaseListener);
    }

    @Test
    public void testAddGlobalListener_shouldAddListenerToFirebase() throws Exception {
        NestListener.GlobalListener mockGlobalListener = mock(NestListener.GlobalListener.class);
        GlobalValueListener mockGlobalValueListener = mock(GlobalValueListener.class);

        whenNew(GlobalValueListener.class)
                .withArguments(mockGlobalListener)
                .thenReturn(mockGlobalValueListener);

        NestAPI nest = NestAPI.getInstance();

        nest.addGlobalListener(mockGlobalListener);
        verify(mockFirebase).addValueEventListener(mockGlobalValueListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testAddDeviceListener_shouldAddListenerToFirebase() throws Exception {
        NestListener.DeviceListener mockListener = mock(NestListener.DeviceListener.class);
        DeviceValueListener mockValueListener = mock(DeviceValueListener.class);

        whenNew(DeviceValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        NestAPI nest = NestAPI.getInstance();

        nest.addDeviceListener(mockListener);
        verify(mockFirebase).addValueEventListener(mockValueListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testAddStructureListener_shouldAddListenerToFirebase() throws Exception {
        NestListener.StructureListener mockListener = mock(NestListener.StructureListener.class);
        StructureValueListener mockValueListener = mock(StructureValueListener.class);

        whenNew(StructureValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        NestAPI nest = NestAPI.getInstance();

        nest.addStructureListener(mockListener);
        verify(mockFirebase).addValueEventListener(mockValueListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testAddMetadataListener_shouldAddListenerToFirebase() throws Exception {
        NestListener.MetadataListener mockListener = mock(NestListener.MetadataListener.class);
        MetadataValueListener mockValueListener = mock(MetadataValueListener.class);

        whenNew(MetadataValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        NestAPI nest = NestAPI.getInstance();

        nest.addMetadataListener(mockListener);
        verify(mockFirebase).addValueEventListener(mockValueListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testAddThermostatListener_shouldAddListenerToFirebase() throws Exception {
        NestListener.ThermostatListener mockListener = mock(NestListener.ThermostatListener.class);
        ThermostatValueListener mockValueListener = mock(ThermostatValueListener.class);

        whenNew(ThermostatValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        NestAPI nest = NestAPI.getInstance();

        nest.addThermostatListener(mockListener);
        verify(mockFirebase).addValueEventListener(mockValueListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testAddSmokeCOAlarmListener_shouldAddListenerToFirebase() throws Exception {
        NestListener.SmokeCOAlarmListener mockListener =
                mock(NestListener.SmokeCOAlarmListener.class);
        SmokeCOAlarmValueListener mockValueListener = mock(SmokeCOAlarmValueListener.class);

        whenNew(SmokeCOAlarmValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        NestAPI nest = NestAPI.getInstance();

        nest.addSmokeCOAlarmListener(mockListener);
        verify(mockFirebase).addValueEventListener(mockValueListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testAddCameraListener_shouldAddListenerToFirebase() throws Exception {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        CameraValueListener mockValueListener = mock(CameraValueListener.class);

        whenNew(CameraValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        NestAPI nest = NestAPI.getInstance();

        nest.addCameraListener(mockListener);

        verify(mockFirebase).addValueEventListener(mockValueListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testAddListeners_shouldAddListenerToListenerMap() {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        NestListener.CameraListener mockListener2 = mock(NestListener.CameraListener.class);
        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        nest.addCameraListener(mockListener2);
        assertEquals(listenerMap.size(), 2);
    }

    @Test
    public void testAddSameListenerTwice_shouldAddOnlyOneListenerToListenerMap() {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        nest.addCameraListener(mockListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testRemoveAllListeners_shouldRemoveAllListeners() {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        NestListener.CameraListener mockListener2 = mock(NestListener.CameraListener.class);
        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        nest.addCameraListener(mockListener2);
        nest.removeAllListeners();
        assertEquals(listenerMap.size(), 0);
    }

    @Test
    public void testAddListenersThenRemoveOneListener_shouldResultInOneListener() {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        NestListener.CameraListener mockListener2 = mock(NestListener.CameraListener.class);
        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        nest.addCameraListener(mockListener2);
        nest.removeListener(mockListener);
        assertEquals(listenerMap.size(), 1);
    }

    @Test
    public void testRemoveListenerWithValidListener_shouldReturnTrue() {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        assertEquals(nest.removeListener(mockListener), true);
    }

    @Test
    public void testRemoveListenerWithInvalidListener_shouldReturnFalse() {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        NestListener.CameraListener mockListener2 = mock(NestListener.CameraListener.class);
        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        assertEquals(nest.removeListener(mockListener2), false);
    }

    @Test
    public void testRemoveListener_shouldRemoveListenerFromFirebase() throws Exception {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        CameraValueListener mockValueListener = mock(CameraValueListener.class);

        whenNew(CameraValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        nest.removeListener(mockListener);
        verify(mockFirebase).removeEventListener(mockValueListener);
    }

    @Test
    public void testRemoveAllListeners_shouldRemoveAllListenersFromFirebase() throws Exception {
        NestListener.CameraListener mockListener = mock(NestListener.CameraListener.class);
        NestListener.CameraListener mockListener2 = mock(NestListener.CameraListener.class);
        CameraValueListener mockValueListener = mock(CameraValueListener.class);
        CameraValueListener mockValueListener2 = mock(CameraValueListener.class);

        whenNew(CameraValueListener.class)
                .withArguments(mockListener)
                .thenReturn(mockValueListener);

        whenNew(CameraValueListener.class)
                .withArguments(mockListener2)
                .thenReturn(mockValueListener2);

        NestAPI nest = NestAPI.getInstance();
        nest.addCameraListener(mockListener);
        nest.addCameraListener(mockListener2);
        nest.removeAllListeners();
        verify(mockFirebase).removeEventListener(mockValueListener);
        verify(mockFirebase).removeEventListener(mockValueListener2);
    }

    @Test
    public void testLaunchAuthFlow_shouldLaunchANewActivity() throws Exception {
        Activity mockActivity = mock(Activity.class);
        Intent mockIntent = mock(Intent.class);
        whenNew(Intent.class).withArguments(mockActivity, NestAuthActivity.class)
                .thenReturn(mockIntent);

        NestAPI nest = NestAPI.getInstance();

        nest.setConfig("client-id", "client-secret", "redirect-url");
        nest.launchAuthFlow(mockActivity, 123);

        NestConfig config = nest.getConfig();

        verify(mockActivity).startActivityForResult(mockIntent, 123);
        verify(mockIntent).putExtra("client_metadata_key", config);
    }

}
