package com.alexey.network;

import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.constants.Keys;
import com.alexey.network.encoder.DetailForecastEncoder;

public class DetailForecast extends BaseForecast {
	@Override
	public org.w3c.dom.Document process(String placeId, int day) {
		try {
			DetailForecastEncoder encoder = new DetailForecastEncoder();
			Document page = getForecastHtmlPage(placeId, day);

			String elem = LoadUtils.getByClass(page, "time__select").select("option[selected]").text();
			encoder.createRootDayBlock(elem);

			Elements detail = LoadUtils.getByClass(page, "detail__time");

			for (Element e : detail) {
				String value = filterTitle(e);

				Elements p = LoadUtils.getPTagElements(e);
				HashMap<String, String> parameters = new HashMap<>();

				fillMap(p, parameters);
				fillMap(e, parameters);

				encoder.addWeatherBlock(parameters);
				encoder.addForecastBlock(parameters);
				encoder.addDetailTimeBlock(value);
			}

			return encoder.prepareDocument();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	protected void fillMap(Elements p, HashMap<String, String> map) {
		map.put(Keys.KEY_FORECAST_FEEL, p.get(0).text());
		map.put(Keys.KEY_FORECAST_WIND, p.get(1).text());
		map.put(Keys.KEY_FORECAST_PRESSURE, p.get(2).text());
		map.put(Keys.KEY_FORECAST_HIMIDATY, p.get(3).text());
	}

	@Override
	protected String prepareURL(String placeId, int day) {
		return LoadUtils.URL_ADDRESS + placeId + "detailday/" + day;
	}
}
