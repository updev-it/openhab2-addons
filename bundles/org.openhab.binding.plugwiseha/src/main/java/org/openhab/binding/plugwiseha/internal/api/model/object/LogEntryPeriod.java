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

import java.util.Optional;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The {@link LogEntryPeriod} class a class to be used by XStream to serialize
 * Plugwise 'period' entries to and from XML. These entries contain measurements
 * such as temperatures, offsets, relay states, power usage, et cetera
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("period")
public class LogEntryPeriod {

    @XStreamAlias("start_date")
    @XStreamAsAttribute
    private String startDate;

    @XStreamAlias("end_date")
    @XStreamAsAttribute
    private String endDate;

    private String measurement;

    public Optional<String> getMeasurement() {
        return Optional.ofNullable(this.measurement);
    }
}