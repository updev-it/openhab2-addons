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
 * The {@link ActuatorFunctionalityRelay} class is an object model class that
 * mirrors the XML structure provided by the Plugwise Home Automation controller
 * for the 'relay' actuator functionality. It implements the
 * {@link ActuatorFunctionalityType} interface.
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("relay_functionality")
public class ActuatorFunctionalityRelay implements ActuatorFunctionalityType {

    @XStreamAsAttribute
    private String id;

    @XStreamAlias("updated_date")
    private String updatedDate;

    private String state;

    private boolean lock;

    public ActuatorFunctionalityRelay(String state) {
        this.state = state;
    }

    public ActuatorFunctionalityRelay(String state, boolean lock) {
        this(state);
        this.lock = lock;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return "relay";
    }

    public String getState() {
        return this.state;
    }

    public boolean getLockState() {
        return this.lock;
    }
}
