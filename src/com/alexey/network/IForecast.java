package com.alexey.network;

import org.w3c.dom.Document;

public interface IForecast {
	void onForecastLoadStart();
	void onForecastLoadFinish(Document xml);
}
