package com.srvivek.sboot.mservices.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.SomeBean;

@RestController
public class FilteringController {

	@GetMapping("/filtering")
	public SomeBean filtering() {
		return new SomeBean("Value-1", "Value-2", "Value-3", "Value-4", "Value-5", "Value-6");
	}

	@GetMapping("/filtering-list")
	public List<SomeBean> filteringList() {
		return Arrays.asList(new SomeBean("Value-1", "Value-2", "Value-3", "Value-4", "Value-5", "Value-6"),
				new SomeBean("Value-11", "Value-22", "Value-33", "Value-44", "Value-55", "Value-66"),
				new SomeBean("Value-111", "Value-222", "Value-333", "Value-444", "Value-555", "Value-666"));
	}
}
