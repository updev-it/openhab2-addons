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

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import org.openhab.binding.plugwiseha.internal.api.model.object.LogEntry;
import org.openhab.binding.plugwiseha.internal.api.model.object.LogType;
import org.openhab.binding.plugwiseha.internal.api.model.object.Logs;

/**
 * The {@link LogsConverter} class is used by XStream to serialize object which 
 * implement the {@link LogType} interface to and from XML.
 * 
 * @author B. van Wetten - Initial contribution
 */
public class LogsConverter extends MapConverter implements Converter {

    // Constructor

    public LogsConverter(Mapper mapper) {
        super(mapper, Logs.class);
    }

    // Overrides

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class cls) {
        return Logs.class.isAssignableFrom(cls);
    }

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        super.marshal(object, writer, context);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Logs map = new Logs();

        populateStringMap(reader, context, map);

        return map;
    }

    protected void populateStringMap(HierarchicalStreamReader reader, UnmarshallingContext context, Logs map) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            LogType logEntry = (LogType) context.convertAnother(reader, LogEntry.class);

            if (logEntry != null) {
                map.put(logEntry.getType(), logEntry);
            }

            reader.moveUp();
        }
    }

}