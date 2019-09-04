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

/**
 * The {@link LogPowerUsage} class is an object model class that mirrors the XML
 * structure provided by the Plugwise Home Automation controller for a specific
 * log entry type. ('electricity_consumed') It implements the {@link LogType}
 * interface.
 * 
 * @author B. van Wetten - Initial contribution
 */
public class LogPowerUsage implements LogType {

    private String type;

    private LogEntryPeriod period;

    public String getType() {
        return this.type;
    }

    public Optional<Double> getPowerUsage() {
        String powerUsage = this.period.getMeasurement().map(measurement -> measurement).orElse(null);

        try {
            if (powerUsage != null) {
                return Optional.of(Double.parseDouble(powerUsage));
            } else {
                return Optional.empty();
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
