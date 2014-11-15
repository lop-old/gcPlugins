package com.growcontrol.plugins.arduinogc;

import com.poixson.commonapp.app.xApp;
import com.poixson.commonjava.EventListener.xEvent;
import com.poixson.commonjava.EventListener.xEvent.Priority;
import com.poixson.commonjava.xLogger.xLog;
import com.poixson.commonjava.xLogger.handlers.xCommandEvent;
import com.poixson.commonjava.xLogger.handlers.xCommandListener;


public class arduinoCommands implements xCommandListener {



	@Override
	@xEvent(
			priority=Priority.NORMAL,
			threaded=false,
			filterHandled=true,
			filterCancelled=true)
	public void onCommand(xCommandEvent event) {
//ArduinoGC.log.severe("ARDUINO Command: "+command.toString());
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
