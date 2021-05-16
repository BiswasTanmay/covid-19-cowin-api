package com.tbiswas.covid19.cowin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbiswas.covid19.cowin.model.AvailableDto;
import com.tbiswas.covid19.cowin.model.DistricListDto;
import com.tbiswas.covid19.cowin.model.DistrictDto;
import com.tbiswas.covid19.cowin.model.StateDto;
import com.tbiswas.covid19.cowin.model.StateListDto;
import com.tbiswas.covid19.cowin.service.ICovidService;
import com.tbiswas.covid19.cowin.service.IEmailService;

@Controller
@RequestMapping("/")
public class CovidController {

	@Autowired
	ICovidService covidServiceApi;

	@Autowired
	IEmailService emailService;

	@PostMapping(value = "cowin")
	public String searchAvilability(@RequestParam(value = "search", required = false) String pincode, Model model)
			throws Exception {
		
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put(pincode, "N");
		List<AvailableDto> availableList = covidServiceApi.getDetailsFromCowin(pincode,typeMap);
		List<StateDto> states = getStateList();

		model.addAttribute("stateList", states);
		model.addAttribute("availableList", availableList);
		return "index";
	}

	@PostMapping(value = "subscribeByPin")
	public String subscribeByPin(@RequestParam(value = "pincode", required = false) String pincode,

			@RequestParam(value = "email", required = false) String email, Model model) throws Exception {

		try {
			String type = "N";
			emailService.subscribeEmail(email, pincode,type);
		} catch (Exception e) {
			throw new Exception();
		}

		return "redirect:/cowin";

	}

	@PostMapping(value = "subscribe")
	public String subscribe(@RequestParam(value = "district_id", required = false) String district_id,
			@Validated String distBox,
			// @RequestParam String distBox,
			@RequestParam(value = "email", required = false) String email, Model model) throws Exception {

		try {
			String type = "Y";
			emailService.subscribeEmail(email, distBox,type);
		} catch (Exception e) {
			throw new Exception();
		}

		return "redirect:/cowin";

	}
	
	@PostMapping(value = "unsubscribe")
	public String unsubscribe(@RequestParam(value = "email", required = false) String email, Model model) throws Exception {

		try {
			emailService.unsubscribeEmail(email);
		} catch (Exception e) {
			throw new Exception();
		}

		return "redirect:/cowin";

	}

	@GetMapping(value = "cowin")
	public String landingPage(Model model) throws Exception {

		List<StateDto> states = getStateList();

		model.addAttribute("stateList", states);
		return "index";
	}

	private List<StateDto> getStateList() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		StateListDto stateList = new StateListDto();
		Document document = Jsoup.connect("https://cdn-api.co-vin.in/api/v2/admin/location/states")
				.ignoreContentType(true).get();

		Elements bodyElement = document.select("body");
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String body = bodyElement.eachText().get(0);
		System.out.println(body);

		stateList = mapper.readValue(body, StateListDto.class);

		List<StateDto> states = new ArrayList<StateDto>();
		states = Arrays.asList(stateList.getStates());
		return states;
	}

	@GetMapping(value = "loadDistByState/{id}")
	@ResponseBody
	public ResponseEntity<?> loadDistByState(@PathVariable("id") int id, Model model) {
		Document document1;
		DistricListDto dist = new DistricListDto();
		List<DistrictDto> distForState = new ArrayList<DistrictDto>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			String stateId = String.valueOf(id);
			document1 = Jsoup.connect("https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + stateId)
					.ignoreContentType(true).get();
			Elements bodyElement1 = document1.select("body");
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String body1 = bodyElement1.eachText().get(0);
			System.out.println(body1);

			dist = mapper.readValue(body1, DistricListDto.class);
			distForState = Arrays.asList(dist.getDistricts());
		} catch (IOException e) {
			e.printStackTrace();
		}

		model.addAttribute("dists", distForState);
		return ResponseEntity.ok(distForState);

	}

}