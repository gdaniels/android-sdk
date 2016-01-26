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

public class ThermostatTest {

    public static final String TEST_THERMOSTAT_JSON = "/test-thermostat.json";
    public static final String TEST_EMPTY_THERMOSTAT = "/test-empty-thermostat.json";
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateThermostatWithJacksonMapper_shouldSetAllValuesCorrectly() {
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_THERMOSTAT_JSON), "utf-8");
            Thermostat thermostat = mapper.readValue(json, Thermostat.class);

            assertEquals(thermostat.getDeviceId(), "peyiJNo0IldT2YlIVtYaGQ");
            assertEquals(thermostat.getSoftwareVersion(), "4.0");
            assertEquals(thermostat.getStructureId(), "VqFabWH21nwVyd4R...");
            assertEquals(thermostat.getWhereId(), "UNCBGUnN24");
            assertEquals(thermostat.getName(), "Hallway (upstairs)");
            assertEquals(thermostat.getNameLong(), "Hallway Thermostat (upstairs)");
            assertEquals(thermostat.isOnline(), true);

            assertEquals(thermostat.getLocale(), "en-US");
            assertEquals(thermostat.getLastConnection(), "2015-10-31T23:59:59.000Z");
            assertEquals(thermostat.getCanCool(), true);
            assertEquals(thermostat.getCanHeat(), true);
            assertEquals(thermostat.isUsingEmergencyHeat(), true);
            assertEquals(thermostat.getHasFan(), true);
            assertEquals(thermostat.getFanTimerActive(), true);
            assertEquals(thermostat.getFanTimerTimeout(), "2015-10-31T23:59:59.000Z");
            assertEquals(thermostat.getHasLeaf(), true);
            assertEquals(thermostat.getTemperatureScale(), "C");
            assertEquals(thermostat.getTargetTemperatureF(), 72);
            assertEquals(thermostat.getTargetTemperatureC(), 21.5, 0.01);
            assertEquals(thermostat.getTargetTemperatureHighF(), 72);
            assertEquals(thermostat.getTargetTemperatureHighC(), 21.5, 0.01);
            assertEquals(thermostat.getTargetTemperatureLowF(), 64);
            assertEquals(thermostat.getTargetTemperatureLowC(), 17.5, 0.01);
            assertEquals(thermostat.getAwayTemperatureHighF(), 72);
            assertEquals(thermostat.getAwayTemperatureHighC(), 21.5, 0.01);
            assertEquals(thermostat.getAwayTemperatureLowF(), 64);
            assertEquals(thermostat.getAwayTemperatureLowC(), 17.5, 0.01);
            assertEquals(thermostat.getHvacMode(), "heat");
            assertEquals(thermostat.getAmbientTemperatureF(), 72);
            assertEquals(thermostat.getAmbientTemperatureC(), 21.5, 0.01);
            assertEquals(thermostat.getHumidity(), 40);
            assertEquals(thermostat.getHvacState(), "heating");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDescribeContents_shouldReturnZero() {
        Thermostat t = new Thermostat();
        assertEquals(t.describeContents(), 0);
    }

    @Test
    public void testNewArray_shouldReturnArrayOfCorrectSize() {
        int thermostatsSize = new Random().nextInt(9) + 1;

        Thermostat[] thermostats = Thermostat.CREATOR.newArray(thermostatsSize);
        assertEquals(thermostats.length, thermostatsSize);
    }

    @Test
    public void testEquals_shouldReturnFalseWithNonThermostat() {
        Object o = new Object();
        Thermostat t = new Thermostat();
        assertFalse(t.equals(o));
    }

    @Test
    public void testToString_shouldReturnNicelyFormattedString() {
        Thermostat t = new Thermostat();
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
