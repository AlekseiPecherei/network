package com.alexey.network.encoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alexey.network.constants.Keys;
import com.alexey.network.constants.Tags;

public class DetailForecastEncoder extends Encoder {	
	private Element mRootElement;		//<DetailDay> root block
	private List<Element> mDetailTimes;	//<DetailTime> blocks
	private List<Element> mForecasts;	//<Forecast> blocks
	private List<Element> mWeathers;	//<Weather> blocks
	
	public DetailForecastEncoder() throws ParserConfigurationException {
		mDetailTimes = new ArrayList<>();
		mForecasts = new ArrayList<>();
		mWeathers = new ArrayList<>();
	}
	
	public void createRootDayBlock(String name) {
		mRootElement = xml.createElement(Tags.ROOT_ELEMENT);
		
		Attr day = xml.createAttribute(Keys.KEY_DETAIL_DAY_ATTR);
		day.setValue(name);
		mRootElement.setAttributeNode(day);		
		
		xml.appendChild(mRootElement);
	}
	
	public void addDetailTimeBlock(String text) {
		Element detail = xml.createElement(Tags.DETAIL_TIME_TAG);
		
		Attr value = xml.createAttribute(Keys.KEY_DETAIL_TIME_ATTR);
		value.setValue(text);
		detail.setAttributeNode(value);
		
		mDetailTimes.add(detail);
	}
	
	public void addForecastBlock(HashMap<String, String> attributes) {
		Element currentForecast = xml.createElement(Tags.FORECAST_TAG);	
		Element[] attrElements = {
			xml.createElement(Keys.KEY_FORECAST_FEEL),
			xml.createElement(Keys.KEY_FORECAST_WIND),
			xml.createElement(Keys.KEY_FORECAST_PRESSURE),
			xml.createElement(Keys.KEY_FORECAST_HIMIDATY),
		};
				
		for(Element attr : attrElements) { 
			String value = attributes.get(attr.getTagName());
			attr.appendChild(xml.createTextNode(value));
			
			currentForecast.appendChild(attr);
		}
		
		mForecasts.add(currentForecast);
	}
	
	public void addWeatherBlock(HashMap<String, String> values) {
		Element currentWeather = xml.createElement(Tags.WEATHER_TAG);
		Attr[] attributes = {
			xml.createAttribute(Keys.KEY_WEATHER_ICON_SRC),
			xml.createAttribute(Keys.KEY_WEATHER_TEMP),
			xml.createAttribute(Keys.KEY_WEATHER_DESC)
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