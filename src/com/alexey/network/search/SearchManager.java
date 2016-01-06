package com.alexey.network.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.LoadUtils;
import com.alexey.network.interfaces.ISearch;

public class SearchManager {
	private ISearch mCallback;

	public SearchManager(ISearch search) {
		mCallback = search;
	}

	public void search(String what) {
		mCallback.onSearchStart();
		List<SearchResult> list = getSearchResults(what);
		mCallback.onSearchFinish(list);
	}

	private List<SearchResult> getSearchResults(String what) {
		List<SearchResult> list = new ArrayList<>();
		try {
			String url = LoadUtils.getSearchAddress(what);
			Document searchPage = LoadUtils.loadPage(url);

			Elements onlyLiElements = filterPage(searchPage);

			for (Element e : onlyLiElements) {
				list.add(prepareResult(e));
			}
		} catch (IOException e) {
			System.out.println("something wrong" + e.getMessage());
		}
		return list;
	}

	private Elements filterPage(Document searchPage) {
		String filter1 = "a.catalog__link";
		String filter2 = "a[href*=/weather/]";

		return searchPage.select(filter1).select(filter2);
	}

	private SearchResult prepareResult(Element e) {
		String className = "catalog__txt";
		String hrefTag = "href";
		String aTag = "a";
		String link = e.attr(hrefTag);
		String discription = e.getElementsByClass(className).text();
		e.getElementsByClass(className).remove();
		String name = e.select(aTag).text();

		SearchResult result = new SearchResult();

		result.setLink(link);
		result.setDiscription(discription);
		result.setName(name);

		return result;
	}
}
