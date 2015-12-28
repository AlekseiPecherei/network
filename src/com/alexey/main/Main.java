package com.alexey.main;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.alexey.network.BaseForecast;
import com.alexey.network.CurrentForecast;
import com.alexey.network.DetailForecast;
import com.alexey.network.interfaces.IForecast;
import com.alexey.network.parser.DetailForecastParser;
import com.alexey.network.parser.Parcer;

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

		BaseForecast forecast = new CurrentForecast(new IForecast() {

			@Override
			public void onForecastLoadStart() {
				System.out.println("update");
			}

			@Override
			public void onForecastLoadFinish(Document xml) {
				Parcer.show(xml);
			}
		});
		
		forecast.update("/weather/11950/");

	}

}
