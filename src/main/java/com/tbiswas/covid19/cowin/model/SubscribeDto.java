package com.tbiswas.covid19.cowin.model;

import lombok.Data;

@Data
public class SubscribeDto {
	private String email;
	private String pin;
	private String type;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
