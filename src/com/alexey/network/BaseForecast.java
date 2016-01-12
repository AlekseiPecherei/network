package com.alexey.network;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.constants.Keys;
import com.alexey.network.interfaces.onForecastLoadListener;

public abstract class BaseForecast {
	protected onForecastLoadListener mListener;
	
	protected abstract org.w3c.dom.Document process(String placeId, int day);
	protected abstract String prepareURL(String placeId, int day);	
	protected abstract void fillMap(Elements p, HashMap<String, String> map);

	public void setOnForecastLoadListener(onForecastLoadListener listener) {
		mListener = listener;
	}

	/**
	 * 
	 * @param place
	 * @param day
	 *            - day is value from 0 to 13.
	 */
	public void update(String place, int day) {
		if (mListener != null) {
			mListener.onForecastLoadStart();
			org.w3c.dom.Document xml = process(place, day);
			mListener.onForecastLoadFinish(xml);
		}
	}

	protected Document getForecastHtmlPage(String placeId, int day) throws IOException {
		String url = prepareURL(placeId, day);
		return LoadUtils.loadHtmlPage(url);
	}

	protected void fillMap(Element e, HashMap<String, String> map) {
		String icon = filterWeatherIcon(e);
		String temp = filterWeatherTemp(e);
		String desc = filterWeatherDesc(e);
		map.put(Keys.KEY_WEATHER_DESC, desc);
		map.put(Keys.KEY_WEATHER_ICON_SRC, icon);
		map.put(Keys.KEY_WEATHER_TEMP, temp);
	}
	
	private String filterWeatherIcon(Element element) {
		String imgTag = "img";
		String srcTag = "src";
		String name = "weather__icon";
		return Filter.getByClass(element, name).select(imgTag).attr(srcTag);
	}

	private String filterWeatherDesc(Element element) {
		String name = "weather__desc";
		return Filter.getTextByTagName(element, name);
	}

	private String filterWeatherTemp(Element element) {
		String name = "weather__temp";
		return Filter.getTextByTagName(element, name);
	}

	protected String filterTitle(Element element) {
		String name = "title";
		return Filter.getTextByTagName(element, name);
	}
	
	public static class Filter {
		public static Elements getByClass(Document document, String name) {
			return document.getElementsByClass(name);
		}
		
		public static Elements getByClass(Element e, String name) {
			return e.getElementsByClass(name);
		}
		
		public static String getTextByTagName(Element e, String name) {
			return getByClass(e, name).text();
		}
		
		public static Elements getPTagElements(Element e) {
			String tag = "p";
			return e.getElementsByTag(tag);
		}
	}
}