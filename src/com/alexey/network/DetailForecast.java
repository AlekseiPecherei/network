package com.alexey.network;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.constants.Keys;
import com.alexey.network.encoder.DetailForecastEncoder;
import com.alexey.network.interfaces.onForecastLoadListener;

public class DetailForecast extends BaseForecast {
	private static final String FILTER_HTML_PAGE_BY_SELECTION = "option[selected]";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_TIME_SELECT = "time__select";

	private static final String URL_WEATHER_TYPE_DETAIL = "detailday";

	public DetailForecast(onForecastLoadListener forecast) {
		super(forecast);
	}

	@Override
	public org.w3c.dom.Document process(String placeId, int day) {
		try {
			DetailForecastEncoder saver = new DetailForecastEncoder();
			Document page = getForecastHtmlPage(placeId, day);

			String elem = page.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_TIME_SELECT)
					.select(FILTER_HTML_PAGE_BY_SELECTION).text();
			saver.createRootDayBlock(elem);

			Elements detail = page.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_DETAIL_TIME);

			for (Element e : detail) {
				String value = BaseForecast.filterTitle(e);
				String icon = BaseForecast.filterWeatherIcon(e);
				String temp = BaseForecast.filterWeatherTemp(e);
				String desc = BaseForecast.filterWeatherDesc(e);

				String pTag = "p";
				Elements p = e.getElementsByTag(pTag);
				HashMap<String, String> parametersMap = new HashMap<>();

				parametersMap.put(Keys.KEY_FORECAST_FEEL, p.get(0).text());
				parametersMap.put(Keys.KEY_FORECAST_WIND, p.get(1).text());
				parametersMap.put(Keys.KEY_FORECAST_PRESSURE, p.get(2).text());
				parametersMap.put(Keys.KEY_FORECAST_HIMIDATY, p.get(3).text());

				parametersMap.put(Keys.KEY_WEATHER_DESC, desc);
				parametersMap.put(Keys.KEY_WEATHER_ICON_SRC, icon);
				parametersMap.put(Keys.KEY_WEATHER_TEMP, temp);

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

	@Override
	protected String getURL(String placeId, int day) {
		return LoadUtils.URL_ADDRESS + placeId + URL_WEATHER_TYPE_DETAIL + "/" + day;
	}
}
