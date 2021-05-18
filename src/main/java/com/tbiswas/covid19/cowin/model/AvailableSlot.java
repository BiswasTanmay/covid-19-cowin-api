package com.tbiswas.covid19.cowin.model;

import lombok.Data;

@Data
public class AvailableSlot {

	private String date;
	private int seat;
	private String vaccine;
	private int min_age;

	private int dose1;
	private int dose2;

	public int getDose1() {
		return dose1;
	}

	public void setDose1(int dose1) {
		this.dose1 = dose1;
	}

	public int getDose2() {
		return dose2;
	}

	public void setDose2(int dose2) {
		this.dose2 = dose2;
	}

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

	public String getVaccine() {
		return vaccine;
	}

	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}

	public int getMin_age() {
		return min_age;
	}

	public void setMin_age(int min_age) {
		this.min_age = min_age;
	}

}
