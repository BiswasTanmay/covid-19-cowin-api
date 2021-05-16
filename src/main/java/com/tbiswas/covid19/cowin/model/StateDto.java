package com.tbiswas.covid19.cowin.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class StateDto {
	
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	public int state_id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	public String state_name;
	
	public int getState_id() {
		return state_id;
	}
	public void setState_id(int state_id) {
		this.state_id = state_id;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	
	

}
