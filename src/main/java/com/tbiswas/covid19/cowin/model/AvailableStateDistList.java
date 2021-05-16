package com.tbiswas.covid19.cowin.model;

import java.util.List;

import lombok.Data;

@Data
public class AvailableStateDistList {

	public String state;
	public List<String> dist;
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<String> getDist() {
		return dist;
	}
	public void setDist(List<String> dist) {
		this.dist = dist;
	}

}
