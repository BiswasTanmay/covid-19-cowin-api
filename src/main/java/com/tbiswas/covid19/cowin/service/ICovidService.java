package com.tbiswas.covid19.cowin.service;

import java.util.List;
import java.util.Map;

import com.tbiswas.covid19.cowin.model.AvailableDto;

public interface ICovidService {

	List<AvailableDto> getDetailsFromCowin(String pincode, Map<String, String> typeMap);

}
