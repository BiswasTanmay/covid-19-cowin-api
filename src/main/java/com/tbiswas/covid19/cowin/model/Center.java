package com.tbiswas.covid19.cowin.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Center {

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String center_id;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String address;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String state_name;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String district_name;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String block_name;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	private int pincode;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	private int lat;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String longitude;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String from;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String to;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String fee_type;

	public Session[] sessions;

	public String getCenter_id() {
		return center_id;
	}

	public void setCenter_id(String center_id) {
		this.center_id = center_id;
	}

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

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getBlock_name() {
		return block_name;
	}

	public void setBlock_name(String block_name) {
		this.block_name = block_name;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Session[] getSessions() {
		return sessions;
	}

	public void setSessions(Session[] sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return "Center [center_id=" + center_id + ", name=" + name + ", address=" + address + ", state_name="
				+ state_name + ", district_name=" + district_name + ", block_name=" + block_name + ", pincode="
				+ pincode + ", lat=" + lat + ", longitude=" + longitude + ", from=" + from + ", to=" + to
				+ ", fee_type=" + fee_type + ", sessions=" + Arrays.toString(sessions) + "]";
	}

	public Center(String center_id, String name, String address, String state_name, String district_name,
			String block_name, int pincode, int lat, String longitude, String from, String to, String fee_type,
			Session[] sessions) {
		super();
		this.center_id = center_id;
		this.name = name;
		this.address = address;
		this.state_name = state_name;
		this.district_name = district_name;
		this.block_name = block_name;
		this.pincode = pincode;
		this.lat = lat;
		this.longitude = longitude;
		this.from = from;
		this.to = to;
		this.fee_type = fee_type;
		this.sessions = sessions;
	}

	public Center() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
