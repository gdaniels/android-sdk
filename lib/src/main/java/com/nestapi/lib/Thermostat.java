/*
 * Copyright 2015, Google Inc.
 * Copyright 2014, Nest Labs Inc.
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

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import static com.nestapi.lib.Utils.readBoolean;

/**
 * Thermostat represents and contains all properties of a Nest Thermostat.
 */
@JsonPropertyOrder(alphabetic = true)
public class Thermostat extends Device implements Parcelable {
    public static final String KEY_CAN_COOL = "can_cool";
    public static final String KEY_CAN_HEAT = "can_heat";
    public static final String KEY_IS_USING_EMERGENCY_HEAT = "is_using_emergency_heat";
    public static final String KEY_HAS_FAN = "has_fan";
    public static final String KEY_FAN_TIMER_ACTIVE = "fan_timer_active";
    public static final String KEY_FAN_TIMER_TIMEOUT = "fan_timer_timeout";
    public static final String KEY_HAS_LEAF = "has_leaf";
    public static final String KEY_TEMP_SCALE = "temperature_scale";
    public static final String KEY_TARGET_TEMP_F = "target_temperature_f";
    public static final String KEY_TARGET_TEMP_C = "target_temperature_c";
    public static final String KEY_TARGET_TEMP_HIGH_F = "target_temperature_high_f";
    public static final String KEY_TARGET_TEMP_HIGH_C = "target_temperature_high_c";
    public static final String KEY_TARGET_TEMP_LOW_F = "target_temperature_low_f";
    public static final String KEY_TARGET_TEMP_LOW_C = "target_temperature_low_c";
    public static final String KEY_AWAY_TEMP_HIGH_F = "away_temperature_high_f";
    public static final String KEY_AWAY_TEMP_HIGH_C = "away_temperature_high_c";
    public static final String KEY_AWAY_TEMP_LOW_F = "away_temperature_low_f";
    public static final String KEY_AWAY_TEMP_LOW_C = "away_temperature_low_c";
    public static final String KEY_HVAC_MODE = "hvac_mode";
    public static final String KEY_AMBIENT_TEMP_F = "ambient_temperature_f";
    public static final String KEY_AMBIENT_TEMP_C = "ambient_temperature_c";
    public static final String KEY_HUMIDITY = "humidity";
    public static final String KEY_HVAC_STATE = "hvac_state";

    @JsonProperty(KEY_CAN_COOL)
    private boolean mCanCool;

    @JsonProperty(KEY_CAN_HEAT)
    private boolean mCanHeat;

    @JsonProperty(KEY_IS_USING_EMERGENCY_HEAT)
    private boolean mIsUsingEmergencyHeat;

    @JsonProperty(KEY_HAS_FAN)
    private boolean mHasFan;

    @JsonProperty(KEY_FAN_TIMER_ACTIVE)
    private boolean mFanTimerActive;

    @JsonProperty(KEY_FAN_TIMER_TIMEOUT)
    private String mFanTimerTimeout;

    @JsonProperty(KEY_HAS_LEAF)
    private boolean mHasLeaf;

    @JsonProperty(KEY_TEMP_SCALE)
    private String mTemperatureScale;

    @JsonProperty(KEY_TARGET_TEMP_F)
    private long mTargetTemperatureF;

    @JsonProperty(KEY_TARGET_TEMP_C)
    private double mTargetTemperatureC;

    @JsonProperty(KEY_TARGET_TEMP_HIGH_F)
    private long mTargetTemperatureHighF;

    @JsonProperty(KEY_TARGET_TEMP_HIGH_C)
    private double mTargetTemperatureHighC;

    @JsonProperty(KEY_TARGET_TEMP_LOW_F)
    private long mTargetTemperatureLowF;

    @JsonProperty(KEY_TARGET_TEMP_LOW_C)
    private double mTargetTemperatureLowC;

    @JsonProperty(KEY_AWAY_TEMP_HIGH_F)
    private long mAwayTemperatureHighF;

    @JsonProperty(KEY_AWAY_TEMP_HIGH_C)
    private double mAwayTemperatureHighC;

    @JsonProperty(KEY_AWAY_TEMP_LOW_F)
    private long mAwayTemperatureLowF;

    @JsonProperty(KEY_AWAY_TEMP_LOW_C)
    private double mAwayTemperatureLowC;

    @JsonProperty(KEY_HVAC_MODE)
    private String mHvacMode;

    @JsonProperty(KEY_AMBIENT_TEMP_F)
    private long mAmbientTemperatureF;

    @JsonProperty(KEY_AMBIENT_TEMP_C)
    private double mAmbientTemperatureC;

