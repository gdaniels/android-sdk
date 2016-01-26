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

public class MetadataTest {

    public static final String TEST_METADATA_JSON = "/test-metadata.json";
    public static final String TEST_EMPTY_METADATA_JSON = "/test-empty-metadata.json";
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateMetadataWithJacksonMapper_shouldSetAllValuesCorrectly() {
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_METADATA_JSON), "utf-8").trim();
            Metadata metadata = mapper.readValue(json, Metadata.class);

            assertEquals(metadata.getAccessToken(), "c.FmDPkzyzaQeX");
            assertEquals(metadata.getClientVersion(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDescribeContents_shouldReturnZero() {
        Metadata metadata = new Metadata();
        assertEquals(metadata.describeContents(), 0);
    }

    @Test
    public void testNewArray_shouldReturnArrayOfCorrectSize() {
        int metadatasSize = new Random().nextInt(9) + 1;

        Metadata[] metadatas = Metadata.CREATOR.newArray(metadatasSize);
        assertEquals(metadatas.length, metadatasSize);
    }

    @Test
    public void testEquals_shouldReturnFalseWithNonMetadata() {
        Object o = new Object();
        Metadata metadata = new Metadata();
        assertFalse(metadata.equals(o));
    }

    @Test
    public void testToString_shouldReturnNicelyFormattedString() {
        Metadata metadata = new Metadata();
        try {
            String json = IOUtils.toString(
                    this.getClass().getResourceAsStream(TEST_EMPTY_METADATA_JSON), "utf-8").trim();
            assertEquals(json, metadata.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
