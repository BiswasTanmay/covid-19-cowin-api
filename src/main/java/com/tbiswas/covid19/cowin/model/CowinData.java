package com.tbiswas.covid19.cowin.model;

import lombok.Data;

@Data
public class CowinData {
	
	public Center [] centers;

	public Center[] getCenters() {
		return centers;
	}

	public void setCenters(Center[] centers) {
		this.centers = centers;
	}

}
