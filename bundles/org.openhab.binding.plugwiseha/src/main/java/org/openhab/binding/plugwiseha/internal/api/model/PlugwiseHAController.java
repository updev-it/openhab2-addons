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

package org.openhab.binding.plugwiseha.internal.api.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAException;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityRelay;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityThermostat;
import org.openhab.binding.plugwiseha.internal.api.model.object.Appliance;
import org.openhab.binding.plugwiseha.internal.api.model.object.Appliances;
import org.openhab.binding.plugwiseha.internal.api.model.object.Gateway;
import org.openhab.binding.plugwiseha.internal.api.model.object.Gateways;
import org.openhab.binding.plugwiseha.internal.api.model.object.Location;
import org.openhab.binding.plugwiseha.internal.api.model.object.Locations;
import org.openhab.binding.plugwiseha.internal.api.xml.PlugwiseHAXStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PlugwiseHAController} class provides the interface to the Plugwise
 * Home Automation API and stores/caches the object model for use by the various
 * ThingHandlers of this binding.
 * 
 * @author B. van Wetten - Initial contribution
 */
@NonNullByDefault
public class PlugwiseHAController {

    // Private member variables/constants

    private final Logger logger = LoggerFactory.getLogger(PlugwiseHAController.class);
    private final HttpClient httpClient;
    private final PlugwiseHAXStream XStream;

    private final String host;
    private final int port;
    private final String username;
    private final String smileId;

    private @Nullable Gateways gateways;
    private @Nullable Locations locations;
    private @Nullable Appliances appliances;

    public PlugwiseHAController(HttpClient httpClient, String host, int port, String username, String smileId) {
        this.httpClient = httpClient;
        this.host = host;
        this.port = port;
        this.username = username;
        this.smileId = smileId;

        this.XStream = new PlugwiseHAXStream(this);
    }

    // Public methods

    public void start(Runnable callback) throws PlugwiseHAException {
        this.gateways = getGateways();

        refresh();
        callback.run();
    }

    public void stop() throws PlugwiseHAException {
    }

    public void refresh() throws PlugwiseHAException {
        synchronized (this) {
            this.locations = getLocations();
            this.appliances = getAppliances();
        }
    }

    public Gateways getGateways() throws PlugwiseHAException {
        Gateways gateways;
        PlugwiseHAControllerRequest<Gateways> request = newRequest(Gateways.class);

        request.setPath("/core/gateways");

        gateways = executeRequest(request);

        if (gateways != null) {
            this.logger.debug("Found {} Plugwise Home Automation gateway(s)", gateways.size());
            return gateways;
        } else {
            this.logger.debug("No Plugwise Home Automation gateways found");
            return new Gateways();
        }
    }

    public Locations getLocations() throws PlugwiseHAException {
        Locations locations;
        PlugwiseHAControllerRequest<Locations> request = newRequest(Locations.class);

        request.setPath("/core/locations");

        locations = executeRequest(request);

        if (locations != null) {
            this.logger.debug("Found {} Plugwise Home Automation location(s)", locations.size());
            return locations;
        } else {
            this.logger.debug("No Plugwise Home Automation locations found");
            return new Locations();
        }
    }

    public Appliances getAppliances() throws PlugwiseHAException {
        Appliances appliances;

        PlugwiseHAControllerRequest<Appliances> request = newRequest(Appliances.class);

        request.setPath("/core/appliances");

        appliances = executeRequest(request);

        if (appliances != null) {
            this.logger.debug("Found {} Plugwise Home Automation appliance(s)", appliances.size());
            return appliances;
        } else {
            this.logger.debug("No Plugwise Home Automation locations found");
            return new Appliances();
        }
    }

    public @Nullable Gateway getGateway() {
        Map.Entry<String, Gateway> entry = this.gateways.entrySet().iterator().next();
        return entry.getValue();
    }

    public @Nullable Location getLocation(String locationId) {
        return this.locations.get(locationId);
    }

    public @Nullable Appliance getAppliance(String applianceId) {
        return this.appliances.get(applianceId);
    }

