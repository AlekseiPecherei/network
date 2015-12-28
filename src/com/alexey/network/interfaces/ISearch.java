package com.alexey.network.interfaces;

import java.util.List;

import com.alexey.network.search.SearchResult;

public interface ISearch {
	void onSearchStart();
	void onSearchFinish(List<SearchResult> result);
}
