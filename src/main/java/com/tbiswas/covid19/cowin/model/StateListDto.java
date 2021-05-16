package com.tbiswas.covid19.cowin.model;

import lombok.Data;

@Data
public class StateListDto {
	
	public StateDto[] states;

	public StateDto[] getStates() {
		return states;
	}

	public void setStates(StateDto[] states) {
		this.states = states;
	}
	
	

}
