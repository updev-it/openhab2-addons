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

package org.openhab.binding.plugwiseha.internal.api.model;

/**
 * The {@link PlugwiseHAModel} interface describes common
 * methods that need to be implemented by any object model class.
 * 
 * @author B. van Wetten - Initial contribution
 */
public interface PlugwiseHAModel {

    public abstract boolean isBatteryOperated();
    
}