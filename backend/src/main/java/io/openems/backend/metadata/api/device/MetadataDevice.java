package io.openems.backend.metadata.api.device;

import java.util.Optional;

import com.google.gson.JsonObject;

import io.openems.common.exceptions.OpenemsException;

public interface MetadataDevice {

	Integer getId();

	Optional<Integer> getNameNumber();

	String getName();

	String getComment();

	String getState();

	String getProductType();

	JsonObject getOpenemsConfig();

	void setOpenemsConfig(JsonObject j);

	void setState(String active);

	void setSoc(int value);

	void setLastMessage();

	void setLastUpdate();

	void setIpV4(String value);

	void writeObject() throws OpenemsException;

}