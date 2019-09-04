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

package org.openhab.binding.plugwiseha.internal.handler;

import static org.eclipse.smarthome.core.thing.ThingStatus.*;
import static org.eclipse.smarthome.core.thing.ThingStatusDetail.CONFIGURATION_ERROR;
import static org.openhab.binding.plugwiseha.internal.PlugwiseHABindingConstants.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.measure.quantity.Temperature;

import org.openhab.binding.plugwiseha.internal.PlugwiseHABindingConstants;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAException;
import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;
import org.openhab.binding.plugwiseha.internal.api.model.object.Appliance;
import org.openhab.binding.plugwiseha.internal.config.PlugwiseHAThingConfig;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.core.thing.type.ChannelKind;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.UnDefType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.plugwiseha.internal.handler.PlugwiseHABaseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PlugwiseHAApplianceHandler} class is responsible for handling
 * commands and status updates for the Plugwise Home Automation appliances.
 * Extends @{link PlugwiseHABaseHandler}
 *
 * @author Bas van Wetten - Initial contribution
 *
 */
public class PlugwiseHAApplianceHandler extends PlugwiseHABaseHandler<Appliance, PlugwiseHAThingConfig> {

    private PlugwiseHAThingConfig config = new PlugwiseHAThingConfig();
    private @Nullable Appliance appliance;
    private final Logger logger = LoggerFactory.getLogger(PlugwiseHAApplianceHandler.class);

    // Constructor

    public PlugwiseHAApplianceHandler(Thing thing) {
        super(thing);
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return PlugwiseHABindingConstants.THING_TYPE_APPLIANCE_VALVE.equals(thingTypeUID)
                || PlugwiseHABindingConstants.THING_TYPE_APPLIANCE_PUMP.equals(thingTypeUID);
    }

    // Overrides

