/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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

package org.openhab.binding.plugwiseha.internal.api.xml;

import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;

import org.openhab.binding.plugwiseha.internal.api.model.converter.DateTimeConverter;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalities;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionality;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityRelay;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityThermostat;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityThreshold;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityTimer;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityToggle;
import org.openhab.binding.plugwiseha.internal.api.model.object.Appliance;
import org.openhab.binding.plugwiseha.internal.api.model.object.Appliances;
import org.openhab.binding.plugwiseha.internal.api.model.object.DomainObjects;
import org.openhab.binding.plugwiseha.internal.api.model.object.GatewayEnvironment;
import org.openhab.binding.plugwiseha.internal.api.model.object.GatewayInfo;
import org.openhab.binding.plugwiseha.internal.api.model.object.Location;
import org.openhab.binding.plugwiseha.internal.api.model.object.Locations;
import org.openhab.binding.plugwiseha.internal.api.model.object.Log;
import org.openhab.binding.plugwiseha.internal.api.model.object.Logs;
import org.openhab.binding.plugwiseha.internal.api.model.object.Modules;
import org.openhab.binding.plugwiseha.internal.api.model.object.Module;
import org.openhab.binding.plugwiseha.internal.api.model.object.Services;
import org.openhab.binding.plugwiseha.internal.api.model.object.Service;
import org.openhab.binding.plugwiseha.internal.api.model.object.ZigBeeNode;

/**
 * The {@link PlugwiseHAXStream} class is a utility class that wraps an XStream
 * object and provide additional functionality specific to the PlugwiseHA
 * binding. It automatically load the correct converter classes and processes
 * the XStream annotions used by the object classes.
 * 
 * @author B. van Wetten - Initial contribution
 */
public class PlugwiseHAXStream extends XStream {

    private static XmlFriendlyNameCoder customCoder = new XmlFriendlyNameCoder("_-", "_");

    public PlugwiseHAXStream() {
        super(new StaxDriver(PlugwiseHAXStream.customCoder));

        initialize();
    }

    // Protected methods

    @SuppressWarnings("rawtypes")
    protected void allowClass(Class clz) {
        this.processAnnotations(clz);
        this.allowTypeHierarchy(clz);
    }

    protected void initialize() {
        // Configure XStream
        this.ignoreUnknownElements();
        this.setClassLoader(getClass().getClassLoader());

        // Clear out existing
        this.addPermission(NoTypePermission.NONE);
        this.addPermission(NullPermission.NULL);

        // Whitelist classes
        this.allowClass(GatewayInfo.class);
        this.allowClass(GatewayEnvironment.class);
        this.allowClass(Appliances.class);
        this.allowClass(Appliance.class);
        this.allowClass(Modules.class);
        this.allowClass(Module.class);
        this.allowClass(Locations.class);
        this.allowClass(Location.class);
        this.allowClass(Logs.class);
        this.allowClass(Log.class);
        this.allowClass(Services.class);
        this.allowClass(Service.class);
        this.allowClass(ZigBeeNode.class);
        this.allowClass(ActuatorFunctionalities.class);
        this.allowClass(ActuatorFunctionality.class);
        this.allowClass(ActuatorFunctionalityThermostat.class);
        this.allowClass(ActuatorFunctionalityRelay.class);
        this.allowClass(ActuatorFunctionalityTimer.class);
        this.allowClass(ActuatorFunctionalityThreshold.class);
        this.allowClass(ActuatorFunctionalityToggle.class);
        this.allowClass(DomainObjects.class);

        // Register custom converters
        this.registerConverter(new DateTimeConverter());
    }

    // Public methods

    public void prettyPrint(Object object) {
        BufferedOutputStream stdout = new BufferedOutputStream(System.out);
        prettyPrint(object, new OutputStreamWriter(stdout));
    }

    public void prettyPrint(Object object, OutputStreamWriter outputStreamWriter) {
        this.marshal(object, new PrettyPrintWriter(outputStreamWriter, PlugwiseHAXStream.customCoder));
    }
}