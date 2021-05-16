package com.tbiswas.covid19.cowin.model;

import java.util.List;

import lombok.Data;

@Data
public class AvailableDto {
	private String name;
	private String state;
	private String district;

	private List<AvailableSlot> slots;

	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<AvailableSlot> getSlots() {
		return slots;
	}

	public void setSlots(List<AvailableSlot> slots) {
		this.slots = slots;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	
	
	
}
