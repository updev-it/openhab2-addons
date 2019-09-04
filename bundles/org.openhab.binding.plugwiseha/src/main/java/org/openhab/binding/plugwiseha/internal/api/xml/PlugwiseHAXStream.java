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

package org.openhab.binding.plugwiseha.internal.api.xml;

import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;
import org.openhab.binding.plugwiseha.internal.api.model.converter.ActuatorFunctionalitiesConverter;
import org.openhab.binding.plugwiseha.internal.api.model.converter.AppliancesConverter;
import org.openhab.binding.plugwiseha.internal.api.model.converter.GatewaysConverter;
import org.openhab.binding.plugwiseha.internal.api.model.converter.LocationsConverter;
import org.openhab.binding.plugwiseha.internal.api.model.converter.LogConverter;
import org.openhab.binding.plugwiseha.internal.api.model.converter.LogsConverter;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityOffset;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityRelay;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityThermostat;
import org.openhab.binding.plugwiseha.internal.api.model.object.Gateways;
import org.openhab.binding.plugwiseha.internal.api.model.object.Locations;
import org.openhab.binding.plugwiseha.internal.api.model.object.LogEntryPeriod;

/**
 * The {@link PlugwiseHAXStream} class is a utility class that wraps an XStream
 * object and provide additional functionality specific to the PlugwiseHA
 * binding. It automatically load the correct converter classes and processes
 * the XStream annotions used by the object classes.
 * 
 * @author B. van Wetten - Initial contribution
 */
public class PlugwiseHAXStream extends XStream {

    protected final PlugwiseHAController controller;

    private static XmlFriendlyNameCoder customCoder = new XmlFriendlyNameCoder("_-", "_");

    public PlugwiseHAXStream(PlugwiseHAController controller) {
        super(new StaxDriver(PlugwiseHAXStream.customCoder));

        this.controller = controller;
        initialize();
    }

    // Protected methods

    protected void initialize() {
        // Configure XStream
        this.ignoreUnknownElements();
        this.setClassLoader(getClass().getClassLoader());

        // Register custom converters
        this.registerConverter(new LocationsConverter(this.getMapper(), "id", controller));
        this.registerConverter(new AppliancesConverter(this.getMapper(), "id", controller));
        this.registerConverter(new GatewaysConverter(this.getMapper(), "id"));
        this.registerConverter(new ActuatorFunctionalitiesConverter(this.getMapper(), "id"));
        this.registerConverter(new LogsConverter(this.getMapper()));
        this.registerConverter(new LogConverter(this.getMapper()));

        // Process annotationsLocations
        this.processAnnotations(Locations.class);
        this.processAnnotations(LogEntryPeriod.class);
        this.processAnnotations(ActuatorFunctionalityThermostat.class);
        this.processAnnotations(ActuatorFunctionalityRelay.class);
        this.processAnnotations(ActuatorFunctionalityOffset.class);
        this.processAnnotations(Gateways.class);

    }

    // Public methods

    public Object fromXML(String xml) {
        return super.fromXML(xml);
    }

    @SuppressWarnings("rawtypes")
    public Object fromXML(String xml, Class outClass) {
        try {
            Class.forName(outClass.getName());
            super.processAnnotations(outClass);
            return fromXML(xml);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public String toXML(Object object) {
        return super.toXML(object);
    }

    @SuppressWarnings("rawtypes")
    public String toXML(Object object, Class outClass) {
        try {
            Class.forName(outClass.getName());
            super.processAnnotations(outClass);
            return toXML(object);
        } catch (Exception e) {
            return null;
        }
    }

    public void prettyPrint(Object object) {
        BufferedOutputStream stdout = new BufferedOutputStream(System.out);
        prettyPrint(object, new OutputStreamWriter(stdout));
    }

    public void prettyPrint(Object object, OutputStreamWriter outputStreamWriter) {
        this.marshal(object, new PrettyPrintWriter(outputStreamWriter, PlugwiseHAXStream.customCoder));
    }
}