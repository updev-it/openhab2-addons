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

import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalities;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityOffset;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityRelay;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityThermostat;
import org.openhab.binding.plugwiseha.internal.api.model.object.ActuatorFunctionalityType;

/**
 * The {@link ActuatorFunctionalitiesConverter} class is used by XStream to
 * serialize {@link ActuatorFunctionalities} to and from XML.
 * 
 * @author B. van Wetten - Initial contribution
 */
@SuppressWarnings("unused")
public class ActuatorFunctionalitiesConverter extends BaseConverter<ActuatorFunctionalities> {

    private final String attributeName;

    public ActuatorFunctionalitiesConverter(Mapper mapper) {
        this(mapper, null);
    }

    public ActuatorFunctionalitiesConverter(Mapper mapper, String attributeName) {
        super(mapper, null, ActuatorFunctionalities.class);

        this.attributeName = attributeName;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class cls) {
        return ActuatorFunctionalities.class.isAssignableFrom(cls);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        ActuatorFunctionalities map = new ActuatorFunctionalities();

        populateStringMap(reader, context, map);

        return map;
    }

    protected void populateStringMap(HierarchicalStreamReader reader, UnmarshallingContext context,
            ActuatorFunctionalities map) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String type = reader.getNodeName();
            ActuatorFunctionalityType actuatorFunctionalityType = null;

            switch (type) {
            case "thermostat_functionality":
                actuatorFunctionalityType = (ActuatorFunctionalityThermostat) readItem(reader, context, map);
                break;
            case "relay_functionality":
                actuatorFunctionalityType = (ActuatorFunctionalityRelay) readItem(reader, context, map);
                break;
            case "offset_functionality":
                actuatorFunctionalityType = (ActuatorFunctionalityOffset) readItem(reader, context, map);
                break;
            default:
                // value = (ActuatorFunctionalityType) readItem(reader, context, map);
                break;
            }

            if (actuatorFunctionalityType != null) {
                map.put(actuatorFunctionalityType.getType(), actuatorFunctionalityType);
            }

            reader.moveUp();
        }
    }
}