package com.growcontrol.plugins.serialcontrol;

import com.poixson.commonapp.app.xApp;
import com.poixson.commonjava.EventListener.xEvent;
import com.poixson.commonjava.EventListener.xEvent.Priority;
import com.poixson.commonjava.xLogger.xLog;
import com.poixson.commonjava.xLogger.handlers.xCommandEvent;
import com.poixson.commonjava.xLogger.handlers.xCommandListener;


public class Commands implements xCommandListener {



	@Override
	@xEvent(
			priority=Priority.NORMAL,
			threaded=false,
			filterHandled=true,
			filterCancelled=true)
	public void onCommand(xCommandEvent event) {
		if( !"serialcontrol".equalsIgnoreCase(event.arg(0)) )
			return;
		switch(event.arg(1)) {
//		case "set":
//			this._set(event);
		}
	}



//		// set output
//		if(command.equalsIgnoreCase("set")) {
//			if(numArgs < 3) return false;
//			if(!args[0].equalsIgnoreCase("ArduinoGC")) return false;
//			// get pin number
//			int pinNum = -1;
//			try {
//				pinNum = Integer.valueOf(args[1]);
//			} catch(Exception ignore) {}
//			if(pinNum < 0) return false;
//			// get pin state
//			int pinState = -1;
//			try {
//				pinState = Integer.valueOf(args[2]);
//			} catch(Exception ignore) {}
//			if(pinState < 0) return false;
//			// send to plugin
//			sendPinState(pinNum, pinState);
//			return true;
//		}
//		if(command.equalsIgnoreCase("get")) {
//			return true;
//		}
//		if(command.equalsIgnoreCase("watch")) {
//			return true;
//		}
//		return false;



//	protected void _set(final xCommandEvent event) {
//		if(event.isHelp()) {
//			this._set_help(event);
//			return;
//		}
//		event.setHandled();
//		// parse command arguments
//		for(final String arg : event.args)
//			this.log().severe("ARG: "+arg);
//		this.log().severe("ARG: "+event.arg(1));
//	}
//	protected void _set_help(final xCommandEvent event) {
//		event.setHandled();
//		this.publish();
//		this.publish("Sets the state of a pin.");
//		this.publish();
//	}



	// logger
	private volatile xLog _log = null;
	public xLog log() {
		if(this._log == null)
			this._log = xApp.log();
		return this._log;
	}
	public void publish(final String msg) {
		this.log().publish(msg);
	}
	public void publish() {
		this.log().publish();
	}



}
