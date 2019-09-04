/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.openhab.binding.plugwiseha.internal.api.model.object;

import java.util.Optional;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The {@link Logs} class is an object model class that
 * mirrors the XML structure provided by the Plugwise Home Automation
 * controller for the collection of logs.
 * It extends the {@link CustomCollection} class.
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("logs")
public class Logs extends CustomCollection<LogType> {

    private static String LOG_TEMPERATURE = "temperature";
    private static String LOG_THERMOSTAT = "thermostat";
    private static String LOG_BATTERY = "battery";
    private static String LOG_RELAY = "relay";
    private static String LOG_POWERUSAGE = "electricity_consumed";

    public Optional<LogTemperature> getLogTemperature() {
        return Optional.ofNullable((LogTemperature) this.get(Logs.LOG_TEMPERATURE));
    }

    public Optional<LogTemperature> getLogThermostat() {
        return Optional.ofNullable((LogTemperature) this.get(Logs.LOG_THERMOSTAT));
    }

    public Optional<LogBattery> getLogBattery() {
        return Optional.ofNullable((LogBattery) this.get(Logs.LOG_BATTERY));
    }

    public Optional<LogRelay> getLogRelay() {
        return Optional.ofNullable((LogRelay) this.get(Logs.LOG_RELAY));
    }

    public Optional<LogPowerUsage> getLogPowerUsage() {
        return Optional.ofNullable((LogPowerUsage) this.get(Logs.LOG_POWERUSAGE));
    }

    public Optional<Double> getTemperature() {
        return this.getLogTemperature().map(logTemperature -> logTemperature.getTemperature()).orElse(Optional.empty());
    }

    public Optional<Double> getThermostatTemperature() {
        return this.getLogThermostat().map(logTemperature -> logTemperature.getTemperature()).orElse(Optional.empty());
    }

    public Optional<Double> getBatteryLevel() {
        return this.getLogBattery().map(logBattery -> logBattery.getBatteryLevel()).orElse(Optional.empty());
    }

    public Optional<String> getRelayState() {
        return this.getLogRelay().map(logRelay -> logRelay.getRelayState()).orElse(Optional.empty());
    }

    public Optional<Double> getPowerUsage() {
        return this.getLogPowerUsage().map(logPowerUsage -> logPowerUsage.getPowerUsage()).orElse(Optional.empty());
    }
}