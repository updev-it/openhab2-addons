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
package org.openhab.binding.plugwiseha.internal.api.exception;

/**
 * The {@link PlugwiseHAUnauthorizedException} represents a binding specific {@link Exception}.
 *
 * @author Bas van Wetten - Initial contribution
 */
public class PlugwiseHAUnauthorizedException extends PlugwiseHAException {

    private static final long serialVersionUID = 1L;

    public PlugwiseHAUnauthorizedException(String message) {
        super(message);
    }

    public PlugwiseHAUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlugwiseHAUnauthorizedException(Throwable cause) {
        super(cause);
    }
}
