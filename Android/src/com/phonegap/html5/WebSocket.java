package com.phonegap.html5;

import android.webkit.WebView;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class WebSocket {		
	private static String BLANK_MESSAGE = "";
	private static String EVENT_ON_OPEN = "onopen";
	private static String EVENT_ON_MESSAGE = "onmessage";
	private static String EVENT_ON_CLOSE = "onclose";
	private static String EVENT_ON_ERROR = "onerror";
	
	private enum ReadyState {
		CONNECTING(0), OPEN(1), CLOSING(2), CLOSED(3);
		
		private final int id;
		
		private ReadyState(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}			
	}
	
	private ReadyState readyState;
	private WebSocketConnection connection;
	
	public final WebView appView;
	public final String url, uuid;
	
	
	public WebSocket(WebView appView, String url, String uuid) {
		super();
		this.appView = appView;
		this.url = url;
		this.uuid = uuid;
		this.readyState = ReadyState.CONNECTING;
		this.connection = new WebSocketConnection();
	}
		
	public void send(final String text) {
		connection.sendTextMessage(text);
	}

/*	
	public void send(final byte[] data) {
		connection.sendBinaryMessage(data);
	}
*/
	public void close() {
		this.readyState = ReadyState.CLOSING;		
		connection.disconnect();
	}
	
	public int getReadyState() {		
		return readyState.getId();
	}
	
	public String getId() {
		return uuid;
	}

	public String getUrl() {
		return url;
	}

	public void connect() throws WebSocketException {		
		this.readyState = ReadyState.CONNECTING;

		connection.connect(url, new WebSocketHandler() {
			/**
			 * Called when an entire text frame has been received.
			 * 
			 * @param msg
			 *            Message from websocket server
			 */
			@Override
			public void onTextMessage(final String msg) {
				appView.post(new Runnable() {
					@Override
					public void run() {
						appView.loadUrl(buildJavaScriptData(EVENT_ON_MESSAGE, msg));
					}
				});
			}
			
			@Override
			public void onOpen() {
				readyState = ReadyState.OPEN;
				
				appView.post(new Runnable() {
					@Override
					public void run() {
						appView.loadUrl(buildJavaScriptData(EVENT_ON_OPEN, BLANK_MESSAGE));
					}
				});		
			}
			
			@Override
			public void onClose(int code, String reason) {
				readyState = ReadyState.CLOSING;
				
				appView.post(new Runnable() {
					@Override
					public void run() {
						appView.loadUrl(buildJavaScriptData(EVENT_ON_CLOSE, BLANK_MESSAGE));
					}
				});
			}
		});
	}

	
	public void onError(final Throwable t) {
		appView.post(new Runnable() {
			@Override
			public void run() {
				appView.loadUrl(buildJavaScriptData(EVENT_ON_ERROR, t.getMessage()));
			}
		});
	}
	
	private String buildJavaScriptData(String event, String msg) {
		String _d = "javascript:WebSocket." + event + "(" + "{" + "\"_target\":\"" + uuid + "\"," + "\"data\":'" + msg
				+ "'" + "}" + ")";
		return _d;
	}
}
