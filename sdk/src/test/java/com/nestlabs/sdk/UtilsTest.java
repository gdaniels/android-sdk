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

import android.os.Parcel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Parcel.class})
public class UtilsTest {

    @Test
    public void testParcelReadBoolean_shouldReturnTrueForIntOne() {
        Parcel mockParcel = PowerMockito.mock(Parcel.class);
        PowerMockito.when(mockParcel.readInt()).thenReturn(1);

        boolean result = Utils.readBoolean(mockParcel);
        assertTrue(result);
    }

    @Test
    public void testParcelReadBoolean_shouldReturnFalseForIntZero() {
        Parcel mockParcel = PowerMockito.mock(Parcel.class);
        PowerMockito.when(mockParcel.readInt()).thenReturn(0);

        boolean result = Utils.readBoolean(mockParcel);
        assertFalse(result);
    }

    @Test
    public void testParcelWriteBoolean_shouldWriteOneWhenInputTrue() {
        Parcel mockParcel = PowerMockito.mock(Parcel.class);
        Utils.writeBoolean(mockParcel, true);
        Mockito.verify(mockParcel).writeInt(1);
    }

    @Test
    public void testParcelWriteBoolean_shouldWriteZeroWhenInputFalse() {
        Parcel mockParcel = PowerMockito.mock(Parcel.class);
        Utils.writeBoolean(mockParcel, false);
        Mockito.verify(mockParcel).writeInt(0);
    }

    @Test
    public void testPathBuilderWithOneString_shouldHaveLeadingSlash() {
        Utils.PathBuilder builder = new Utils.PathBuilder();
        builder.append("hello");
        String result = builder.build();

        Assert.assertEquals("/hello", result);
    }

    @Test
    public void testPathBuilderWithTwoStrings_shouldJoinWithSlashes() {
        Utils.PathBuilder builder = new Utils.PathBuilder();
        builder.append("hello").append("world");
        String result = builder.build();

        Assert.assertEquals("/hello/world", result);
    }
}
