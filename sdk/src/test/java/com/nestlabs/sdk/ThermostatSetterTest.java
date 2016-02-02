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

import com.firebase.client.Firebase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ThermostatSetter.class, Firebase.class, NestCompletionListener.class,
        Callback.class})
public class ThermostatSetterTest {

    Firebase mockFirebase;
    Callback mockCallback;
    NestCompletionListener mockListener;

    @Before
    public void before() throws Exception {
        mockFirebase = mock(Firebase.class);
        mockCallback = mock(Callback.class);
        mockListener = mock(NestCompletionListener.class);

        whenNew(NestCompletionListener.class).withArguments(mockCallback).thenReturn(mockListener);
        when(mockFirebase.child(anyString())).thenReturn(mockFirebase);
    }

    @Test
    public void testSetTargetTemperatureF_shouldSetCorrectValues() {
        String testId = "test-id";
        long testValue = 67;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureF(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_F);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetTargetTemperatureF_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        long testValue = 67;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureF(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_F);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }

    @Test
    public void testSetTargetTemperatureC_shouldSetCorrectValues() {
        String testId = "test-id";
        double testValue = 67.0;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureC(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_C);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetTargetTemperatureC_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        double testValue = 67.0;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureC(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_C);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }

    @Test
    public void testSetTargetTemperatureLowF_shouldSetCorrectValues() {
        String testId = "test-id";
        long testValue = 67;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureLowF(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_LOW_F);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetTargetTemperatureLowF_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        long testValue = 67;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureLowF(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_LOW_F);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }

    @Test
    public void testSetTargetTemperatureLowC_shouldSetCorrectValues() {
        String testId = "test-id";
        double testValue = 67.0;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureLowC(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_LOW_C);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetTargetTemperatureLowC_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        double testValue = 67.0;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureLowC(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_LOW_C);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }

    @Test
    public void testSetTargetTemperatureHighF_shouldSetCorrectValues() {
        String testId = "test-id";
        long testValue = 67;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureHighF(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_HIGH_F);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetTargetTemperatureHighF_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        long testValue = 67;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureHighF(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_HIGH_F);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }

    @Test
    public void testSetTargetTemperatureHighC_shouldSetCorrectValues() {
        String testId = "test-id";
        double testValue = 67.0;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureHighC(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_HIGH_C);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetTargetTemperatureHighC_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        double testValue = 67.0;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setTargetTemperatureHighC(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_TARGET_TEMP_HIGH_C);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }

    @Test
    public void testSetHVACMode_shouldSetCorrectValues() {
        String testId = "test-id";
        String testValue = "heat";

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setHVACMode(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_HVAC_MODE);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetHVACMode_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        String testValue = "heat-cool";

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setHVACMode(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_HVAC_MODE);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }

    @Test
    public void testSetFanTimerActive_shouldSetCorrectValues() {
        String testId = "test-id";
        boolean testValue = true;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setFanTimerActive(testId, testValue);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_FAN_TIMER_ACTIVE);
        Mockito.verify(mockFirebase).setValue(testValue);
    }

    @Test
    public void testSetFanTimerActive_withCallback_shouldSetCorrectValues() {
        String testId = "test-id";
        boolean testValue = true;

        ThermostatSetter setter = new ThermostatSetter(mockFirebase);
        setter.setFanTimerActive(testId, testValue, mockCallback);

        Mockito.verify(mockFirebase).child("/devices/thermostats/" + testId + "/"
                + Thermostat.KEY_FAN_TIMER_ACTIVE);
        Mockito.verify(mockFirebase).setValue(testValue, mockListener);
    }
}
