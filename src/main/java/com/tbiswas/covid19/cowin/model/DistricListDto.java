package com.tbiswas.covid19.cowin.model;

import lombok.Data;

@Data
public class DistricListDto {
	
	DistrictDto[] districts;

	public DistrictDto[] getDistricts() {
		return districts;
	}

	public void setDistricts(DistrictDto[] districts) {
		this.districts = districts;
	}
	
	

}
