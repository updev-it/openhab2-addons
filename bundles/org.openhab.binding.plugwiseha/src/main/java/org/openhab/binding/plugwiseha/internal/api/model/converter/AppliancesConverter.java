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

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;
import org.openhab.binding.plugwiseha.internal.api.model.object.Appliance;
import org.openhab.binding.plugwiseha.internal.api.model.object.Appliances;

/**
 * The {@link AppliancesConverter} class is used by XStream to serialize
 * {@link Appliances} to and from XML.
 * 
 * @author B. van Wetten - Initial contribution
 */
public class AppliancesConverter extends BaseConverter<Appliances> {

    private static final Class<Appliances> CLAZZ = Appliances.class;
    private final String attributeName;

    public AppliancesConverter(Mapper mapper, String attributeName, PlugwiseHAController controller) {
        super(mapper, controller, CLAZZ);

        this.attributeName = attributeName;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class cls) {
        return Appliances.class.isAssignableFrom(cls);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Appliances map = new Appliances();

        populateMap(reader, context, map);

        return map;
    }

    protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Appliances map) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String key = reader.getAttribute(this.attributeName);
            Appliance appliance = (Appliance) readItem(reader, context, map);

            appliance.setController(this.controller);

            map.put(key, appliance);

            reader.moveUp();
        }
    }
}