package com.growcontrol.plugins.example;

import com.poixson.commonapp.app.xApp;
import com.poixson.commonjava.EventListener.xEvent;
import com.poixson.commonjava.EventListener.xEvent.Priority;
import com.poixson.commonjava.xLogger.xLog;
import com.poixson.commonjava.xLogger.handlers.xCommandEvent;
import com.poixson.commonjava.xLogger.handlers.xCommandListener;


public class exampleCommands implements xCommandListener {



	@Override
	@xEvent(
			priority=Priority.NORMAL,
			threaded=false,
			filterHandled=true,
			filterCancelled=true)
	public void onCommand(xCommandEvent event) {
		switch(event.arg(0)) {
		case "example":
			event.setHandled();
			break;
		}
	}



	protected void _example(final xCommandEvent event) {
		if(event.isHelp()) {
			this._example_help(event);
			return;
		}
		this.log().title("EXAMPLE COMMAND");
	}
	protected void _example_help(final xCommandEvent event) {
		event.setHandled();
		this.publish();
		this.publish("This is the help info for the example plugin.");
		this.publish();
	}



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
