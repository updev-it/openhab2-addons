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

package org.openhab.binding.plugwiseha.internal.api.model.converter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;
import org.openhab.binding.plugwiseha.internal.api.model.object.Location;
import org.openhab.binding.plugwiseha.internal.api.model.object.Locations;

/**
 * The {@link LocationsConverter} class is used by XStream to serialize
 * {@link Locations} to and from XML.
 * 
 * @author B. van Wetten - Initial contribution
 */
public class LocationsConverter extends BaseConverter<Locations> {

    private static final Class<Locations> CLAZZ = Locations.class;
    private final String attributeName;

    // Constructor

    public LocationsConverter(Mapper mapper, String attributeName, PlugwiseHAController controller) {
        super(mapper, controller, CLAZZ);

        this.attributeName = attributeName;
    }

    // Overrides

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class cls) {
        return CLAZZ.isAssignableFrom(cls);
    }

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        super.marshal(object, writer, context);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Locations map = new Locations();

        populateMap(reader, context, map);

        return map;
    }

    protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Locations map) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String key = reader.getAttribute(this.attributeName);
            Location location = (Location) readItem(reader, context, map);

            if (location != null) {
                location.setController(this.controller);
                map.put(key, location);
            }

            reader.moveUp();
        }
    }
}