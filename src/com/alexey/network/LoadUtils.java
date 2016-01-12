package com.alexey.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public final class LoadUtils {
	private static final String ENCODE_UTF_8 = StandardCharsets.UTF_8.name();
	
	public final static String URL_ADDRESS = "http://m.gismeteo.ru";
	public final static String SEARCH_URL_ADDRESS = "http://m.gismeteo.ru/citysearch/by_name/?gis_search=";
	
	public static Document loadHtmlPage(String url) throws IOException {
		return Jsoup.connect(url).get();
	}
	
	public static String prepareSearchAddress(String what) throws UnsupportedEncodingException {
		return SEARCH_URL_ADDRESS + URLEncoder.encode(what, ENCODE_UTF_8);
	}
}
