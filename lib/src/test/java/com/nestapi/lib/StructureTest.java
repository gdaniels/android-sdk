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
import java.util.LinkedHashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class StructureTest {
    public static final String TEST_STRUCTURE_JSON = "/test-structure.json";
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateThermostatWithJacksonMapper_shouldSetAllValuesCorrectly() {
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_STRUCTURE_JSON), "utf-8");
            Structure structure = mapper.readValue(json, Structure.class);

            assertEquals(structure.getStructureId(), "VqFabWH21nwVyd4RWgJgNb292wa7hG");
            assertEquals(structure.getThermostats().size(), 1);
            assertEquals(structure.getThermostats().get(0), "peyiJNo0IldT2YlIVtYaGQ");
            assertEquals(structure.getSmokeCoAlarms().size(), 1);
            assertEquals(structure.getSmokeCoAlarms().get(0), "RTMTKxsQTCxzVcsySOHPxKoF4OyCifrs");
            assertEquals(structure.getCameras().size(), 1);
            assertEquals(structure.getCameras().get(0), "awJo6rHX");
            assertEquals(structure.getDevices().size(), 1);

            assertEquals(structure.getAway(), "home");
            assertEquals(structure.getName(), "Home");
            assertEquals(structure.getCountryCode(), "US");
            assertEquals(structure.getPostalCode(), "94304");
            assertEquals(structure.getPeakPeriodStartTime(), "2015-10-31T23:59:59.000Z");
            assertEquals(structure.getPeakPeriodEndTime(), "2015-10-31T23:59:59.000Z");
            assertEquals(structure.getTimeZone(), "America/Los_Angeles");
            assertNotNull(structure.getEta());

            Structure.ETA eta = structure.getEta();
            assertEquals(eta.getTripId(), "myTripHome1024");
            assertEquals(eta.getEstimatedArrivalWindowBegin(), "2015-10-31T22:42:59.000Z");
            assertEquals(eta.getEstimatedArrivalWindowEnd(), "2015-10-31T23:59:59.000Z");

            assertEquals(structure.getRhrEnrollment(), true);

            assertNotNull(structure.getWheres());
            assertEquals(structure.getWheres().size(), 1);

            Structure.Where where = structure.getWheres().get("Fqp6wJIX");
            assertNotNull(where);
            assertEquals(where.getWhereId(), "Fqp6wJIX");
            assertEquals(where.getName(), "Bedroom");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testWhereNewArray_shouldReturnArrayOfCorrectSize() {
        int structureWheresSize = new Random().nextInt(9) + 1;

        Structure.Where[] wheres = Structure.Where.CREATOR.newArray(structureWheresSize);
        assertEquals(wheres.length, structureWheresSize);
    }

    @Test
    public void testETANewArray_shouldReturnArrayOfCorrectSize() {
        int structureEtasSize = new Random().nextInt(9) + 1;

        Structure.ETA[] etas = Structure.ETA.CREATOR.newArray(structureEtasSize);
        assertEquals(etas.length, structureEtasSize);
    }

    @Test
    public void testDescribeContents_shouldReturnZero() {
        Structure structure = new Structure();
        assertEquals(structure.describeContents(), 0);
    }

    @Test
    public void testEtaDescribeContents_shouldReturnZero() {
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_STRUCTURE_JSON), "utf-8");
            Structure structure = mapper.readValue(json, Structure.class);
            assertEquals(structure.getEta().describeContents(), 0);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testWhereDescribeContents_shouldReturnZero() {
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_STRUCTURE_JSON), "utf-8");
            Structure structure = mapper.readValue(json, Structure.class);
            LinkedHashMap<String, Structure.Where> wheres = structure.getWheres();
            for (String whereId : wheres.keySet()) {
                assertEquals(wheres.get(whereId).describeContents(), 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testNewArray_shouldReturnArrayOfCorrectSize() {
        int structuresSize = new Random().nextInt(9) + 1;

        Structure[] structures = Structure.CREATOR.newArray(structuresSize);
        assertEquals(structures.length, structuresSize);
    }

    @Test
    public void testEquals_shouldReturnFalseWithNonStructure() {
        Object o = new Object();
        Structure s = new Structure();
        assertFalse(s.equals(o));
    }
}
