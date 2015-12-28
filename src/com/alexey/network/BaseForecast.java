package com.alexey.network;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alexey.network.interfaces.IForecast;

public abstract class BaseForecast {
	protected static final String FILTER_HTML_PAGE_BY_CLASS_NAME_DETAIL_TIME = "detail__time";
	protected static final String URL_ADDRESS_M_GISMETEO_RU = "http://m.gismeteo.ru";

	protected IForecast callback;

	public BaseForecast(IForecast callback) {
		this.callback = callback;
	}

	public void update(String place) {
		update(place, 0);
	}

	/**
	 * 
	 * @param place
	 * @param day
	 *            - day is value from 1 to 13.
	 */
	public void update(String place, int day) {
		callback.onForecastLoadStart();
		org.w3c.dom.Document xml = process(place, day);
		callback.onForecastLoadFinish(xml);
	}

	protected Document getForecastHtmlPage(String placeId, int day) throws IOException {
		String url = getURL(placeId, day);
		return Jsoup.connect(url).get();
	}

	protected abstract org.w3c.dom.Document process(String placeId, int day);
	protected abstract String getURL(String placeId, int day);

	protected static String filterWeatherIcon(Element element) {
		String imgTag = "img";
		String srcTag = "src";
		String className = "weather__icon";
		return element.getElementsByClass(className).select(imgTag).attr(srcTag);
	}
	
	protected static String filterWeatherDesc(Element element) {
		String className = "weather__desc";
		return element.getElementsByClass(className).text();
	}
	
	protected static String filterWeatherTemp(Element element) {
		String className = "weather__temp";
		return element.getElementsByClass(className).text();
	}
	
	protected static String filterTitle(Element element) {
		String className = "title";
		return element.getElementsByClass(className).text();
	}
}