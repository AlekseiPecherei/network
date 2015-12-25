package com.alexey.main;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.alexey.network.DetailForecast;
import com.alexey.network.DetailForecastXMLParser;
import com.alexey.network.IForecast;

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

		DetailForecast forecast = new DetailForecast(new IForecast() {

			@Override
			public void onForecastLoadStart() {
				System.out.println("update");
			}

			@Override
			public void onForecastLoadFinish(Document xml) {
				DetailForecastXMLParser.show(xml);
			}
		});

		forecast.update("/weather/11950/", 0);

	}

}