    @JsonProperty(KEY_HUMIDITY)
    private long mHumidity;

    @JsonProperty(KEY_HVAC_STATE)
    private String mHvacState;

    public Thermostat() {}

    private Thermostat(Parcel in) {
        super(in);
        mCanCool = readBoolean(in);
        mCanHeat = readBoolean(in);
        mIsUsingEmergencyHeat = readBoolean(in);
        mHasFan = readBoolean(in);
        mFanTimerActive = readBoolean(in);
        mFanTimerTimeout = in.readString();
        mHasLeaf = readBoolean(in);
        mTemperatureScale = in.readString();
        mTargetTemperatureF = in.readLong();
        mTargetTemperatureC = in.readDouble();
        mTargetTemperatureHighF = in.readLong();
        mTargetTemperatureHighC = in.readDouble();
        mTargetTemperatureLowF = in.readLong();
        mTargetTemperatureLowC = in.readDouble();
        mAwayTemperatureHighF = in.readLong();
        mAwayTemperatureHighC = in.readDouble();
        mAwayTemperatureLowF = in.readLong();
        mAwayTemperatureLowC = in.readDouble();
        mHvacMode = in.readString();
        mAmbientTemperatureF = in.readLong();
        mAmbientTemperatureC = in.readDouble();
        mHumidity = in.readLong();
        mHvacState = in.readString();
    }

    /**
     * Returns whether HVAC system is actively heating, cooling or is off.
     * <p/>
     * Values: "heating", "cooling", "off"
     *
     * @return whether HVAC system is actively heating, cooling or is off.
     */
    @JsonGetter(KEY_HVAC_STATE)
    public String getHvacState() {
        return mHvacState;
    }

    /**
     * Returns the humidity, in percent (%) format, measured at the device.
     *
     * @return the humidity, in percent (%) format, measured at the device.
     */
    @JsonGetter(KEY_HUMIDITY)
    public long getHumidity() {
        return mHumidity;
    }

    /**
     * Returns true if this thermostat is connected to a cooling system.
     */
    @JsonGetter(KEY_CAN_COOL)
    public boolean getCanCool() {
        return mCanCool;
    }

    /**
     * Returns true if this thermostat is connected to a heating system.
     */
    @JsonGetter(KEY_CAN_HEAT)
    public boolean getCanHeat() {
        return mCanHeat;
    }

    /**
     * Returns true if this thermostat is currently operating using the emergency heating system.
     */
    @JsonGetter(KEY_IS_USING_EMERGENCY_HEAT)
    public boolean isUsingEmergencyHeat() {
        return mIsUsingEmergencyHeat;
    }

    /**
     * Returns true if this thermostat has a connected fan.
     */
    @JsonGetter(KEY_HAS_FAN)
    public boolean getHasFan() {
        return mHasFan;
    }

    /**
     * If the fan is running on a timer, this provides the timestamp (in ISO-8601 format) at which
     * the fan will stop running.
     */
    @JsonGetter(KEY_FAN_TIMER_TIMEOUT)
    public String getFanTimerTimeout() {
        return mFanTimerTimeout;
    }

    /**
     * Returns true if the thermostat is currently displaying the leaf indicator.
     */
    @JsonGetter(KEY_HAS_LEAF)
    public boolean getHasLeaf() {
        return mHasLeaf;
    }

    /**
     * Returns the temperature scale: one of "C" (Celsius) or "F" (Fahrenheit) that this thermostat
     * should display temperatures in.
     */
    @JsonGetter(KEY_TEMP_SCALE)
    public String getTemperatureScale() {
        return mTemperatureScale;
    }

    /**
     * Returns the temperature (in Fahrenheit) at which the cooling system will engage when in
     * "Away" state.
     */
    @JsonGetter(KEY_AWAY_TEMP_HIGH_F)
    public long getAwayTemperatureHighF() {
        return mAwayTemperatureHighF;
    }

    /**
     * Returns the temperature (in Celsius) at which the cooling system will engage when in "Away"
     * state.
     */
    @JsonGetter(KEY_AWAY_TEMP_HIGH_C)
    public double getAwayTemperatureHighC() {
        return mAwayTemperatureHighC;
    }

    /**
     * Returns the temperature (in Fahrenheit) at which the heating system will engage when in
     * "Away" state.
     */
    @JsonGetter(KEY_AWAY_TEMP_LOW_F)
    public long getAwayTemperatureLowF() {
        return mAwayTemperatureLowF;
    }

