package com.growcontrol.plugins.serialcontrol.connections;

import java.io.Closeable;
import java.io.IOException;

import com.poixson.commonjava.xLogger.xLog;


public interface Connection extends Closeable {


	@Override
	public void close();

	public void write(final String data) throws IOException;

	public xLog log();


}
