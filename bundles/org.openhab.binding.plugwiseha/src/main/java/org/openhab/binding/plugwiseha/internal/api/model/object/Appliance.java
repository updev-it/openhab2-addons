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

import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAException;
import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;

/**
 * The {@link Appliance} class is an object model class that
 * mirrors the XML structure provided by the Plugwise Home Automation
 * controller for a Plugwise appliance.
 * It implements the {@link PlugwiseHAModel} interface.
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("appliance")
public class Appliance implements PlugwiseHAModel {

    @XStreamAsAttribute
    private String id;

    private String name;

    private String description;

    private String type;

    private Logs logs;

    @XStreamAlias("actuator_functionalities")
    private ActuatorFunctionalities actuatorFunctionalities;

    @XStreamAlias("created_date")
    private String createdDate;

    @XStreamAlias("modified_date")
    private String modifiedDate;

    @XStreamAlias("deleted_date")
    private String deletedDate;

    // Non-serializable fields

    private transient PlugwiseHAController controller;

    public Appliance(PlugwiseHAController controller) {
        this.controller = controller;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public ActuatorFunctionalities getActuatorFunctionalities() {
        return this.actuatorFunctionalities;
    }

    public PlugwiseHAController getController() {
        return controller;
    }

    public void setController(PlugwiseHAController controller) {
        this.controller = controller;
    }

    public Optional<Double> getTemperature() {                
        return this.logs.getTemperature();
    }

    public Optional<Double> getThermostatTemperature() {                
        return this.logs.getThermostatTemperature();
    }

    public Optional<Double> getBatteryLevel() {
        return this.logs.getBatteryLevel();
    }

    public Optional<String> getRelayState() {
        return this.logs.getRelayState();     
    }

    public Optional<Double> getPowerUsage() {
        return this.logs.getPowerUsage();
    }

    public Optional<Boolean> getRelayLockState() {
        ActuatorFunctionalityRelay relay = (ActuatorFunctionalityRelay) this.actuatorFunctionalities.get("relay");
        return Optional.ofNullable(relay.getLockState());
    }


    public boolean isBatteryOperated() {
        return this.getBatteryLevel().isPresent();
    }

    public void setThermostatTemperature(Double temperature) throws PlugwiseHAException {
        ActuatorFunctionalityThermostat thermostat = (ActuatorFunctionalityThermostat) this.actuatorFunctionalities.get("thermostat");

        if (thermostat != null && this.controller != null) {
            try {
                this.controller.setThermostatTemperature(temperature, this, thermostat);
            } catch (Exception e) {
                e.toString();
            }            
        }
    }

    public void switchOff() {
        ActuatorFunctionalityRelay relay = (ActuatorFunctionalityRelay) this.actuatorFunctionalities.get("relay");

        if (relay != null && this.controller != null) {
            try {
                this.controller.switchRelay(this, "off");
            } catch (Exception e) {
                e.toString();
            }            
        }
    }

    public void switchOn() {
        ActuatorFunctionalityRelay relay = (ActuatorFunctionalityRelay) this.actuatorFunctionalities.get("relay");

        if (relay != null && this.controller != null) {
            try {
                this.controller.switchRelay(this, "on");
            } catch (Exception e) {
                e.toString();
            }            
        }
    }

    public void switchLockOff() {
        ActuatorFunctionalityRelay relay = (ActuatorFunctionalityRelay) this.actuatorFunctionalities.get("relay");

        if (relay != null && this.controller != null) {
            try {
                this.controller.switchRelayLock(this, "off");
            } catch (Exception e) {
                e.toString();
            }            
        }
    }

    public void switchLockOn() {
        ActuatorFunctionalityRelay relay = (ActuatorFunctionalityRelay) this.actuatorFunctionalities.get("relay");

        if (relay != null && this.controller != null) {
            try {
                this.controller.switchRelayLock(this, "on");
            } catch (Exception e) {
                e.toString();
            }            
        }
    }
}