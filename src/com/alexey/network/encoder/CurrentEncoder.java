package com.alexey.network.encoder;

import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alexey.network.constants.Keys;
import com.alexey.network.constants.Tags;

public class CurrentEncoder extends Encoder {
	public CurrentEncoder() throws ParserConfigurationException {}

	private Element mRootElement, mWeather, mForecast, mForecastMore;

	@Override
	public Document prepareDocument() {
		mRootElement.appendChild(mWeather);
		mRootElement.appendChild(mForecast);
		mRootElement.appendChild(mForecastMore);
		return xml;
	}

	public void createRootDayBlock(String name) {
		mRootElement = xml.createElement(Tags.ROOT_ELEMENT);

		Attr day = xml.createAttribute(Keys.KEY_DETAIL_DAY_ATTR);
		day.setValue(name);
		mRootElement.setAttributeNode(day);

		xml.appendChild(mRootElement);
	}

	public void addWeatherBlock(HashMap<String, String> values) {
		mWeather = xml.createElement(Tags.WEATHER_TAG);
		Attr[] attributes = { xml.createAttribute(Keys.KEY_WEATHER_ICON_SRC), xml.createAttribute(Keys.KEY_WEATHER_TEMP),
				xml.createAttribute(Keys.KEY_WEATHER_DESC) };

		for (Attr a : attributes) {
			String value = values.get(a.getName());
			a.setValue(value);
			mWeather.setAttributeNode(a);
		}
	}

	public void addForecastBlock(HashMap<String, String> attributes) {
		mForecast = xml.createElement(Tags.FORECAST_TAG);
		Element[] attrElements = { xml.createElement(Keys.KEY_FORECAST_FEEL), xml.createElement(Keys.KEY_FORECAST_WIND),
				xml.createElement(Keys.KEY_FORECAST_PRESSURE), xml.createElement(Keys.KEY_FORECAST_HIMIDATY),
				xml.createElement(Keys.KEY_FORECAST_WATER_TEMP), xml.createElement(Keys.KEY_FORECAST_HEOM) };

		for (Element attr : attrElements) {
			String value = attributes.get(attr.getTagName());
			attr.appendChild(xml.createTextNode(value));
			mForecast.appendChild(attr);
		}
	}

	public void addForecastMoreBlock(HashMap<String, String> attributes) {
		mForecastMore = xml.createElement(Tags.FORECAST_MORE_TAG);
		Element[] attrElements = { xml.createElement(Keys.KEY_FORECAST_SUNRISE), xml.createElement(Keys.KEY_FORECAST_SUNSET),
				xml.createElement(Keys.KEY_FORECAST_DAY_DURATION), xml.createElement(Keys.KEY_FORECAST_MOON) };

		for (Element attr : attrElements) {
			String value = attributes.get(attr.getTagName());
			attr.appendChild(xml.createTextNode(value));
			mForecastMore.appendChild(attr);
		}
	}

}
