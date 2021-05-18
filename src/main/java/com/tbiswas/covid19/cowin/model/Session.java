package com.tbiswas.covid19.cowin.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Session {
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String session_id;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String date;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	private int available_capacity;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	private int min_age_limit;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String vaccine;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	private int available_capacity_dose1;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	private int available_capacity_dose2;

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getAvailable_capacity() {
		return available_capacity;
	}

	public void setAvailable_capacity(int available_capacity) {
		this.available_capacity = available_capacity;
	}

	public int getMin_age_limit() {
		return min_age_limit;
	}

	public void setMin_age_limit(int min_age_limit) {
		this.min_age_limit = min_age_limit;
	}

	public String getVaccine() {
		return vaccine;
	}

	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}

	public int getAvailable_capacity_dose1() {
		return available_capacity_dose1;
	}

	public void setAvailable_capacity_dose1(int available_capacity_dose1) {
		this.available_capacity_dose1 = available_capacity_dose1;
	}

	public int getAvailable_capacity_dose2() {
		return available_capacity_dose2;
	}

	public void setAvailable_capacity_dose2(int available_capacity_dose2) {
		this.available_capacity_dose2 = available_capacity_dose2;
	}

	
	
	

}
