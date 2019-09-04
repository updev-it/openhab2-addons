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

/**
 * The {@link LogEntry} class is an extendible class to be used by XStream to
 * serialize Plugwise 'point_log' entries to and from XML. It implements the
 * {@link LogType} interface
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("point_log")
public class LogEntry implements LogType {

    private String type;

    public String getType() {
        return this.type;
    }
}
