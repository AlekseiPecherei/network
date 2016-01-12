package com.alexey.network.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alexey.network.LoadUtils;
import com.alexey.network.interfaces.onSearchListener;

public class SearchManager {
	private onSearchListener mListener;

	public void setOnSearchListener(onSearchListener listener) {
		mListener = listener;
	}

	public void search(String what) {
		if (mListener != null) {
			mListener.onSearchStart();
			List<SearchResult> list = getSearchResults(what);
			mListener.onSearchFinish(list);
		}
	}

	private List<SearchResult> getSearchResults(String what) {
		List<SearchResult> list = new ArrayList<>();
		try {
			String url = LoadUtils.prepareSearchAddress(what);
			Document searchPage = LoadUtils.loadHtmlPage(url);

			Elements elements = getLiTagElements(searchPage);

			for (Element e : elements) {
				list.add(prepareResult(e));
			}
		} catch (IOException e) {
			System.out.println("something wrong" + e.getMessage());
		}
		return list;
	}

	private Elements getLiTagElements(Document searchPage) {
//		<html>
//		<body>
//		<ul>
//		  <li></li>
//		  <li></li>
//		  <li></li>
//		</ul>
//		</body>
//		</html>
//		
//		convert to
//		
//		<li></li>
//		<li></li>
//		<li></li>
		
		String filter1 = "a.catalog__link";
		String filter2 = "a[href*=/weather/]";

		return searchPage.select(filter1).select(filter2);
	}

	private SearchResult prepareResult(Element e) {
		String className = "catalog__txt";
		String hrefTag = "href";
		String aTag = "a";
		
		String link = e.attr(hrefTag);
		
		Elements temp = LoadUtils.getByClass(e, className);
		String discription = temp.text();
		temp.remove();
		String name = e.select(aTag).text();

		SearchResult result = new SearchResult();

		result.setLink(link);
		result.setDiscription(discription);
		result.setName(name);

		return result;
	}
}
