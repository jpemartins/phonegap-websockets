(function() {
	// window object
	var global = window;

	if (!!global.WebSocket || !window.WebSocketFactory) {
		return;
	}
	
	// WebSocket Object. All listener methods are cleaned up!
	var WebSocket = global.WebSocket = function(url, protocols) {
		// get a new websocket object from factory (check com.k2c.webphone.phonegap.html5.WebSocketFactory.java)
		this.socket = WebSocketFactory.getInstance(url);
		// store in registry
		if(this.socket) {
			var id = this.socket.getId();
			//console.log("websocket id="+id)
			WebSocket.store[id] = this;
		} else {
			throw new Error('Websocket instantiation failed! Address might be wrong.');
		}
	};
	
	// storage to hold websocket object for later invokation of event methods
	WebSocket.store = {};
	
	// static event methods to call event methods on target websocket objects
	WebSocket.onmessage = function (evt) {		
		WebSocket.store[evt._target]['onmessage'].call(global, evt);
	}	
	
	WebSocket.onopen = function (evt) {
		WebSocket.store[evt._target]['onopen'].call(global, evt);
	}
	
	WebSocket.onclose = function (evt) {
		WebSocket.store[evt._target]['onclose'].call(global, evt);
	}
	
	WebSocket.onerror = function (evt) {
		WebSocket.store[evt._target]['onerror'].call(global, evt);
	}

	// instance event methods
	WebSocket.prototype.send = function(data) {
		this.socket.send(data);
	}

	WebSocket.prototype.close = function() {
		this.socket.close();
	}
	
	WebSocket.prototype.CONNECTING = 0;
	
	WebSocket.prototype.OPEN = 1;
	
	WebSocket.prototype.CLOSING = 2;

	WebSocket.prototype.CLOSED = 3;

	WebSocket.prototype.__defineGetter__("readyState", function() {
		return this.socket.getReadyState();
	});

	WebSocket.prototype.__defineGetter__("url", function() {
		return this.socket.getUrl();
	});

	WebSocket.prototype.onopen = function(){
		//throw new Error('onopen not implemented.');
    };
    
    WebSocket.prototype.onmessage = function(msg){    	
    	//throw new Error('onmessage not implemented.');
    };
    
    WebSocket.prototype.onerror = function(msg){
    	//throw new Error('onerror not implemented.');
    };
    
    WebSocket.prototype.onclose = function(){
        //throw new Error('onclose not implemented.');
    };
})();