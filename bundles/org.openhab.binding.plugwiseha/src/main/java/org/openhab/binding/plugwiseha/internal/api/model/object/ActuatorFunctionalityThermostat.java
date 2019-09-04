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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The {@link ActuatorFunctionalityThermostat} class is an object model class
 * that mirrors the XML structure provided by the Plugwise Home Automation
 * controller for the 'thermostat' actuator functionality. It implements the
 * {@link ActuatorFunctionalityType} interface.
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("thermostat_functionality")
public class ActuatorFunctionalityThermostat implements ActuatorFunctionalityType {

    @XStreamAsAttribute
    private String id;

    private String type;

    private String setpoint;

    private String lower_bound;

    private String upper_bound;

    private String resolution;

    @XStreamAlias("updated_date")
    private String updatedDate;

    public ActuatorFunctionalityThermostat(Double setpointTemperature) {
        this.setpoint = setpointTemperature.toString();
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getSetPoint() {
        return this.setpoint;
    }

    public String getLowerBound() {
        return this.lower_bound;
    }

    public String getUpperBound() {
        return this.upper_bound;
    }

    public String getResolution() {
        return this.resolution;
    }
}
