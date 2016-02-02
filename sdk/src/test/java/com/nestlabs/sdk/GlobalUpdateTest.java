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

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertSame;

public class GlobalUpdateTest {

    @Test
    public void testNestGlobalUpdate_shouldReturnSameObjectInGetters() {
        ArrayList<Thermostat> testThermos = new ArrayList<>();
        ArrayList<SmokeCOAlarm> testSmokeAlarms = new ArrayList<>();
        ArrayList<Camera> testCams = new ArrayList<>();
        ArrayList<Structure> testStructures = new ArrayList<>();
        Metadata testMetadata = new Metadata();

        GlobalUpdate update = new GlobalUpdate(testThermos, testSmokeAlarms, testCams,
                testStructures, testMetadata);

        assertSame(testThermos, update.getThermostats());
        assertSame(testSmokeAlarms, update.getSmokeCOAlarms());
        assertSame(testCams, update.getCameras());
        assertSame(testStructures, update.getStructures());
        assertSame(testMetadata, update.getMetadata());
    }
}
