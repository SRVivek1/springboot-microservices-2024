package com.srvivek.sboot.msc.limitservice.bean;

public class AppService {

	private int minimum;
	private int maximum;

	public AppService() {
		super();
	}

	public AppService(int minimum, int maximum) {
		super();
		this.minimum = minimum;
		this.maximum = maximum;
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
		return "AppService [minimum=" + minimum + ", maximum=" + maximum + "]";
	}

}
