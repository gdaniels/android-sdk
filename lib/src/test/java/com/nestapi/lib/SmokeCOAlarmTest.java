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

package com.nestapi.lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SmokeCOAlarmTest {

    public static final String TEST_SMOKEALARM_JSON = "/test-smoke-alarm.json";
    public static final String TEST_EMPTY_THERMOSTAT = "/test-empty-smoke-alarm.json";
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateSmokeAlarmWithJacksonMapper_shouldSetAllValuesCorrectly() {
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_SMOKEALARM_JSON), "utf-8");
            SmokeCOAlarm smokeCOAlarm = mapper.readValue(json, SmokeCOAlarm.class);

            assertEquals(smokeCOAlarm.getDeviceId(), "RTMTKxsQTCxzVcsySOHPxKoF4OyCifrs");
            assertEquals(smokeCOAlarm.getSoftwareVersion(), "1.01");
            assertEquals(smokeCOAlarm.getStructureId(), "VqFabWH21nwVyd4RWgJgNb292wa7hG");
            assertEquals(smokeCOAlarm.getWhereId(), "UNCBGUnN24");
            assertEquals(smokeCOAlarm.getName(), "Hallway (upstairs)");
            assertEquals(smokeCOAlarm.getNameLong(), "Hallway Protect (upstairs)");
            assertEquals(smokeCOAlarm.isOnline(), true);
            assertEquals(smokeCOAlarm.getWhereId(), "UNCBGUnN24");
            assertEquals(smokeCOAlarm.getLocale(), "en-US");

            assertEquals(smokeCOAlarm.getBatteryHealth(), "ok");
            assertEquals(smokeCOAlarm.getCOAlarmState(), "ok");
            assertEquals(smokeCOAlarm.getSmokeAlarmState(), "ok");
            assertEquals(smokeCOAlarm.getUIColorState(), "gray");
            assertEquals(smokeCOAlarm.getIsManualTestActive(), true);
            assertEquals(smokeCOAlarm.getLastManualTestTime(), "2015-10-31T23:59:59.000Z");

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDescribeContents_shouldReturnZero() {
        SmokeCOAlarm smokeCOAlarm = new SmokeCOAlarm();
        assertEquals(smokeCOAlarm.describeContents(), 0);
    }

    @Test
    public void testNewArray_shouldReturnArrayOfCorrectSize() {
        int smokealarmsSize = new Random().nextInt(9) + 1;

        SmokeCOAlarm[] smokeCOAlarms = SmokeCOAlarm.CREATOR.newArray(smokealarmsSize);
        assertEquals(smokeCOAlarms.length, smokealarmsSize);
    }

    @Test
    public void testEquals_shouldReturnFalseWithNonSmokeCoAlarm() {
        Object o = new Object();
        SmokeCOAlarm t = new SmokeCOAlarm();
        assertFalse(t.equals(o));
    }

    @Test
    public void testToString_shouldReturnNicelyFormattedString() {
        SmokeCOAlarm t = new SmokeCOAlarm();
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_EMPTY_THERMOSTAT), "utf-8").trim();
            assertEquals(json, t.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
