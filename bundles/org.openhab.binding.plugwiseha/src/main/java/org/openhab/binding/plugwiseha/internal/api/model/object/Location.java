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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAException;
import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;

/**
 * The {@link Location} class is an object model class that mirrors the XML
 * structure provided by the Plugwise Home Automation controller for a Plugwise
 * location/zone. It implements the {@link PlugwiseHAModel} interface.
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("location")
@NonNullByDefault
public class Location implements PlugwiseHAModel {

    @XStreamAsAttribute
    private @Nullable String id;

    private @Nullable String name;

    private @Nullable String type;

    private @Nullable String description;

    private @Nullable Appliances appliances;

    private @Nullable Logs logs;

    // Non-serializable fields

    private transient PlugwiseHAController controller;

    @XStreamAlias("actuator_functionalities")
    private @Nullable ActuatorFunctionalities actuatorFunctionalities;

    public Location(PlugwiseHAController controller) {
        this.controller = controller;
    }

    public @Nullable String getId() {
        return this.id;
    }

    public @Nullable String getName() {
        return this.name;
    }

    public @Nullable String getType() {
        return this.type;
    }

    public @Nullable String getDescription() {
        return this.description;
    }

    public @Nullable ActuatorFunctionalities getActuatorFunctionalities() {
        return this.actuatorFunctionalities;
    }

    public PlugwiseHAController getController() {
        return controller;
    }

    public void setController(PlugwiseHAController controller) {
        this.controller = controller;
    }

    @SuppressWarnings("null")
    public int applianceCount() {
        if (this.appliances == null) {
            return 0;
        } else {
            return this.appliances.size();
        }
    }

    @SuppressWarnings("null")
    public Optional<Double> getTemperature() {
        if (this.logs == null) {
            return Optional.empty();
        } else {
            return this.logs.getTemperature();
        }
    }

    @SuppressWarnings("null")
    public Optional<Double> getThermostatTemperature() {
        if (this.logs == null) {
            return Optional.empty();
        } else {
            return this.logs.getThermostatTemperature();
        }
    }

    public boolean isBatteryOperated() {
        return false;
    }

    @SuppressWarnings("null")
    public void setThermostatTemperature(Double temperature) throws PlugwiseHAException {
        if (this.actuatorFunctionalities != null) {
            ActuatorFunctionalityThermostat thermostat = (ActuatorFunctionalityThermostat) this.actuatorFunctionalities
                    .get("thermostat");

            if (thermostat != null) {
                this.controller.setThermostatTemperature(temperature, this, thermostat);
            }
        } else {
            throw new PlugwiseHAException(String.format("Location %s has no thermostat functionality: unable to change ThermostatTemperature", this.getName()));
        }
    }
}