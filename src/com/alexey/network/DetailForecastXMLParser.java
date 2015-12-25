package com.alexey.network;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DetailForecastXMLParser {
	private static final String ROOT_ELEMENT = "detailDay";
	private static final String DETAIL_TIME_TAG = "detailTime";
	private static final String WEATHER_TAG = "weather";
	private static final String FORECAST_TAG = "forecast";
	
	public static final String KEY_FORECAST_FEEL = "feel";
	public static final String KEY_FORECAST_GEOM = "geom";
	public static final String KEY_FORECAST_HIMIDATY = "himidaty";
	public static final String KEY_FORECAST_PRESSURE = "pressure";
	public static final String KEY_FORECAST_WIND = "wind";
	
	public static final String KEY_WEATHER_DESC = "desc";
	public static final String KEY_WEATHER_TEMP = "temp";
	public static final String KEY_WEATHER_ICON_SRC = "icon_src";
	
	public static final String KEY_DETAIL_TIME_ATTR = "value";
	public static final String KEY_DETAIL_DAY_ATTR = "day";
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document xml;
	
	private Element mRootElement;		//<DetailDay> root block
	private List<Element> mDetailTimes;	//<DetailTime> blocks
	private List<Element> mForecasts;	//<Forecast> blocks
	private List<Element> mWeathers;	//<Weather> blocks
	
	public DetailForecastXMLParser() throws ParserConfigurationException {
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		xml = builder.newDocument();		

		mDetailTimes = new ArrayList<>();
		mForecasts = new ArrayList<>();
		mWeathers = new ArrayList<>();
	}
	
	public void createRootDayBlock(String name) {
		mRootElement = xml.createElement(ROOT_ELEMENT);
		
		Attr day = xml.createAttribute(KEY_DETAIL_DAY_ATTR);
		day.setValue(name);
		mRootElement.setAttributeNode(day);		
		
		xml.appendChild(mRootElement);
	}
	
	public void addDetailTimeBlock(String text) {
		Element detail = xml.createElement(DETAIL_TIME_TAG);
		
		Attr value = xml.createAttribute(KEY_DETAIL_TIME_ATTR);
		value.setValue(text);
		detail.setAttributeNode(value);
		
		mDetailTimes.add(detail);
	}
	
	public void addForecastBlock(HashMap<String, String> attributes) {
		Element currentForecast = xml.createElement(FORECAST_TAG);	
		Element[] attrElements = {
			xml.createElement(KEY_FORECAST_FEEL),
			xml.createElement(KEY_FORECAST_WIND),
			xml.createElement(KEY_FORECAST_PRESSURE),
			xml.createElement(KEY_FORECAST_HIMIDATY),
			xml.createElement(KEY_FORECAST_GEOM)
		};
				
		for(Element attr : attrElements) { 
			String value = attributes.get(attr.getTagName());
			attr.appendChild(xml.createTextNode(value));
			
			currentForecast.appendChild(attr);
		}
		
		mForecasts.add(currentForecast);
	}
	
	public void addWeatherBlock(HashMap<String, String> values) {
		Element currentWeather = xml.createElement(WEATHER_TAG);
		Attr[] attributes = {
			xml.createAttribute(KEY_WEATHER_ICON_SRC),
			xml.createAttribute(KEY_WEATHER_TEMP),
			xml.createAttribute(KEY_WEATHER_DESC)
		};
		
		for(Attr a : attributes) {	
			String value = values.get(a.getName());
			a.setValue(value);
			currentWeather.setAttributeNode(a);
		}
		
		mWeathers.add(currentWeather);
	}

	public Document prepareDocument() {
		for(Element e : mDetailTimes) {
			int index = mDetailTimes.indexOf(e);
			e.appendChild(mWeathers.get(index));
			e.appendChild(mForecasts.get(index));
			
			mRootElement.appendChild(e);
		}
		return xml;
	}
	
	/**
	 * 
	 * @param doc - org.w3c.dom.Document object.
	 * This method print <b>doc</b> param to System.out stream
	 */
	public static void show(Document doc) {
	    try {
	        StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        transformer.transform(new DOMSource(doc), new StreamResult(sw));
	        System.out.println(sw.toString());
	        sw.flush();
	    } catch (Exception ex) {
	        throw new RuntimeException("Error converting to String", ex);
	    }
	}
}