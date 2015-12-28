package com.alexey.network.search;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.interfaces.ISearch;

public class SearchManager {
	private static final String FILTER_HTML_PAGE_BY_A_HREF_TAG 					= "a[href*=/weather/]";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_CATALOG_TXT 		= "catalog__txt";
	private static final String FILTER_HTML_PAGE_BY_CLASS_NAME_A_CATALOG_LINK 	= "a.catalog__link";

	private static final String SEARCH_URL_ADDRESS 	= "http://m.gismeteo.ru/citysearch/by_name/?gis_search=";
	
	private static final String ENCODE_UTF_8 	= StandardCharsets.UTF_8.name();
	
	private ISearch mCallback;

	public SearchManager(ISearch search) {
		mCallback = search;
	}

	public void search(String what) {
		mCallback.onSearchStart();

		Document searchPage = null;
		List<SearchResult> list = new ArrayList<>();
		try {
			String url = SEARCH_URL_ADDRESS + URLEncoder.encode(what, ENCODE_UTF_8);
			searchPage = Jsoup.connect(url).get();

			Elements onlyLiElements = searchPage.select(FILTER_HTML_PAGE_BY_CLASS_NAME_A_CATALOG_LINK)
					.select(FILTER_HTML_PAGE_BY_A_HREF_TAG);

			for (Element e : onlyLiElements) {
				list.add(prepareResult(e));
			}
		} catch (IOException e) {
			System.out.println("something wrong" + e.getMessage());
		} finally {
			mCallback.onSearchFinish(list);
		}
	}

	private SearchResult prepareResult(Element e) {
		String hrefTag = "href";
		String aTag	= "a";
		String link = e.attr(hrefTag);
		String discription = e.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_CATALOG_TXT).text();
		e.getElementsByClass(FILTER_HTML_PAGE_BY_CLASS_NAME_CATALOG_TXT).remove();
		String name = e.select(aTag).text();

		SearchResult result = new SearchResult();

		result.setLink(link);
		result.setDiscription(discription);
		result.setName(name);

		return result;
	}
}