    /**
     * Returns the temperature (in Celsius) at which the heating system will engage when in "Away"
     * state.
     */
    @JsonGetter(KEY_AWAY_TEMP_LOW_C)
    public double getAwayTemperatureLowC() {
        return mAwayTemperatureLowC;
    }

    /**
     * Returns the current ambient temperature in the structure in Fahrenheit.
     */
    @JsonGetter(KEY_AMBIENT_TEMP_F)
    public long getAmbientTemperatureF() {
        return mAmbientTemperatureF;
    }

    /**
     * Returns the current ambient temperature in the structure in Celsius.
     */
    @JsonGetter(KEY_AMBIENT_TEMP_C)
    public double getAmbientTemperatureC() {
        return mAmbientTemperatureC;
    }

    /**
     * Returns true if the fan is currently running on a timer.
     */
    @JsonGetter(KEY_FAN_TIMER_ACTIVE)
    public boolean getFanTimerActive() {
        return mFanTimerActive;
    }

    /**
     * Returns the target temperature of the thermostat in Fahrenheit. Note that this is only
     * applicable when in Heat or Cool mode, not "Heat and Cool" mode.
     */
    @JsonGetter(KEY_TARGET_TEMP_F)
    public long getTargetTemperatureF() {
        return mTargetTemperatureF;
    }

    /**
     * Returns the target temperature of the thermostat in Celsius. Note that this is only
     * applicable when in Heat or Cool mode, not "Heat and Cool" mode.
     */
    @JsonGetter(KEY_TARGET_TEMP_C)
    public double getTargetTemperatureC() {
        return mTargetTemperatureC;
    }

    /**
     * Returns the target temperature of the cooling system in Fahrenheit when in "Heat and Cool"
     * mode.
     */
    @JsonGetter(KEY_TARGET_TEMP_HIGH_F)
    public long getTargetTemperatureHighF() {
        return mTargetTemperatureHighF;
    }

    /**
     * Returns the target temperature of the cooling system in Celsius when in "Heat and Cool"
     * mode.
     */
    @JsonGetter(KEY_TARGET_TEMP_HIGH_C)
    public double getTargetTemperatureHighC() {
        return mTargetTemperatureHighC;
    }

    /**
     * Returns the target temperature of the heating system in Celsius when in "Heat and Cool"
     * mode.
     */
    @JsonGetter(KEY_TARGET_TEMP_LOW_F)
    public long getTargetTemperatureLowF() {
        return mTargetTemperatureLowF;
    }

    /**
     * Returns the target temperature of the heating system in Fahrenheit when in "Heat and Cool"
     * mode.
     */
    @JsonGetter(KEY_TARGET_TEMP_LOW_C)
    public double getTargetTemperatureLowC() {
        return mTargetTemperatureLowC;
    }

    /**
     * Returns the current operating mode of the thermostat.
     */
    @JsonGetter(KEY_HVAC_MODE)
    public String getHvacMode() {
        return mHvacMode;
    }

    public static final Creator<Thermostat> CREATOR = new Creator<Thermostat>() {
        @Override
        public Thermostat createFromParcel(Parcel in) {
            return new Thermostat(in);
        }

        @Override
        public Thermostat[] newArray(int size) {
            return new Thermostat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Thermostat) {
            Thermostat t = (Thermostat) obj;
            return t.toString().equals(this.toString());
        } else {
            return false;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        Utils.writeBoolean(dest, mCanCool);
        Utils.writeBoolean(dest, mCanHeat);
        Utils.writeBoolean(dest, mIsUsingEmergencyHeat);
        Utils.writeBoolean(dest, mHasFan);
        Utils.writeBoolean(dest, mFanTimerActive);
        dest.writeString(mFanTimerTimeout);
        Utils.writeBoolean(dest, mHasLeaf);
        dest.writeString(mTemperatureScale);
        dest.writeLong(mTargetTemperatureF);
        dest.writeDouble(mTargetTemperatureC);
        dest.writeLong(mTargetTemperatureHighF);
        dest.writeDouble(mTargetTemperatureHighC);
        dest.writeLong(mTargetTemperatureLowF);
        dest.writeDouble(mTargetTemperatureLowC);
        dest.writeLong(mAwayTemperatureHighF);
        dest.writeDouble(mAwayTemperatureHighC);
        dest.writeLong(mAwayTemperatureLowF);
        dest.writeDouble(mAwayTemperatureLowC);
        dest.writeString(mHvacMode);
        dest.writeLong(mAmbientTemperatureF);
        dest.writeDouble(mAmbientTemperatureC);
        dest.writeLong(mHumidity);
        dest.writeString(mHvacState);
    }
}
