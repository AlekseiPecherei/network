package com.alexey.network;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.constants.Keys;
import com.alexey.network.encoder.CurrentEncoder;
import com.alexey.network.encoder.Encoder;
import com.alexey.network.interfaces.IForecast;

public class CurrentForecast extends BaseForecast {
	private static final String URL_WEATHER_TYPE_DETAIL = "current";
	
	public CurrentForecast(IForecast callback) {
		super(callback);
	}

	@Override
	public org.w3c.dom.Document process(String placeId, int day) {
		try {
			CurrentEncoder parser = new CurrentEncoder();
			
			Document page = getForecastHtmlPage(placeId, day);			
			Element root = page.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_DETAIL_TIME).get(0);

			String title = BaseForecast.filterTitle(root);		
			parser.createRootDayBlock(title);
			
			String icon = BaseForecast.filterWeatherIcon(root);
			String temp = BaseForecast.filterWeatherTemp(root);
			String desc = BaseForecast.filterWeatherDesc(root);
			
			String pTag = "p";
			Elements p = root.getElementsByTag(pTag);
			HashMap<String, String> parametersMap = new HashMap<>();
			
			parametersMap.put(Keys.KEY_FORECAST_FEEL, p.get(0).text());
			parametersMap.put(Keys.KEY_FORECAST_WIND, p.get(1).text());
			parametersMap.put(Keys.KEY_FORECAST_PRESSURE, p.get(2).text());
			parametersMap.put(Keys.KEY_FORECAST_HIMIDATY, p.get(3).text());
			parametersMap.put(Keys.KEY_FORECAST_WATER_TEMP, p.get(4).text());
			parametersMap.put(Keys.KEY_FORECAST_HEOM, p.get(5).text());
			parametersMap.put(Keys.KEY_FORECAST_SUNRISE, p.get(6).text());
			parametersMap.put(Keys.KEY_FORECAST_SUNSET, p.get(7).text());
			parametersMap.put(Keys.KEY_FORECAST_DAY_DURATION, p.get(8).text());
			parametersMap.put(Keys.KEY_FORECAST_MOON, p.get(9).text());
			
			parametersMap.put(Keys.KEY_WEATHER_DESC, desc);
			parametersMap.put(Keys.KEY_WEATHER_ICON_SRC, icon);
			parametersMap.put(Keys.KEY_WEATHER_TEMP, temp);
			
			parser.addWeatherBlock(parametersMap);
			parser.addForecastBlock(parametersMap);
			parser.addForecastMoreBlock(parametersMap);

			return parser.prepareDocument();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	protected String getURL(String placeId, int day) {
		return LoadUtils.URL_ADDRESS + placeId + URL_WEATHER_TYPE_DETAIL;
	}

}
