package com.alexey.main;

import org.w3c.dom.Document;

import com.alexey.network.BaseForecast;
import com.alexey.network.DetailForecast;
import com.alexey.network.constants.Keys;
import com.alexey.network.decoder.DetailForecastDecoder;
import com.alexey.network.decoder.DetailForecastDecoder.DetailTimes;
import com.alexey.network.encoder.Encoder;
import com.alexey.network.interfaces.IForecast;

public class Main {

	public static void main(String[] args) {
		// List<SearchResult> list = new ArrayList<>();
		// SearchManager manager = new SearchManager(new ISearch() {
		//
		// @Override
		// public void onSearchStart() {
		// System.out.println("поиск началс€");
		// }
		//
		// @Override
		// public void onSearchFinish(List<SearchResult> result) {
		// System.out.println("найдено: " + result.size());
		// }
		// });
		//
		// manager.search("ƒзержинск");

		BaseForecast forecast = new DetailForecast(new IForecast() {

			@Override
			public void onForecastLoadStart() {
				System.out.println("start...");
			}

			@Override
			public void onForecastLoadFinish(Document xml) {
				Encoder.show(xml);
				System.out.println(DetailForecastDecoder.getDetailDay(xml));
				DetailForecastDecoder.DetailTime time = DetailForecastDecoder.getDetailTime(xml, DetailTimes.DAY);
				System.out.println(time.getValue(Keys.KEY_FORECAST_WIND));
			}
		});		
		
		forecast.update("/weather/11950/", 1);

	}

}
