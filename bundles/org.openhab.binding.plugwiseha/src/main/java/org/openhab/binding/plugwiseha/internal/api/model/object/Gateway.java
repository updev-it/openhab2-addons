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
 * The {@link Gateway} class is an object model class that mirrors the XML
 * structure provided by the Plugwise Home Automation controller for a Plugwise
 * gateway.
 * 
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("gateway")
public class Gateway {

    @XStreamAsAttribute
    private String id;

    private String name;

    @XStreamAlias("vendor_name")
    private String vendorName;

    @XStreamAlias("vendor_model")
    private String vendorModel;

    @XStreamAlias("hardware_version")
    private String hardwareVersion;

    @XStreamAlias("firmware_version")
    private String firmwareVersion;

    @XStreamAlias("mac_address")
    private String macAddress;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorModel() {
        return vendorModel;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getMacAddress() {
        return macAddress;
    }
}