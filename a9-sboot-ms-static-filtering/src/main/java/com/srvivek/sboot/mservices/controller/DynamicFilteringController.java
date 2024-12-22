package com.srvivek.sboot.mservices.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.srvivek.sboot.mservices.bean.SomeBeanDynamicFilter;

@RestController
public class DynamicFilteringController {

	@GetMapping("/dyna-filtering")
	public MappingJacksonValue filtering() {

		// Dynamic filtering
		final SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("field2",
				"field4", "field6");

		final SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider()
				.addFilter("dyna-filter-for-somebean", simpleBeanPropertyFilter);

		final SomeBeanDynamicFilter SomeBeanDynamicFilter = new SomeBeanDynamicFilter("Value-1", "Value-2", "Value-3",
				"Value-4", "Value-5", "Value-6");
		final MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SomeBeanDynamicFilter);
		mappingJacksonValue.setFilters(simpleFilterProvider);

		return mappingJacksonValue;
	}

	@GetMapping("/dyna-filtering-list")
	public MappingJacksonValue filteringList() {

		// Dynamic filtering
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("field1",
				"field3", "field5", "field6");
		FilterProvider simpleFilterProvider = new SimpleFilterProvider().addFilter("dyna-filter-for-somebean",
				simpleBeanPropertyFilter);

		List<SomeBeanDynamicFilter> SomeBeanDynamicFilterList = Arrays.asList(
				new SomeBeanDynamicFilter("Value-1", "Value-2", "Value-3", "Value-4", "Value-5", "Value-6"),
				new SomeBeanDynamicFilter("Value-11", "Value-22", "Value-33", "Value-44", "Value-55", "Value-66"),
				new SomeBeanDynamicFilter("Value-111", "Value-222", "Value-333", "Value-444", "Value-555",
						"Value-666"));

		final MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SomeBeanDynamicFilterList);
		mappingJacksonValue.setFilters(simpleFilterProvider);

		return mappingJacksonValue;
	}
}
