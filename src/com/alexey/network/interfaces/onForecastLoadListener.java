package com.alexey.network.interfaces;

import org.w3c.dom.Document;

public interface onForecastLoadListener {
	void onForecastLoadStart();
	void onForecastLoadFinish(Document xml);
}
