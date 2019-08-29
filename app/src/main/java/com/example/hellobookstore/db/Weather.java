package com.example.hellobookstore.db;

public class Weather {
	private String cityName;
	private String degree;
	private String weatherInfo;


	public Weather(String cityName, String degree, String weatherInfo) {
		this.cityName = cityName;
		this.degree = degree;
		this.weatherInfo = weatherInfo;
	}

	public String getCityName() {
		return cityName;
	}

	public String getDegree() {
		return degree;
	}

	public String getWeatherInfo() {
		return weatherInfo;
	}
}
