package com.alexey.network;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DetailForecast {
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_WEATHER_DESC = "weather__desc";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_WEATHER_TEMP = "weather__temp";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_WEATHER_ICON = "weather__icon";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_TITLE = "title";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_DETAIL_TIME = "detail__time";
	private static final String FILTER_HTML_PAGE_BY_SELECTION = "option[selected]";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_TIME_SELECT = "time__select";

	private static final String URL_ADDRESS_M_GISMETEO_RU = "http://m.gismeteo.ru";
	private static final String URL_WEATHER_TYPE_DETAIL = "detailday";

	private IForecast callback;

	public DetailForecast(IForecast forecast) {
		callback = forecast;
	}

	public void update(String place) {
		callback.onForecastLoadStart();
		org.w3c.dom.Document xml = process(place, 0);
		callback.onForecastLoadFinish(xml);
	}
	
	/**
	 * 
	 * @param place 
	 * @param day - day is value from 1 to 13.
	 */
	public void update(String place, int day) {
		callback.onForecastLoadStart();
		org.w3c.dom.Document xml = process(place, day);
		callback.onForecastLoadFinish(xml);
	}

	private org.w3c.dom.Document process(String placeId, int day) {
		Document page = null;
		DetailForecastXMLParser saver = null;
		try {
			saver = new DetailForecastXMLParser();
			String url = URL_ADDRESS_M_GISMETEO_RU + placeId + URL_WEATHER_TYPE_DETAIL + "/" + day;
			page = Jsoup.connect(url).get();

			String elem = page.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_TIME_SELECT)
					.select(FILTER_HTML_PAGE_BY_SELECTION).text();
			saver.createRootDayBlock(elem);

			Elements detail = page.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_DETAIL_TIME);

			for (Element e : detail) {
				String value = e.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_TITLE).text();

				String imgTag = "img";
				String srcTag = "src";
				String pTag = "p";
				
				String icon = e.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_WEATHER_ICON).select(imgTag).attr(srcTag);
				String temp = e.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_WEATHER_TEMP).text();
				String desc = e.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_WEATHER_DESC).text();

				Elements p = e.getElementsByTag(pTag);
				HashMap<String, String> parametersMap = new HashMap<>();

				parametersMap.put(DetailForecastXMLParser.KEY_FORECAST_FEEL, p.get(0).text());
				parametersMap.put(DetailForecastXMLParser.KEY_FORECAST_WIND, p.get(1).text());
				parametersMap.put(DetailForecastXMLParser.KEY_FORECAST_PRESSURE, p.get(2).text());
				parametersMap.put(DetailForecastXMLParser.KEY_FORECAST_HIMIDATY, p.get(3).text());
				parametersMap.put(DetailForecastXMLParser.KEY_FORECAST_GEOM, p.get(4).text());

				parametersMap.put(DetailForecastXMLParser.KEY_WEATHER_DESC, desc);
				parametersMap.put(DetailForecastXMLParser.KEY_WEATHER_ICON_SRC, icon);
				parametersMap.put(DetailForecastXMLParser.KEY_WEATHER_TEMP, temp);

				saver.addWeatherBlock(parametersMap);
				saver.addForecastBlock(parametersMap);
				saver.addDetailTimeBlock(value);
			}

			return saver.prepareDocument();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
