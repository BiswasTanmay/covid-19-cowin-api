package com.tbiswas.covid19.cowin.model;

import lombok.Data;

@Data
public class AvailableSlot {
	
	private String date;
	private int seat;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
	
	

}
