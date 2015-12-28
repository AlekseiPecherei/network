package com.alexey.network.parser;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public abstract class Parcer {

	public final static String KEY_FORECAST_FEEL = "feel";
	public final static String KEY_FORECAST_HIMIDATY = "himidaty";
	public final static String KEY_FORECAST_PRESSURE = "pressure";
	public final static String KEY_FORECAST_WIND = "wind";
	public final static String KEY_FORECAST_WATER_TEMP = "water";
	public final static String KEY_FORECAST_HEOM = "heom";
	public final static String KEY_FORECAST_SUNRISE = "sunrise";
	public final static String KEY_FORECAST_SUNSET = "sunset";
	public final static String KEY_FORECAST_DAY_DURATION = "duration";
	public final static String KEY_FORECAST_MOON = "moon";
	
	public final static String KEY_WEATHER_DESC = "desc";
	public final static String KEY_WEATHER_TEMP = "temp";
	public final static String KEY_WEATHER_ICON_SRC = "icon_src";
	
	public final static String KEY_DETAIL_TIME_ATTR = "value";
	public final static String KEY_DETAIL_DAY_ATTR = "day";
	
	protected Document xml;
	
	public Parcer() throws ParserConfigurationException {
		xml = createDocument();
	}
	
	private Document createDocument() throws ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}
	
	public abstract Document prepareDocument();

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