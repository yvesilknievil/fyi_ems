package io.openems.edge.ess.sungrow.enums;

import io.openems.common.types.OptionsEnum;

public enum ChargeDischargeCommand implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	CHARGE(0xAA, "Charge"), //
	DISCHARGE(0xBB, "Discharge"), //
	STOP(0xCC, "Stop") //
	;

	private final int value;
	private final String name;

	private ChargeDischargeCommand(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public OptionsEnum getUndefined() {
		return UNDEFINED;
	}

}