    @Override
    protected synchronized void initialize(PlugwiseHAThingConfig config) {
        if (thing.getStatus() == INITIALIZING) {
            logger.debug("Initializing Plugwise Home Automation appliance handler with config = {}", config);
            if (!config.isValid()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR,
                        "Invalid configuration for Plugwise Home Automation appliance handler.");
                return;
            }
            this.config = config;
            this.appliance = getEntity(this.getPlugwiseHABridge().getController());

            setApplianceProperties();
            updateStatus(ONLINE);

            if (this.appliance.isBatteryOperated()) {
                addBatteryChannels();
            }
        }
    }

    @Override
    protected @Nullable Appliance getEntity(PlugwiseHAController controller) {
        Appliance appliance = controller.getAppliance(this.getId());

        return appliance;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleCommand(Appliance entity, ChannelUID channelUID, Command command) throws PlugwiseHAException {
        String channelID = channelUID.getIdWithoutGroup();
        switch (channelID) {
        case APPLIANCE_SETPOINT_CHANNEL:
            if (command instanceof QuantityType) {
                QuantityType<Temperature> state = (QuantityType<Temperature>) command;
                entity.setThermostatTemperature(state.doubleValue());
                updateState(APPLIANCE_SETPOINT_CHANNEL, (State) command);
            }
            break;
        case APPLIANCE_POWER_CHANNEL:
            if (command instanceof OnOffType) {
                OnOffType state = (OnOffType) command;
                if (state == OnOffType.ON) {
                    entity.switchOn();
                } else {
                    entity.switchOff();
                }
                updateState(APPLIANCE_POWER_CHANNEL, (State) command);
            }
            break;
        case APPLIANCE_LOCK_CHANNEL:
            if (command instanceof OnOffType) {
                OnOffType state = (OnOffType) command;
                if (state == OnOffType.ON) {
                    entity.switchLockOn();
                } else {
                    entity.switchLockOff();
                }
                updateState(APPLIANCE_LOCK_CHANNEL, (State) command);
            }
            break;
        default:
            logger.warn("Ignoring unsupported command = {} for channel = {}", command, channelUID);
        }
    }

    private State getDefaultState(String channelID) {
        State state = UnDefType.NULL;
        switch (channelID) {
        case APPLIANCE_SETPOINT_CHANNEL:
        case APPLIANCE_TEMPERATURE_CHANNEL:
        case APPLIANCE_BATTERYLEVEL_CHANNEL:
        case APPLIANCE_POWER_USAGE_CHANNEL:
            state = UnDefType.NULL;
            break;
        case APPLIANCE_BATTERYLEVELLOW_CHANNEL:
        case APPLIANCE_POWER_CHANNEL:
        case APPLIANCE_LOCK_CHANNEL:
            state = UnDefType.UNDEF;
            break;
        }
        return state;
    }

    @Override
    protected void refreshChannel(Appliance entity, ChannelUID channelUID) {
        String channelID = channelUID.getIdWithoutGroup();
        State state = getDefaultState(channelID);

        switch (channelID) {
        case APPLIANCE_SETPOINT_CHANNEL:
            if (entity.getThermostatTemperature().isPresent()) {
                state = new DecimalType(entity.getThermostatTemperature().get());
            }
            break;
        case APPLIANCE_TEMPERATURE_CHANNEL:
            if (entity.getTemperature().isPresent()) {
                state = new DecimalType(entity.getTemperature().get());
            }
            break;
        case APPLIANCE_BATTERYLEVEL_CHANNEL: {
            Optional<Double> batteryLevelOptional = entity.getBatteryLevel();
            Double batteryLevel = batteryLevelOptional.isPresent() ? batteryLevelOptional.get() * 100 : null;

            if (batteryLevelOptional.isPresent()) {
                state = new DecimalType(batteryLevel.intValue());
            }
            break;
        }
        case APPLIANCE_BATTERYLEVELLOW_CHANNEL: {
            Optional<Double> batteryLevelOptional = entity.getBatteryLevel();
            Double batteryLevel = batteryLevelOptional.isPresent() ? batteryLevelOptional.get() * 100 : null;

            if (batteryLevelOptional.isPresent()) {
                if (batteryLevel <= this.config.getLowBatteryPercentage()) {
                    state = OnOffType.ON;
                } else {
                    state = OnOffType.OFF;
                }
            }
            break;
        }
        case APPLIANCE_POWER_USAGE_CHANNEL: {
            if (entity.getPowerUsage().isPresent()) {
                state = new DecimalType(entity.getPowerUsage().get());
            }
            break;
        }
        case APPLIANCE_POWER_CHANNEL: {
            Optional<String> relayStateOptional = entity.getRelayState();
            String relayState = relayStateOptional.isPresent() ? relayStateOptional.get() : null;

            if (relayStateOptional.isPresent()) {
                switch (relayState.toLowerCase()) {
                case "on":
                    state = OnOffType.ON;
                    break;
                case "off":
                    state = OnOffType.OFF;
                    break;
                default:
                    break;
                }
            }
            break;
        }
        case APPLIANCE_LOCK_CHANNEL: {
            Optional<Boolean> relayLockStateOptional = entity.getRelayLockState();
            boolean relayLockState = relayLockStateOptional.isPresent() ? relayLockStateOptional.get() : null;

            if (relayLockStateOptional.isPresent()) {
                if (relayLockState == true) {
                    state = OnOffType.ON;
                } else {
                    state = OnOffType.OFF;
                }
            }
            break;
        }
        default:
            break;
        }

        if (state != UnDefType.NULL && state != UnDefType.UNDEF) {
            updateState(channelID, state);
        }
    }

    protected synchronized void addBatteryChannels() {
        logger.debug("Battery operated appliance detected: adding 'Battery level' and 'Battery low level' channels");

        ChannelUID channelUIDBatteryLevel = new ChannelUID(getThing().getUID(), APPLIANCE_BATTERYLEVEL_CHANNEL);
        ChannelUID channelUIDBatteryLevelLow = new ChannelUID(getThing().getUID(), APPLIANCE_BATTERYLEVELLOW_CHANNEL);

        boolean channelBatteryLevelExists = false;
        boolean channelBatteryLowExists = false;

        List<Channel> channels = getThing().getChannels();
        for (Channel channel : channels) {
            if (channel.getUID().equals(channelUIDBatteryLevel)) {
                channelBatteryLevelExists = true;
            } else if (channel.getUID().equals(channelUIDBatteryLevelLow)) {
                channelBatteryLowExists = true;
            }
        }

        if (!channelBatteryLevelExists) {
            ThingBuilder thingBuilder = editThing();

            Channel channelBatteryLevel = ChannelBuilder.create(channelUIDBatteryLevel, "Number")
                    .withType(CHANNEL_TYPE_BATTERYLEVEL).withKind(ChannelKind.STATE).withLabel("Battery level")
                    .withDescription("Represents the battery level as a percentage (0-100%)").build();

            thingBuilder.withChannel(channelBatteryLevel);

            updateThing(thingBuilder.build());
        }

        if (!channelBatteryLowExists) {
            ThingBuilder thingBuilder = editThing();

            Channel channelBatteryLow = ChannelBuilder.create(channelUIDBatteryLevelLow, "Switch:Battery")
                    .withType(CHANNEL_TYPE_BATTERYLEVELLOW).withKind(ChannelKind.STATE).withLabel("Battery low level")
                    .withDescription("Switches ON when battery level gets below threshold level").build();

            thingBuilder.withChannel(channelBatteryLow);

            updateThing(thingBuilder.build());
        }
    }

    protected void setApplianceProperties() {
        Map<String, String> properties = editProperties();
        properties.put("description", this.appliance.getDescription());
        properties.put("type", this.appliance.getType());
        properties.put("functionalities", this.appliance.getActuatorFunctionalities().keySet().stream()
                .map(e -> e.toString()).collect(Collectors.joining(", ")));
        updateProperties(properties);
    }
}