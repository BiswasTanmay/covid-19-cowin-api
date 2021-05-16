package com.tbiswas.covid19.cowin.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbiswas.covid19.cowin.model.AvailableDto;
import com.tbiswas.covid19.cowin.model.AvailableSlot;
import com.tbiswas.covid19.cowin.model.Center;
import com.tbiswas.covid19.cowin.model.CowinData;
import com.tbiswas.covid19.cowin.model.Session;
import com.tbiswas.covid19.cowin.service.ICovidService;

@Service
public class CovidServiceImpl implements ICovidService {

	@Override
	public List<AvailableDto> getDetailsFromCowin(String pincode, Map<String, String> typeMap) {
		List<AvailableDto> availableList = new ArrayList<AvailableDto>();
		CowinData participantJsonList = new CowinData();
		try {
			// Date date = new Date();
			Date date = new Date();
			SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");

			List<Center> allcentrList = new ArrayList<Center>();
			List<Center> centrList = new ArrayList<Center>();

			do {
				String stringDate = DateFor.format(date);
				centrList = new ArrayList<Center>();
				participantJsonList = getdata(pincode, stringDate, typeMap);
				centrList = Arrays.asList(participantJsonList.getCenters());
				allcentrList.addAll(centrList);

				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 7);
				date = cal.getTime();

			} while (centrList.size() > 0);

			for (Center c : allcentrList) {

				List<AvailableSlot> slots = new ArrayList<AvailableSlot>();
				for (Session s : c.getSessions()) {
					if (s.getAvailable_capacity() > 0) {
						AvailableSlot avs = new AvailableSlot();
						avs.setDate(s.getDate());
						avs.setSeat(s.getAvailable_capacity());
						slots.add(avs);
					} else {
						continue;
					}
				}

				if (slots.size() > 0) {
					AvailableDto av = new AvailableDto();
					av.setName(c.getName());
					av.setAddress(c.getAddress() + ", " + c.getBlock_name() + ", " + c.getDistrict_name() + ", "
							+ c.getPincode() + ", " + c.getState_name());

					av.setSlots(slots);
					av.setDistrict(c.getDistrict_name());
					av.setState(c.getState_name());
					availableList.add(av);
				} else {
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return availableList;
	}

	private CowinData getdata(String pincode, String stringDate, Map<String, String> typeMap) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CowinData participantJsonList = new CowinData();
		Document document = null;
		if (typeMap.get(pincode).equalsIgnoreCase("N")) {
			document = Jsoup
					.connect("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="
							+ pincode + "&date=" + stringDate)
					.ignoreContentType(true).get();
		} else {
			document = Jsoup.connect(
					"https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="
							+ pincode + "&date=" + stringDate)
					.ignoreContentType(true).get();
		}

		Elements bodyElement = document.select("body");
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String body = bodyElement.eachText().get(0).replaceAll("long", "longitude");
		System.out.println(body);

		participantJsonList = mapper.readValue(body, CowinData.class);

		return participantJsonList;
	}

}
