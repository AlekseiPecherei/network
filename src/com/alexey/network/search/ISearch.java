package com.alexey.network.search;

import java.util.List;

public interface ISearch {
	void onSearchStart();
	void onSearchFinish(List<SearchResult> result);
}
