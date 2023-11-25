package io.openems.edge.evcs.api;

import java.util.function.Consumer;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Level;
import io.openems.common.channel.PersistencePriority;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.EnumReadChannel;
import io.openems.edge.common.channel.IntegerReadChannel;
import io.openems.edge.common.channel.LongReadChannel;
import io.openems.edge.common.channel.StateChannel;
import io.openems.edge.common.channel.value.Value;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.modbusslave.ModbusSlaveNatureTable;
import io.openems.edge.common.modbusslave.ModbusType;
import io.openems.edge.meter.api.ElectricityMeter;

public interface Evcs extends OpenemsComponent {

	public static final int DEFAULT_MAXIMUM_HARDWARE_POWER = 22_080; // W
	public static final int DEFAULT_MINIMUM_HARDWARE_POWER = 4_140; // W
	public static final int DEFAULT_MAXIMUM_HARDWARE_CURRENT = 32_000; // mA
	public static final int DEFAULT_MINIMUM_HARDWARE_CURRENT = 6_000; // mA
	public static final int DEFAULT_VOLTAGE = 230; // V
	public static final int DEFAULT_POWER_RECISION = 230;

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {

		/**
		 * Status.
		 *
		 * <p>
		 * The Status of the EVCS charging station.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Status
		 * </ul>
		 */
		STATUS(Doc.of(Status.values()) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Charge Power.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: W
		 * </ul>
		 */
		CHARGE_POWER(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT).accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Charging Type.
		 *
		 * <p>
		 * Type of charging.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: ChargingType
		 * </ul>
		 */
		CHARGING_TYPE(Doc.of(ChargingType.values()) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Current.
		 *
		 * <p>
		 * The current for all three phases in mA.
		 * 
		 * <p>
		 * See Evcs.initializeCurrentSumChannels() also.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: Milliampere
		 * </ul>
		 */
		CURRENT(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.MILLIAMPERE) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)),

		/**
		 * CurrentL1.
		 *
		 * <p>
		 * The current for phase L1.
		 * 
		 * <p>
		 * In charge parks the phases of chargepoints are rotated when installed. Thus a
		 * chargepoint measures current on L1 but it may actually be connected to L2 or
		 * L3.
		 * 
		 * <p>
		 * CurrentL1 needs to reflect the real phase L1, thus the driver is responsible
		 * to rotating the phases before updating this channel. Thus the controller
		 * using this channel can always trust that CURRENT_L1 reflects the current on
		 * Phase 1.
		 * 
		 * <p>
		 * chargepoint driver developer may use the following helper method:
		 * this.getPhaseRotation().getFirstPhase()
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: Milliampere
		 * </ul>
		 */
		CURRENT_L1(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.MILLIAMPERE) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)),

		/**
		 * CurrentL2.
		 *
		 * <p>
		 * The current for phase L2.
		 * 
		 * <p>
		 * In charge parks the phases of chargepoints are rotated when installed. Thus a
		 * chargepoint measures current on L2 but it may actually be connected to L2 or
		 * L3.
		 * 
		 * <p>
		 * CurrentL2 needs to reflect the real phase L2, thus the driver is responsible
		 * to rotating the phases before updating this channel. Thus the controller
		 * using this channel can always trust that CURRENT_L2 reflects the current on
		 * Phase 1.
		 * 
		 * <p>
		 * chargepoint driver developer may use the following helper method:
		 * this.getPhaseRotation().getFirstPhase()
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: Milliampere
		 * </ul>
		 */
		CURRENT_L2(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.MILLIAMPERE) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)),

		/**
		 * CurrentL3.
		 *
		 * <p>
		 * The current for phase L3.
		 * 
		 * <p>
		 * In charge parks the phases of chargepoints are rotated when installed. Thus a
		 * chargepoint measures current on L3 but it may actually be connected to L3 or
		 * L3.
		 * 
		 * <p>
		 * CurrentL3 needs to reflect the real phase L3, thus the driver is responsible
		 * to rotating the phases before updating this channel. Thus the controller
		 * using this channel can always trust that CURRENT_L3 reflects the current on
		 * Phase 1.
		 * 
		 * <p>
		 * chargepoint driver developer may use the following helper method:
		 * this.getPhaseRotation().getFirstPhase()
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: Milliampere
		 * </ul>
		 */
		CURRENT_L3(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.MILLIAMPERE) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)),

		/**
		 * Count of phases, the EV is charging with.
		 *
		 * <p>
		 * This value is derived from the charging station or calculated during the
		 * charging. When this value is set, the minimum and maximum limits are set at
		 * the same time if the EVCS is a {@link ManagedEvcs}.
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * </ul>
		 */
		PHASES(Doc.of(Phases.values()) //
				.debounce(5) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Phase Rotation.
		 * 
		 * <p>
		 * In charge parks the phases of chargepoints are rotated when installed. This
		 * reduces phase shifting load when the majority of vehicles charge with only
		 * 2phase or less. Channel provides information on the phase rotation of this
		 * chargepoint.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: PhaseRotation @see {@link PhaseRotation}
		 * </ul>
		 */
		PHASE_ROTATION(Doc.of(PhaseRotation.values()) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Fixed minimum power allowed by the hardware in W.
		 * 
		 * <p>
		 * Maximum of the configured minimum hardware limit and the read or given
		 * minimum hardware limit - e.g. KEBA minimum requirement is 6A = 4140W and the
		 * component configuration is 10A = 6900W because the customer wants to ensure
		 * that his Renault ZOE is always charging with an acceptable efficiency. In
		 * this case the Channel should be set to 6900W. Used power instead of current,
		 * because it is easier to store it in an Integer Channel. When this value is
		 * set, the minimum and maximum limits are set at the same time if the EVCS is a
		 * {@link ManagedEvcs}.
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: W
		 * </ul>
		 */
		FIXED_MINIMUM_HARDWARE_POWER(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Fixed maximum power allowed by the hardware in W.
		 * 
		 * <p>
		 * Minimum of the configured maximum hardware limit and the read maximum
		 * hardware limit - e.g. KEBA Dip-Switch Settings set to 32A = 22080W and
		 * component configuration of 16A = 11040. In this case the Channel should be
		 * set to 11040W. Used power instead of current, because it is easier to store
		 * it in an Integer Channel. When this value is set, the minimum and maximum
		 * limits are a Integer Channel. When this value is set, the minimum and maximum
		 * limits are set at the same time if the EVCS is a {@link ManagedEvcs}.
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: W
		 * </ul>
		 */
		FIXED_MAXIMUM_HARDWARE_POWER(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Minimum hardware power in W calculated with the current used Phases, used for
		 * the boundaries of the monitoring.
		 * 
		 * <p>
		 * This minimum limit is dynamically set depending on the current used
		 * {@link #PHASES} and the {@link #FIXED_MINIMUM_HARDWARE_POWER}, to be able to
		 * react on different power limits if some of the Phases are not used - e.g. The
		 * minimum and maximum of a charger is 6 and 32 Ampere. Because the default unit
		 * of all OpenEMS calculations is power, the real minimum for charging on one
		 * Phase is not 4140 Watt but 1380 Watt (Or in current 6A|0A|0A).
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: W
		 * </ul>
		 */
		MINIMUM_HARDWARE_POWER(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Maximum hardware power in W calculated with the current used Phases, used for
		 * the boundaries of the monitoring.
		 * 
		 * <p>
		 * This maximum limit is dynamically set depending on the current used
		 * {@link #PHASES} and the {@link #FIXED_MINIMUM_HARDWARE_POWER}, to be able to
		 * react on different power limits if some of the Phases are not used - e.g. The
		 * minimum and maximum of a charger is 6 and 32 Ampere. Because the default unit
		 * of all OpenEMS calculations is power, the real maximum for charging on one
		 * Phase is not 22080 Watt but 7360 Watt (Or in current 32A|0A|0A).
		 * 
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: W
		 * </ul>
		 */
		MAXIMUM_HARDWARE_POWER(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Maximum Power defined by software.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: W
		 * </ul>
		 */
		MAXIMUM_POWER(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Minimum Power defined by software.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: W
		 * </ul>
		 */
		MINIMUM_POWER(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Energy that was charged during the current or last Session.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Type: Integer
		 * <li>Unit: Wh
		 * </ul>
		 */
		ENERGY_SESSION(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.WATT_HOURS) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Active Consumption Energy.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Type: Integer
		 * <li>Unit: Wh
		 * </ul>
		 */
		ACTIVE_CONSUMPTION_ENERGY(Doc.of(OpenemsType.LONG) //
				.unit(Unit.CUMULATED_WATT_HOURS) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH)), //

		/**
		 * Failed state channel for a failed communication to the EVCS.
		 *
		 * <ul>
		 * <li>Interface: Evcs
		 * <li>Readable
		 * <li>Level: FAULT
		 * </ul>
		 */
		CHARGINGSTATION_COMMUNICATION_FAILED(Doc.of(Level.FAULT) //
				.accessMode(AccessMode.READ_ONLY) //
				.persistencePriority(PersistencePriority.HIGH) //
				.text("Chargingstation Communication Failed " //
						+ "| Keine Verbindung zur Ladestation " //
						+ "| Bitte überprüfen Sie die Kommunikationsverbindung zu der Ladestation")); //

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	/**
	 * Gets the Channel for {@link ChannelId#STATUS}.
	 *
	 * @return the Channel
	 */
	public default Channel<Status> getStatusChannel() {
		return this.channel(ChannelId.STATUS);
	}

	/**
	 * Gets the Status of the EVCS charging station. See {@link ChannelId#STATUS}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Status getStatus() {
		return this.getStatusChannel().value().asEnum();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#STATUS} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setStatus(Status value) {
		this.getStatusChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#CHARGE_POWER}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getChargePowerChannel() {
		return this.channel(ChannelId.CHARGE_POWER);
	}

	/**
	 * Gets the Charge Power in [W]. See {@link ChannelId#CHARGE_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getChargePower() {
		return this.getChargePowerChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#CHARGE_POWER}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setChargePower(Integer value) {
		this.getChargePowerChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#CHARGE_POWER}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setChargePower(int value) {
		this.getChargePowerChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#CHARGING_TYPE}.
	 *
	 * @return the Channel
	 */
	public default Channel<ChargingType> getChargingTypeChannel() {
		return this.channel(ChannelId.CHARGING_TYPE);
	}

	/**
	 * Gets the Type of charging. See {@link ChannelId#CHARGING_TYPE}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default ChargingType getChargingType() {
		return this.getChargingTypeChannel().value().asEnum();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#CHARGING_TYPE}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setChargingType(ChargingType value) {
		this.getChargingTypeChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#CURRENT}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getCurrentChannel() {
		return this.channel(ChannelId.CURRENT);
	}

	/**
	 * Gets the Current. See {@link ChannelId#MINIMUM_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getCurrent() {
		return this.getCurrentChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#CURRENT} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setCurrent(Integer value) {
		this.getCurrentChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#CURRENT_L1}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getCurrentL1Channel() {
		return this.channel(ChannelId.CURRENT_L1);
	}

	/**
	 * Gets the Current. See {@link ChannelId#CURRENT_L1}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getCurrentL1() {
		return this.getCurrentL1Channel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#CURRENT_L1}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setCurrentL1(Integer value) {
		this.getCurrentL1Channel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#CURRENT_L2}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getCurrentL2Channel() {
		return this.channel(ChannelId.CURRENT_L2);
	}

	/**
	 * Gets the Current. See {@link ChannelId#CURRENT_L2}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getCurrentL2() {
		return this.getCurrentL2Channel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#CURRENT_L3}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setCurrentL2(Integer value) {
		this.getCurrentL2Channel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#CURRENT_L3}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getCurrentL3Channel() {
		return this.channel(ChannelId.CURRENT_L3);
	}

	/**
	 * Gets the Current. See {@link ChannelId#CURRENT_L3}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getCurrentL3() {
		return this.getCurrentL3Channel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#CURRENT_L3}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setCurrentL3(Integer value) {
		this.getCurrentL3Channel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#PHASES}.
	 *
	 * @return the Channel
	 */
	public default EnumReadChannel getPhasesChannel() {
		return this.channel(ChannelId.PHASES);
	}

	/**
	 * Gets the current Phases definition. See {@link ChannelId#PHASES}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Phases getPhases() {
		return this.getPhasesChannel().value().asEnum();
	}

	/**
	 * Gets the Count of phases, the EV is charging with. See
	 * {@link ChannelId#PHASES}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default int getPhasesAsInt() {
		return this.getPhasesChannel().value().asEnum().getValue();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#PHASES} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setPhases(Phases value) {
		this.getPhasesChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#PHASES} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setPhases(Integer value) {
		if (value == null || value == 0) {
			this._setPhases(Phases.THREE_PHASE);
			return;
		}
		switch (value) {
		case 1:
			this._setPhases(Phases.ONE_PHASE);
			break;
		case 2:
			this._setPhases(Phases.TWO_PHASE);
			break;
		case 3:
			this._setPhases(Phases.THREE_PHASE);
			break;
		default:
			throw new IllegalArgumentException("Value [" + value + "] for _setPhases is invalid");
		}
	}

	/**
	 * Gets the Channel for {@link ChannelId#PHASE_ROTATION}.
	 *
	 * @return the Channel
	 */
	public default EnumReadChannel getPhaseRotationChannel() {
		return this.channel(ChannelId.PHASE_ROTATION);
	}

	/**
	 * Gets the current PhaseRotation. See {@link ChannelId#PHASE_ROTATION}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default PhaseRotation getPhaseRotation() {
		return this.getPhaseRotationChannel().value().asEnum();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#PHASES} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setPhaseRotation(PhaseRotation value) {
		this.getPhaseRotationChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#FIXED_MINIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getFixedMinimumHardwarePowerChannel() {
		return this.channel(ChannelId.FIXED_MINIMUM_HARDWARE_POWER);
	}

	/**
	 * Gets the fixed minimum power valid by the hardware in [W]. See
	 * {@link ChannelId#FIXED_MINIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getFixedMinimumHardwarePower() {
		return this.getFixedMinimumHardwarePowerChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#FIXED_MINIMUM_HARDWARE_POWER} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setFixedMinimumHardwarePower(Integer value) {
		this.getFixedMinimumHardwarePowerChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#FIXED_MINIMUM_HARDWARE_POWER} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setFixedMinimumHardwarePower(int value) {
		this.getFixedMinimumHardwarePowerChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#FIXED_MAXIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getFixedMaximumHardwarePowerChannel() {
		return this.channel(ChannelId.FIXED_MAXIMUM_HARDWARE_POWER);
	}

	/**
	 * Gets the fixed maximum power valid by the hardware in [W]. See
	 * {@link ChannelId#FIXED_MAXIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getFixedMaximumHardwarePower() {
		return this.getFixedMaximumHardwarePowerChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#FIXED_MAXIMUM_HARDWARE_POWER} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setFixedMaximumHardwarePower(Integer value) {
		this.getFixedMaximumHardwarePowerChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#FIXED_MAXIMUM_HARDWARE_POWER} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setFixedMaximumHardwarePower(int value) {
		this.getFixedMaximumHardwarePowerChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#MAXIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getMaximumHardwarePowerChannel() {
		return this.channel(ChannelId.MAXIMUM_HARDWARE_POWER);
	}

	/**
	 * Gets the Maximum Power valid by the hardware in [W]. See
	 * {@link ChannelId#MAXIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getMaximumHardwarePower() {
		return this.getMaximumHardwarePowerChannel().value();
	}

	/**
	 * Gets the Channel for {@link ChannelId#MINIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getMinimumHardwarePowerChannel() {
		return this.channel(ChannelId.MINIMUM_HARDWARE_POWER);
	}

	/**
	 * Gets the Minimum Power valid by the hardware in [W]. See
	 * {@link ChannelId#MINIMUM_HARDWARE_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getMinimumHardwarePower() {
		return this.getMinimumHardwarePowerChannel().value();
	}

	/**
	 * Gets the Channel for {@link ChannelId#MAXIMUM_POWER}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getMaximumPowerChannel() {
		return this.channel(ChannelId.MAXIMUM_POWER);
	}

	/**
	 * Gets the Maximum Power valid by software in [W]. See
	 * {@link ChannelId#MAXIMUM_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getMaximumPower() {
		return this.getMaximumPowerChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#MAXIMUM_POWER}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setMaximumPower(Integer value) {
		this.getMaximumPowerChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#MAXIMUM_POWER}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setMaximumPower(int value) {
		this.getMaximumPowerChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#MINIMUM_POWER}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getMinimumPowerChannel() {
		return this.channel(ChannelId.MINIMUM_POWER);
	}

	/**
	 * Gets the Minimum Power valid by software in [W]. See
	 * {@link ChannelId#MINIMUM_POWER}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getMinimumPower() {
		return this.getMinimumPowerChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#MINIMUM_POWER}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setMinimumPower(Integer value) {
		this.getMinimumPowerChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#MINIMUM_POWER}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setMinimumPower(int value) {
		this.getMinimumPowerChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#ENERGY_SESSION}.
	 *
	 * @return the Channel
	 */
	public default IntegerReadChannel getEnergySessionChannel() {
		return this.channel(ChannelId.ENERGY_SESSION);
	}

	/**
	 * Gets the Energy that was charged during the current or last Session in [Wh].
	 * See {@link ChannelId#ENERGY_SESSION}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Integer> getEnergySession() {
		return this.getEnergySessionChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#ENERGY_SESSION}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setEnergySession(Integer value) {
		this.getEnergySessionChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on {@link ChannelId#ENERGY_SESSION}
	 * Channel.
	 *
	 * @param value the next value
	 */
	public default void _setEnergySession(int value) {
		this.getEnergySessionChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#ACTIVE_CONSUMPTION_ENERGY}.
	 *
	 * @return the Channel
	 */
	public default LongReadChannel getActiveConsumptionEnergyChannel() {
		return this.channel(ChannelId.ACTIVE_CONSUMPTION_ENERGY);
	}

	/**
	 * Gets the Active Consumption Energy in [Wh_Σ]. This relates to negative
	 * ACTIVE_POWER. See {@link ChannelId#ACTIVE_CONSUMPTION_ENERGY}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Long> getActiveConsumptionEnergy() {
		return this.getActiveConsumptionEnergyChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#ACTIVE_CONSUMPTION_ENERGY} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setActiveConsumptionEnergy(Long value) {
		this.getActiveConsumptionEnergyChannel().setNextValue(value);
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#ACTIVE_CONSUMPTION_ENERGY} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setActiveConsumptionEnergy(long value) {
		this.getActiveConsumptionEnergyChannel().setNextValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#CHARGINGSTATION_COMMUNICATION_FAILED}.
	 *
	 * @return the Channel
	 */
	public default StateChannel getChargingstationCommunicationFailedChannel() {
		return this.channel(ChannelId.CHARGINGSTATION_COMMUNICATION_FAILED);
	}

	/**
	 * Gets the Failed state channel for a failed communication to the EVCS. See
	 * {@link ChannelId#CHARGINGSTATION_COMMUNICATION_FAILED}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Boolean> getChargingstationCommunicationFailed() {
		return this.getChargingstationCommunicationFailedChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#CHARGINGSTATION_COMMUNICATION_FAILED} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setChargingstationCommunicationFailed(boolean value) {
		this.getChargingstationCommunicationFailedChannel().setNextValue(value);
	}
	
	/**
	 * Adds listeners mapping meter channels into respective evcs channels in case an Evcs is measured by an external meter.
	 * 
	 * @param evcs the {@link Evcs}
	 * @param meter the {@link ElectricityMeter} measuring the Evcs
	 */
	public static void addMeterListeners(Evcs evcs, ElectricityMeter meter) {
		meter.getActivePowerChannel().onSetNextValue(newValue -> {
			evcs.getChargePowerChannel().setNextValue(newValue);
		});
		meter.getCurrentChannel().onSetNextValue(newValue -> {
			evcs.getCurrentChannel().setNextValue(newValue);
		});
		meter.getCurrentL1Channel().onSetNextValue(newValue -> {
			evcs.getCurrentL1Channel().setNextValue(newValue);
		});
		meter.getCurrentL2Channel().onSetNextValue(newValue -> {
			evcs.getCurrentL2Channel().setNextValue(newValue);
		});
		meter.getCurrentL3Channel().onSetNextValue(newValue -> {
			evcs.getCurrentL3Channel().setNextValue(newValue);
		});
		meter.getActiveConsumptionEnergyChannel().onSetNextValue(newValue -> {
			evcs.getActiveConsumptionEnergyChannel().setNextValue(newValue);
		});
	}

	/**
	 * Adds onSetNextValue listeners for minimum and maximum hardware power.
	 * 
	 * <p>
	 * Since the minimum and maximum power strongly depends on the connected
	 * vehicle, this automatically adjusts it to the currently used phases.
	 * 
	 * @param evcs evcs
	 */
	public static void addCalculatePowerLimitListeners(Evcs evcs) {

		final Consumer<Value<Integer>> calculateHardwarePowerLimits = ignore -> {

			Phases phases = evcs.getPhasesChannel().getNextValue().asEnum();
			int fixedMaximum = evcs.getFixedMaximumHardwarePowerChannel().getNextValue()
					.orElse(DEFAULT_MAXIMUM_HARDWARE_POWER);
			int fixedMinimum = evcs.getFixedMinimumHardwarePowerChannel().getNextValue()
					.orElse(DEFAULT_MINIMUM_HARDWARE_POWER);

			var maximumPower = phases.getFromThreePhase(fixedMaximum);
			var minimumPower = phases.getFromThreePhase(fixedMinimum);

			evcs.getMaximumHardwarePowerChannel().setNextValue(maximumPower);
			evcs.getMinimumHardwarePowerChannel().setNextValue(minimumPower);
		};

		evcs.getFixedMaximumHardwarePowerChannel().onSetNextValue(calculateHardwarePowerLimits);
		evcs.getFixedMinimumHardwarePowerChannel().onSetNextValue(calculateHardwarePowerLimits);
		evcs.getPhasesChannel().onSetNextValue(calculateHardwarePowerLimits);
	}

	/**
	 * Used for Modbus/TCP Api Controller. Provides a Modbus table for the Channels
	 * of this Component.
	 *
	 * @param accessMode filters the Modbus-Records that should be shown
	 * @return the {@link ModbusSlaveNatureTable}
	 */
	public static ModbusSlaveNatureTable getModbusSlaveNatureTable(AccessMode accessMode) {
		return ModbusSlaveNatureTable.of(Evcs.class, accessMode, 100) //
				.channel(0, ChannelId.STATUS, ModbusType.UINT16) //
				.channel(1, ChannelId.CHARGE_POWER, ModbusType.UINT16) //
				.channel(2, ChannelId.CHARGING_TYPE, ModbusType.UINT16) //
				.channel(3, ChannelId.PHASES, ModbusType.UINT16) //
				.channel(4, ChannelId.MAXIMUM_HARDWARE_POWER, ModbusType.UINT16) //
				.channel(5, ChannelId.MINIMUM_HARDWARE_POWER, ModbusType.UINT16) //
				.channel(6, ChannelId.MAXIMUM_POWER, ModbusType.UINT16) //
				.channel(7, ChannelId.ENERGY_SESSION, ModbusType.UINT16) //
				.channel(8, ChannelId.CHARGINGSTATION_COMMUNICATION_FAILED, ModbusType.UINT16) //
				.channel(9, ChannelId.FIXED_MINIMUM_HARDWARE_POWER, ModbusType.UINT16) //
				.channel(10, ChannelId.FIXED_MAXIMUM_HARDWARE_POWER, ModbusType.UINT16) //
				.channel(11, ChannelId.MINIMUM_POWER, ModbusType.UINT16) //
				.channel(12, ChannelId.ACTIVE_CONSUMPTION_ENERGY, ModbusType.UINT32) //
				.channel(14, ChannelId.PHASE_ROTATION, ModbusType.ENUM16) //
				.channel(15, ChannelId.CURRENT, ModbusType.UINT16) //
				.channel(16, ChannelId.CURRENT_L1, ModbusType.UINT16) //
				.channel(17, ChannelId.CURRENT_L2, ModbusType.UINT16) //
				.channel(18, ChannelId.CURRENT_L3, ModbusType.UINT16) //
				.build();
		// TODO open question to community? Any problems if we add CURRENT and Phase
		// rotation as address 13..?
	}
}