    public void setThermostatTemperature(Double temperature, Appliance appliance,
            ActuatorFunctionalityThermostat thermostat) throws PlugwiseHAException {
        PlugwiseHAControllerRequest<Void> request = newRequest(Void.class);

        request.setPath("/core/appliances");

        request.addPathParameter("id", String.format("%s/thermostat", appliance.getId()));
        request.addPathParameter("id", String.format("%s", thermostat.getId()));

        request.setBodyParameter(new ActuatorFunctionalityThermostat(temperature));

        executeRequest(request);
    }

    public void setThermostatTemperature(Double temperature, Location location,
            ActuatorFunctionalityThermostat thermostat) throws PlugwiseHAException {
        PlugwiseHAControllerRequest<Void> request = newRequest(Void.class);

        request.setPath("/core/locations");

        request.addPathParameter("id", String.format("%s/thermostat", location.getId()));
        request.addPathParameter("id", String.format("%s", thermostat.getId()));

        request.setBodyParameter(new ActuatorFunctionalityThermostat(temperature));

        executeRequest(request);
    }

    public void switchRelay(Appliance appliance, String state) throws PlugwiseHAException {
        List<String> allowStates = Arrays.asList("on", "off");
        if (allowStates.contains(state.toLowerCase())) {
            if (state.toLowerCase().equals("on")) {
                switchRelayOn(appliance);
            } else {
                switchRelayOff(appliance);
            }
        }
    }

    public void switchRelayOff(Appliance appliance) throws PlugwiseHAException {
        PlugwiseHAControllerRequest<Void> request = newRequest(Void.class);
        boolean relayLockState = appliance.getRelayLockState().orElse(null);

        request.setPath("/core/appliances");

        request.addPathParameter("id", String.format("%s/relay", appliance.getId()));

        request.setBodyParameter(new ActuatorFunctionalityRelay("off", relayLockState));

        executeRequest(request);
    }

    public void switchRelayOn(Appliance appliance) throws PlugwiseHAException {
        PlugwiseHAControllerRequest<Void> request = newRequest(Void.class);
        boolean relayLockState = appliance.getRelayLockState().orElse(null);

        request.setPath("/core/appliances");

        request.addPathParameter("id", String.format("%s/relay", appliance.getId()));

        request.setBodyParameter(new ActuatorFunctionalityRelay("on", relayLockState));

        executeRequest(request);
    }

    public void switchRelayLock(Appliance appliance, String state) throws PlugwiseHAException {
        List<String> allowStates = Arrays.asList("on", "off");
        if (allowStates.contains(state.toLowerCase())) {
            if (state.toLowerCase().equals("on")) {
                switchRelayLockOn(appliance);
            } else {
                switchRelayLockOff(appliance);
            }

            // Temporary 'hack' to update the internal model of appliances to reflect the
            // lock state
            this.appliances = getAppliances();
        }
    }

    public void switchRelayLockOff(Appliance appliance) throws PlugwiseHAException {
        PlugwiseHAControllerRequest<Void> request = newRequest(Void.class);

        request.setPath("/core/appliances");

        request.addPathParameter("id", String.format("%s/relay", appliance.getId()));

        request.setBodyParameter(new ActuatorFunctionalityRelay(null, false));

        executeRequest(request);
    }

    public void switchRelayLockOn(Appliance appliance) throws PlugwiseHAException {
        PlugwiseHAControllerRequest<Void> request = newRequest(Void.class);

        request.setPath("/core/appliances");

        request.addPathParameter("id", String.format("%s/relay", appliance.getId()));

        request.setBodyParameter(new ActuatorFunctionalityRelay(null, true));

        executeRequest(request);
    }

    // Protected and private methods

    private <T> PlugwiseHAControllerRequest<T> newRequest(Class<T> responseType) {
        return new PlugwiseHAControllerRequest<T>(responseType, this.XStream, this.httpClient, this.host, this.port,
                this.username, this.smileId);
    }

    private <T> @Nullable T executeRequest(PlugwiseHAControllerRequest<T> request) throws PlugwiseHAException {
        T result;
        result = request.execute();
        return result;
    }
}