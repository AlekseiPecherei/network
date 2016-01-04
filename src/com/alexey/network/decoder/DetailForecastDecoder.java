package com.alexey.network.decoder;

import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alexey.network.constants.Keys;
import com.alexey.network.constants.Tags;

public class DetailForecastDecoder {
	private DetailForecastDecoder() {
		
	}
	
	public enum DetailTimes {
		NIGHT, MORNING, DAY, EVENING
	};

	public static String getDetailDay(Document document) {
		return document.getElementsByTagName(Tags.ROOT_ELEMENT).item(0).getAttributes()
				.getNamedItem(Keys.KEY_DETAIL_DAY_ATTR).getNodeValue();
	}

	public static DetailTime getDetailTime(Document document, DetailTimes detail) {
 		NodeList list = document.getElementsByTagName(Tags.DETAIL_TIME_TAG);
		Node node = null;
		switch (detail) {
		case NIGHT:
			node = list.item(0);			
			break;
		case MORNING:
			node = list.item(1);			
			break;
		case DAY:
			node = list.item(2);			
			break;
		case EVENING:
			node = list.item(3);			
			break;

		default:
			break;
		}
		
		Map<String, String> values = new LinkedHashMap<>();
		String value = node.getAttributes().getNamedItem(Keys.KEY_DETAIL_TIME_ATTR).getNodeValue();
		Element element = (Element) node.getFirstChild();
		String desc = element.getAttribute(Keys.KEY_WEATHER_DESC);
		String icon = element.getAttribute(Keys.KEY_WEATHER_ICON_SRC);
		String temp = element.getAttribute(Keys.KEY_WEATHER_TEMP);
		
		values.put(Keys.KEY_DETAIL_TIME_ATTR, value);
		values.put(Keys.KEY_WEATHER_DESC, desc);
		values.put(Keys.KEY_WEATHER_ICON_SRC, icon);
		values.put(Keys.KEY_WEATHER_TEMP, temp);
		
		NodeList forecast = node.getChildNodes().item(1).getChildNodes();
		String feel = forecast.item(0).getFirstChild().getNodeValue();
		String wind = forecast.item(1).getFirstChild().getNodeValue();
		String pressure = forecast.item(2).getFirstChild().getNodeValue();
		String himidaty = forecast.item(3).getFirstChild().getNodeValue();
		
		values.put(Keys.KEY_FORECAST_FEEL, feel);
		values.put(Keys.KEY_FORECAST_WIND, wind);
		values.put(Keys.KEY_FORECAST_PRESSURE, pressure);
		values.put(Keys.KEY_FORECAST_HIMIDATY, himidaty);
		
		return new DetailForecastDecoder().new DetailTime(values);
	}

	public class DetailTime {
		private Map<String, String> mParameters;
		public DetailTime(Map<String, String> value) {
			mParameters = value;
		}
		
		public String getValue(String key) {
			return mParameters.get(key);
		}
	}
}
