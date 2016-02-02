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

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DeviceTest {
    @Test
    public void testDescribeContents_shouldReturnZero() {
        Device device = new Device();
        assertEquals(device.describeContents(), 0);
    }

    @Test
    public void testNewArray_shouldReturnArrayOfCorrectSize() {
        int devicesSize = new Random().nextInt(9) + 1;

        Device[] devices = Device.CREATOR.newArray(devicesSize);
        assertEquals(devices.length, devicesSize);
    }

    @Test
    public void testEquals_shouldReturnFalseWithNonNestDevice() {
        Object o = new Object();
        Device device = new Device();
        assertFalse(device.equals(o));
    }
}
