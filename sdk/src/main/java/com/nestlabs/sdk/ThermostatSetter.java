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

import android.support.annotation.NonNull;

import com.firebase.client.Firebase;

/**
 * ThermostatSetter provides methods for setting values on {@link Thermostat}s.
 */
public final class ThermostatSetter {
    private final Firebase mFirebaseRef;

    private static String getPath(@NonNull String thermostatId, @NonNull String attribute) {
        return new Utils.PathBuilder()
                .append(NestAPI.KEY_DEVICES)
                .append(NestAPI.KEY_THERMOSTATS)
                .append(thermostatId)
                .append(attribute).build();
    }

    ThermostatSetter(final Firebase firebaseRef) {
        mFirebaseRef = firebaseRef;
    }

    /**
     * Sets the desired temperature, in full degrees Fahrenheit (1&deg;F). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature in full degrees Fahrenheit.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureF(@NonNull String thermostatId, long temperature,
            @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_F);
        mFirebaseRef.child(path).setValue(temperature, new NestCompletionListener(callback));
    }

    /**
     * Sets the desired temperature, in full degrees Fahrenheit (1&deg;F). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature in full degrees Fahrenheit.
     */
    public void setTargetTemperatureF(@NonNull String thermostatId, long temperature) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_F);
        mFirebaseRef.child(path).setValue(temperature);
    }

    /**
     * Sets the desired temperature, in half degrees Celsius (0.5&deg;C). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature, in half degrees Celsius (0.5&deg;C).
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureC(@NonNull String thermostatId, double temperature,
            @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_C);
        mFirebaseRef.child(path).setValue(temperature, new NestCompletionListener(callback));
    }

    /**
     * Sets the desired temperature, in half degrees Celsius (0.5&deg;C). Used when hvac_mode =
     * "heat" or "cool".
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The desired temperature, in half degrees Celsius (0.5&deg;C).
     */
    public void setTargetTemperatureC(@NonNull String thermostatId, double temperature) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_C);
        mFirebaseRef.child(path).setValue(temperature);
    }

    /**
     * Sets the minimum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum desired temperature, displayed in whole degrees Fahrenheit.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureLowF(@NonNull String thermostatId, long temperature,
            @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_LOW_F);
        mFirebaseRef.child(path).setValue(temperature, new NestCompletionListener(callback));
    }

    /**
     * Sets the minimum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum desired temperature, displayed in whole degrees Fahrenheit.
     */
    public void setTargetTemperatureLowF(@NonNull String thermostatId, long temperature) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_LOW_F);
        mFirebaseRef.child(path).setValue(temperature);
    }

    /**
     * Sets the minimum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum target temperature, displayed in half degrees Celsius.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureLowC(@NonNull String thermostatId, double temperature,
            @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_LOW_C);
        mFirebaseRef.child(path).setValue(temperature, new NestCompletionListener(callback));
    }

    /**
     * Sets the minimum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The minimum target temperature, displayed in half degrees Celsius.
     */
    public void setTargetTemperatureLowC(@NonNull String thermostatId, double temperature) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_LOW_C);
        mFirebaseRef.child(path).setValue(temperature);
    }

    /**
     * Sets the maximum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum desired temperature, displayed in whole degrees Fahrenheit.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureHighF(@NonNull String thermostatId, long temperature,
            @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_HIGH_F);
        mFirebaseRef.child(path).setValue(temperature, new NestCompletionListener(callback));
    }

    /**
     * Sets the maximum target temperature, displayed in whole degrees Fahrenheit (1&deg;F). Used
     * when hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum desired temperature, displayed in whole degrees Fahrenheit.
     */
    public void setTargetTemperatureHighF(@NonNull String thermostatId, long temperature) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_HIGH_F);
        mFirebaseRef.child(path).setValue(temperature);
    }

    /**
     * Sets the maximum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum target temperature, displayed in half degrees Celsius.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setTargetTemperatureHighC(@NonNull String thermostatId, double temperature,
            @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_HIGH_C);
        mFirebaseRef.child(path).setValue(temperature, new NestCompletionListener(callback));
    }

    /**
     * Sets the maximum target temperature, displayed in half degrees Celsius (0.5&deg;C). Used when
     * hvac_mode = "heat-cool" (Heat / Cool mode).
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param temperature  The maximum target temperature, displayed in half degrees Celsius.
     */
    public void setTargetTemperatureHighC(@NonNull String thermostatId, double temperature) {
        String path = getPath(thermostatId, Thermostat.KEY_TARGET_TEMP_HIGH_C);
        mFirebaseRef.child(path).setValue(temperature);
    }

    /**
     * Sets the HVAC system heating/cooling modes. For systems with both heating and cooling
     * capability, set this value to "heat-cool" (Heat / Cool mode) to get the best experience.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param mode         The heating/cooling mode. Values can be "heat", "cool", "heat-cool", or
     *                     "off".
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setHVACMode(@NonNull String thermostatId, String mode, @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_HVAC_MODE);
        mFirebaseRef.child(path).setValue(mode, new NestCompletionListener(callback));
    }

    /**
     * Sets the HVAC system heating/cooling modes. For systems with both heating and cooling
     * capability, set this value to "heat-cool" (Heat / Cool mode) to get the best experience.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param mode         The heating/cooling mode. Values can be "heat", "cool", "heat-cool", or
     *                     "off".
     */
    public void setHVACMode(@NonNull String thermostatId, String mode) {
        String path = getPath(thermostatId, Thermostat.KEY_HVAC_MODE);
        mFirebaseRef.child(path).setValue(mode);
    }

    /**
     * Sets whether the fan timer is engaged; used with fanTimerTimeout to turn on the fan for a
     * (user-specified) preset duration.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param isActive     true if the fan timer is to be engaged, false if the fan timer should be
     *                     disengaged.
     * @param callback     A {@link Callback} to receive whether the change was successful.
     */
    public void setFanTimerActive(@NonNull String thermostatId, boolean isActive,
            @NonNull Callback callback) {
        String path = getPath(thermostatId, Thermostat.KEY_FAN_TIMER_ACTIVE);
        mFirebaseRef.child(path).setValue(isActive, new NestCompletionListener(callback));
    }

    /**
     * Sets whether the fan timer is engaged; used with fanTimerTimeout to turn on the fan for a
     * (user-specified) preset duration.
     *
     * @param thermostatId The unique identifier for the {@link Thermostat}.
     * @param isActive     true if the fan timer is to be engaged, false if the fan timer should be
     *                     disengaged.
     */
    public void setFanTimerActive(@NonNull String thermostatId, boolean isActive) {
        String path = getPath(thermostatId, Thermostat.KEY_FAN_TIMER_ACTIVE);
        mFirebaseRef.child(path).setValue(isActive);
    }
}
