package com.srvivek.sboot.msc.limitservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app-config")
@Component
public class AppServiceConfiguration {

	// it will match to property named - limits-service.minimum
	private int minimum;

	// it will match to property named - limits-service.maximum
	private int maximum;

	public AppServiceConfiguration() {
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
