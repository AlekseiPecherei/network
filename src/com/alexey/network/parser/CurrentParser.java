package com.alexey.network.parser;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CurrentParser extends Parcer {
	private static final String ROOT_ELEMENT = "detailDay";
	private static final String WEATHER_TAG = "weather";
	private static final String FORECAST_TAG = "forecast";
	private static final String FORECAST_MORE_TAG = "forecastMore";

	private Element mRootElement, mWeather, mForecast, mForecastMore;

	public CurrentParser() throws ParserConfigurationException {
	}

	@Override
	public Document prepareDocument() {
		mRootElement.appendChild(mWeather);
		mRootElement.appendChild(mForecast);
		mRootElement.appendChild(mForecastMore);
		return xml;
	}

	public void createRootDayBlock(String name) {
		mRootElement = xml.createElement(ROOT_ELEMENT);

		Attr day = xml.createAttribute(KEY_DETAIL_DAY_ATTR);
		day.setValue(name);
		mRootElement.setAttributeNode(day);

		xml.appendChild(mRootElement);
	}

	public void addWeatherBlock(HashMap<String, String> values) {
		mWeather = xml.createElement(WEATHER_TAG);
		Attr[] attributes = { xml.createAttribute(KEY_WEATHER_ICON_SRC), xml.createAttribute(KEY_WEATHER_TEMP),
				xml.createAttribute(KEY_WEATHER_DESC) };

		for (Attr a : attributes) {
			String value = values.get(a.getName());
			a.setValue(value);
			mWeather.setAttributeNode(a);
		}
	}

	public void addForecastBlock(HashMap<String, String> attributes) {
		mForecast = xml.createElement(FORECAST_TAG);
		Element[] attrElements = { xml.createElement(KEY_FORECAST_FEEL), xml.createElement(KEY_FORECAST_WIND),
				xml.createElement(KEY_FORECAST_PRESSURE), xml.createElement(KEY_FORECAST_HIMIDATY),
				xml.createElement(KEY_FORECAST_WATER_TEMP), xml.createElement(KEY_FORECAST_HEOM) };

		for (Element attr : attrElements) {
			String value = attributes.get(attr.getTagName());
			attr.appendChild(xml.createTextNode(value));
			mForecast.appendChild(attr);
		}
	}

	public void addForecastMoreBlock(HashMap<String, String> attributes) {
		mForecastMore = xml.createElement(FORECAST_MORE_TAG);
		Element[] attrElements = { xml.createElement(KEY_FORECAST_SUNRISE), xml.createElement(KEY_FORECAST_SUNSET),
				xml.createElement(KEY_FORECAST_DAY_DURATION), xml.createElement(KEY_FORECAST_MOON) };

		for (Element attr : attrElements) {
			String value = attributes.get(attr.getTagName());
			attr.appendChild(xml.createTextNode(value));
			mForecastMore.appendChild(attr);
		}
	}

}
