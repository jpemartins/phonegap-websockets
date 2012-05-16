package com.phonegap.html5;

import java.util.Random;

import android.util.Log;
import android.webkit.WebView;

public class WebSocketFactory {

	/** The app view. */
	WebView appView;

	/**
	 * Instantiates a new web socket factory.
	 * 
	 * @param appView
	 *            the app view
	 */
	public WebSocketFactory(WebView appView) {
		this.appView = appView;
	}

	public WebSocket getInstance(String url) {
		WebSocket socket = new WebSocket(appView, url, getRandonUniqueId());		
		try {
			socket.connect();
		} catch (Exception e) {
			Log.e("WebSocketFactory", "Cannot connect: "+e.getMessage());
			return null;
		} 
		return socket;
	}

	/**
	 * Generates random unique ids for WebSocket instances
	 * 
	 * @return String
	 */
	private String getRandonUniqueId() {
		Integer uuid = new Random().nextInt(100);
		Log.d("WebSocketFactory", "WebSocket uuid="+uuid);
		return uuid.toString();
	}

}