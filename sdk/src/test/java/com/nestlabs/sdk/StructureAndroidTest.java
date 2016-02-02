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

import android.os.Build;
import android.os.Parcel;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import junit.framework.Assert;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class StructureAndroidTest {

    public static final String TEST_STRUCTURE_JSON = "/test-structure.json";
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testStructureToParcel() {
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_STRUCTURE_JSON), "utf-8");
            Structure structure = mapper.readValue(json, Structure.class);

            Parcel parcel = Parcel.obtain();
            structure.writeToParcel(parcel, 0);

            parcel.setDataPosition(0);

            Structure structureFromParcel = Structure.CREATOR.createFromParcel(parcel);
            assertEquals(structure, structureFromParcel);

            assertEquals(structureFromParcel.getStructureId(), "VqFabWH21nwVyd4RWgJgNb292wa7hG");
            assertEquals(structureFromParcel.getThermostats().size(), 1);
            assertEquals(structureFromParcel.getThermostats().get(0), "peyiJNo0IldT2YlIVtYaGQ");
            assertEquals(structureFromParcel.getSmokeCoAlarms().size(), 1);
            assertEquals(structureFromParcel.getSmokeCoAlarms().get(0),
                    "RTMTKxsQTCxzVcsySOHPxKoF4OyCifrs");
            assertEquals(structureFromParcel.getCameras().size(), 1);
            assertEquals(structureFromParcel.getCameras().get(0), "awJo6rHX");
            assertEquals(structureFromParcel.getDevices().size(), 1);

            assertEquals(structureFromParcel.getAway(), "home");
            assertEquals(structureFromParcel.getName(), "Home");
            assertEquals(structureFromParcel.getCountryCode(), "US");
            assertEquals(structureFromParcel.getPostalCode(), "94304");
            assertEquals(structureFromParcel.getPeakPeriodStartTime(), "2015-10-31T23:59:59.000Z");
            assertEquals(structureFromParcel.getPeakPeriodEndTime(), "2015-10-31T23:59:59.000Z");
            assertEquals(structureFromParcel.getTimeZone(), "America/Los_Angeles");
            assertNotNull(structureFromParcel.getEta());

            Structure.ETA eta = structureFromParcel.getEta();
            assertEquals(eta.getTripId(), "myTripHome1024");
            assertEquals(eta.getEstimatedArrivalWindowBegin(), "2015-10-31T22:42:59.000Z");
            assertEquals(eta.getEstimatedArrivalWindowEnd(), "2015-10-31T23:59:59.000Z");

            assertEquals(structureFromParcel.getRhrEnrollment(), true);

            assertNotNull(structureFromParcel.getWheres());
            assertEquals(structureFromParcel.getWheres().size(), 1);

            Structure.Where where = structureFromParcel.getWheres().get("Fqp6wJIX");
            assertNotNull(where);
            assertEquals(where.getWhereId(), "Fqp6wJIX");
            assertEquals(where.getName(), "Bedroom");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
