package io.openems.edge.evcs.alfen;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Level;
import io.openems.common.channel.PersistencePriority;
import io.openems.common.channel.Unit;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.common.component.OpenemsComponent;

public interface EvcsAlfen extends OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {

		METER_STATE(Doc.of(OpenemsType.INTEGER) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Meter state")), //
		METER_LAST_VALUE_TIMESTAMP(Doc.of(OpenemsType.INTEGER) //
				.unit(Unit.MILLISECONDS) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Milliseconds since last received measurement")), //
		METER_TYPE(Doc.of(OpenemsType.INTEGER) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Meter type")), //
		VOLTAGE_L1(Doc.of(OpenemsType.FLOAT).unit(Unit.VOLT) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Voltage Phase L1")), //
		VOLTAGE_L2(Doc.of(OpenemsType.FLOAT).unit(Unit.VOLT) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Voltage Phase L2")), //
		VOLTAGE_L3(Doc.of(OpenemsType.FLOAT).unit(Unit.VOLT) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Voltage Phase L3")), //
		RAW_CURRENT_L1(Doc.of(OpenemsType.FLOAT).unit(Unit.MILLIAMPERE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Current as metered on Input Phase L1")), //
		RAW_CURRENT_L2(Doc.of(OpenemsType.FLOAT).unit(Unit.MILLIAMPERE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Current as metered on Input Phase L2")), //
		RAW_CURRENT_L3(Doc.of(OpenemsType.FLOAT).unit(Unit.MILLIAMPERE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Current as metered on Input Phase L3")), //
		CURRENT_N(Doc.of(OpenemsType.FLOAT).unit(Unit.AMPERE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Current N")), //
		POWER_FACTOR_L1(Doc.of(OpenemsType.FLOAT).unit(Unit.NONE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Power factor L1")), //
		POWER_FACTOR_L2(Doc.of(OpenemsType.FLOAT).unit(Unit.NONE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Power factor L2")), //
		POWER_FACTOR_L3(Doc.of(OpenemsType.FLOAT).unit(Unit.NONE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Power factor L3")), //
		POWER_FACTOR_SUM(Doc.of(OpenemsType.FLOAT).unit(Unit.NONE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Power factor sum")), //
		FREQUENCY(Doc.of(OpenemsType.FLOAT).unit(Unit.HERTZ) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Frequency")), //
		CHARGE_POWER_L1(Doc.of(OpenemsType.FLOAT).unit(Unit.WATT) //
				.persistencePriority(PersistencePriority.HIGH) //
				.text("Charge Power L1")), //
		CHARGE_POWER_L2(Doc.of(OpenemsType.FLOAT).unit(Unit.WATT) //
				.persistencePriority(PersistencePriority.HIGH) //
				.text("Charge Power L2")), //
		CHARGE_POWER_L3(Doc.of(OpenemsType.FLOAT).unit(Unit.WATT) //
				.persistencePriority(PersistencePriority.HIGH) //
				.text("Charge Power L3")), //
		APPARENT_POWER_SUM(Doc.of(OpenemsType.FLOAT).unit(Unit.VOLT_AMPERE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Apparent Power sum")), //
		REACTIVE_POWER_SUM(Doc.of(OpenemsType.FLOAT).unit(Unit.VOLT_AMPERE_REACTIVE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Reactive Power sum")), //

		ENERGY_DELIVERED_SUM(Doc.of(OpenemsType.FLOAT).unit(Unit.WATT_HOURS) //
				.persistencePriority(PersistencePriority.LOW).text("Real energy delivered sum")), //

		ENERGY_CONSUMED_SUM(Doc.of(OpenemsType.FLOAT).unit(Unit.WATT_HOURS) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Real energy consumed sum")), //

		APPARENT_ENERGY_SUM(Doc.of(OpenemsType.FLOAT).unit(Unit.VOLT_AMPERE_HOURS) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Apparent Power sum")), //

		REACTIVE_ENERGY_SUM(Doc.of(OpenemsType.FLOAT).unit(Unit.VOLT_AMPERE_REACTIVE_HOURS) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Reactive Power sum")), //
		AVAILABILITY(Doc.of(OpenemsType.BOOLEAN) //
				.persistencePriority(PersistencePriority.MEDIUM) //
				.text("Availability")), //
		/**
		 * See Modbus specification for details on the Mode 3 state.
		 */
		MODE_3_STATE(Doc.of(OpenemsType.STRING) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Mode 3 state")), //
		ACTUAL_APPLIED_MAX_CURRENT(Doc.of(OpenemsType.FLOAT) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Actual applied max current")), //
		MODBUS_SLAVE_MAX_CURRENT_VALID_TIME(Doc.of(OpenemsType.INTEGER).unit(Unit.SECONDS) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Remaining time before fallback to safe current")), //
		MODBUS_SLAVE_MAX_CURRENT(Doc.of(OpenemsType.FLOAT).accessMode(AccessMode.READ_WRITE).unit(Unit.AMPERE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Modbus slave max current")), //
		ACTIVE_LOAD_BALANCING_SAFE_CURRENT(Doc.of(OpenemsType.FLOAT).unit(Unit.AMPERE) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Active load balancing safe current")), //
		MODBUS_SLAVE_RECEIVED_SETPOINT_ACCOUNTED_FOR(Doc.of(OpenemsType.BOOLEAN) //
				.persistencePriority(PersistencePriority.LOW) //
				.text("Modbus slave received setpoint accounted for")), //
		CHARGE_USING_1_OR_3_PHASES(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.READ_WRITE).unit(Unit.NONE) //
				.persistencePriority(PersistencePriority.MEDIUM) //
				.text("Charge using 1 or 3 phases")), //
		ERROR(Doc.of(Level.FAULT).persistencePriority(PersistencePriority.HIGH) //
				.text("Error in the charging station.") //
		);

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	public default WriteChannel<Float> getModbusSlaveMaxCurrentChannel() {
		return this.channel(ChannelId.MODBUS_SLAVE_MAX_CURRENT);
	}

	public default void setModbusSlaveMaxCurrent(Float value) {
		this.getModbusSlaveMaxCurrentChannel().setNextValue(value);
	}

	public default void setModbusSlaveMaxCurrentWriteValue(Float value) throws OpenemsNamedException {
		this.getModbusSlaveMaxCurrentChannel().setNextWriteValue(value);
	}

	public default WriteChannel<Integer> getUsedPhasesChannel() {
		return this.channel(ChannelId.CHARGE_USING_1_OR_3_PHASES);
	}

	public default void setUsedPhases(Integer value) {
		this.getUsedPhasesChannel().setNextValue(value);
	}

	public default void setUsedPhasesWriteValue(Integer value) throws OpenemsNamedException {
		this.getUsedPhasesChannel().setNextWriteValue(value);
	}

}
