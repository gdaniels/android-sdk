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
import android.content.Intent;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class NestAuthActivityTest {

    @Test
    public void testLaunchActivityWithoutIntent_shouldFinishWithNullResult() throws Exception {
        NestAuthActivity a = Robolectric.buildActivity(NestAuthActivity.class).create().get();
        ShadowActivity shadow = (ShadowActivity) ShadowExtractor.extract(a);

        assertEquals(shadow.getResultCode(), Activity.RESULT_CANCELED);

        NestToken token = shadow.getResultIntent().getExtras().getParcelable("access_token_key");
        assertEquals(token, null);
    }

    @Test
    public void testLaunchActivityWithInvalidIntent_shouldFinishWithNullResult() throws Exception {
        Intent intent = new Intent();
        NestConfig config = new NestConfig.Builder()
                .clientID("")
                .clientSecret("")
                .redirectURL("")
                .build();
        intent.putExtra("client_metadata_key", config);
        NestAuthActivity a = Robolectric
                .buildActivity(NestAuthActivity.class)
                .withIntent(intent)
                .create()
                .get();
        ShadowActivity shadow = (ShadowActivity) ShadowExtractor.extract(a);

        assertEquals(shadow.getResultCode(), Activity.RESULT_CANCELED);

        NestToken token = shadow.getResultIntent().getExtras().getParcelable("access_token_key");
        assertEquals(token, null);
    }
}
