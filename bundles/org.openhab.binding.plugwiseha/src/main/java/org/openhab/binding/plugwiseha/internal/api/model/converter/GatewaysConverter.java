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
import org.openhab.binding.plugwiseha.internal.api.model.object.Gateway;
import org.openhab.binding.plugwiseha.internal.api.model.object.Gateways;

/**
 * The {@link GatewaysConverter} class is used by XStream to serialize
 * {@link Gateways} to and from XML.
 * 
 * @author B. van Wetten - Initial contribution
 */
public class GatewaysConverter extends BaseConverter<Gateways> {

    private final String attributeName;

    public GatewaysConverter(Mapper mapper) {
        this(mapper, null);
    }

    public GatewaysConverter(Mapper mapper, String attributeName) {
        super(mapper, null, Gateways.class);

        this.attributeName = attributeName;
    }

    // Overrides

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class cls) {
        return Gateways.class.isAssignableFrom(cls);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Gateways map = new Gateways();

        populateMap(reader, context, map);

        return map;
    }

    protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Gateways map) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String key = reader.getAttribute(this.attributeName);
            Gateway gateway = (Gateway) readItem(reader, context, map);

            if (gateway != null) {
                map.put(key, gateway);
            }

            reader.moveUp();
        }
    }
}