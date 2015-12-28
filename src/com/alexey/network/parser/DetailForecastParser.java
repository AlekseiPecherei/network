package com.alexey.network.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DetailForecastParser extends Parcer {
	private static final String ROOT_ELEMENT = "detailDay";
	private static final String DETAIL_TIME_TAG = "detailTime";
	private static final String WEATHER_TAG = "weather";
	private static final String FORECAST_TAG = "forecast";
	
	private Element mRootElement;		//<DetailDay> root block
	private List<Element> mDetailTimes;	//<DetailTime> blocks
	private List<Element> mForecasts;	//<Forecast> blocks
	private List<Element> mWeathers;	//<Weather> blocks
	
	public DetailForecastParser() throws ParserConfigurationException {
		mDetailTimes = new ArrayList<>();
		mForecasts = new ArrayList<>();
		mWeathers = new ArrayList<>();
	}
	
	public void createRootDayBlock(String name) {
		mRootElement = xml.createElement(ROOT_ELEMENT);
		
		Attr day = xml.createAttribute(KEY_DETAIL_DAY_ATTR);
		day.setValue(name);
		mRootElement.setAttributeNode(day);		
		
		xml.appendChild(mRootElement);
	}
	
	public void addDetailTimeBlock(String text) {
		Element detail = xml.createElement(DETAIL_TIME_TAG);
		
		Attr value = xml.createAttribute(KEY_DETAIL_TIME_ATTR);
		value.setValue(text);
		detail.setAttributeNode(value);
		
		mDetailTimes.add(detail);
	}
	
	public void addForecastBlock(HashMap<String, String> attributes) {
		Element currentForecast = xml.createElement(FORECAST_TAG);	
		Element[] attrElements = {
			xml.createElement(KEY_FORECAST_FEEL),
			xml.createElement(KEY_FORECAST_WIND),
			xml.createElement(KEY_FORECAST_PRESSURE),
			xml.createElement(KEY_FORECAST_HIMIDATY),
		};
				
		for(Element attr : attrElements) { 
			String value = attributes.get(attr.getTagName());
			attr.appendChild(xml.createTextNode(value));
			
			currentForecast.appendChild(attr);
		}
		
		mForecasts.add(currentForecast);
	}
	
	public void addWeatherBlock(HashMap<String, String> values) {
		Element currentWeather = xml.createElement(WEATHER_TAG);
		Attr[] attributes = {
			xml.createAttribute(KEY_WEATHER_ICON_SRC),
			xml.createAttribute(KEY_WEATHER_TEMP),
			xml.createAttribute(KEY_WEATHER_DESC)
		};
		
		for(Attr a : attributes) {	
			String value = values.get(a.getName());
			a.setValue(value);
			currentWeather.setAttributeNode(a);
		}
		
		mWeathers.add(currentWeather);
	}

	/* (non-Javadoc)
	 * @see com.alexey.network.IParcer#prepareDocument()
	 */
	@Override
	public Document prepareDocument() {
		for(Element e : mDetailTimes) {
			int index = mDetailTimes.indexOf(e);
			e.appendChild(mWeathers.get(index));
			e.appendChild(mForecasts.get(index));
			
			mRootElement.appendChild(e);
		}
		return xml;
	}
	

}