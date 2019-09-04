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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The {@link ActuatorFunctionalityOffset} class is an object model class that
 * mirrors the XML structure provided by the Plugwise Home Automation controller
 * for the 'offset' actuator functionality. It implements the
 * {@link ActuatorFunctionalityType} interface.
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("offset_functionality")
public class ActuatorFunctionalityOffset implements ActuatorFunctionalityType {

    @XStreamAsAttribute
    private String id;

    private String type;

    @XStreamAlias("updated_date")
    private String updatedDate;

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }
}
