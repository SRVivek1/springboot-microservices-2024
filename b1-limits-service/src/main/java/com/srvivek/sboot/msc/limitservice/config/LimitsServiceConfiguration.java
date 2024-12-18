package com.srvivek.sboot.msc.limitservice.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "limits-service")
@Component
public class LimitsServiceConfiguration {

	private int minimum;
	private int maximum;

	public LimitsServiceConfiguration() {
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	@Override
	public String toString() {
		return "LimitsServiceConfiguration [minimum=" + minimum + ", maximum=" + maximum + "]";
	}

}
