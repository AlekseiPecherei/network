package com.alexey.network;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.constants.Keys;
import com.alexey.network.encoder.CurrentEncoder;

public class CurrentForecast extends BaseForecast {
	@Override
	public org.w3c.dom.Document process(String placeId, int day) {
		try {
			CurrentEncoder encoder = new CurrentEncoder();
			
			Document page = getForecastHtmlPage(placeId, day);			
			Element root = Filter.getByClass(page, "detail__time").get(0);

			String title = filterTitle(root);		
			encoder.createRootDayBlock(title);

			Elements p = Filter.getPTagElements(root);
			HashMap<String, String> parameters = new HashMap<>();
			
			fillMap(p, parameters);			
			fillMap(root, parameters);
			
			encoder.addWeatherBlock(parameters);
			encoder.addForecastBlock(parameters);
			encoder.addForecastMoreBlock(parameters);

			return encoder.prepareDocument();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	protected void fillMap(Elements es, HashMap<String, String> map) {
		map.put(Keys.KEY_FORECAST_FEEL, es.get(0).text());
		map.put(Keys.KEY_FORECAST_WIND, es.get(1).text());
		map.put(Keys.KEY_FORECAST_PRESSURE, es.get(2).text());
		map.put(Keys.KEY_FORECAST_HIMIDATY, es.get(3).text());
		map.put(Keys.KEY_FORECAST_WATER_TEMP, es.get(4).text());
		map.put(Keys.KEY_FORECAST_HEOM, es.get(5).text());
		map.put(Keys.KEY_FORECAST_SUNRISE, es.get(6).text());
		map.put(Keys.KEY_FORECAST_SUNSET, es.get(7).text());
		map.put(Keys.KEY_FORECAST_DAY_DURATION, es.get(8).text());
		map.put(Keys.KEY_FORECAST_MOON, es.get(9).text());
	}

	@Override
	protected String prepareURL(String placeId, int day) {
		return LoadUtils.URL_ADDRESS + placeId + "current";
	}

}
