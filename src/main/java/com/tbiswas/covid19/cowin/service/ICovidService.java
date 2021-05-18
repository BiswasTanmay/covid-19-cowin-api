package com.tbiswas.covid19.cowin.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.tbiswas.covid19.cowin.model.AvailableDto;
import com.tbiswas.covid19.cowin.model.CowinData;

public interface ICovidService {

	List<AvailableDto> getDetailsFromCowin(String pincode, Map<String, String> typeMap);


}
