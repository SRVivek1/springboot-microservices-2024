package com.srvivek.sboot.ms.ces.bean;

import java.math.BigDecimal;

public class CurrencyExchange {

	private long id;
	private String from;
	private String to;
	private BigDecimal conversionMultiples;

	public CurrencyExchange() {
		super();
	}

	public CurrencyExchange(long id, String from, String to, BigDecimal conversionMultiples) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.conversionMultiples = conversionMultiples;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getConversionMultiples() {
		return conversionMultiples;
	}

	public void setConversionMultiples(BigDecimal conversionMultiples) {
		this.conversionMultiples = conversionMultiples;
	}

	@Override
	public String toString() {
		return "CurrencyExchange [id=" + id + ", from=" + from + ", to=" + to + ", conversionMultiples="
				+ conversionMultiples + "]";
	}

}
