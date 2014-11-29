package com.growcontrol.plugins.serialcontrol.connections;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

import com.growcontrol.plugins.serialcontrol.SerialControl;
import com.growcontrol.plugins.serialcontrol.devices.ArduinoDevice;
import com.poixson.commonjava.Utils.CoolDown;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.utilsNumbers;
import com.poixson.commonjava.Utils.utilsString;
import com.poixson.commonjava.xLogger.xLog;


// 0403:6001 - Future Technology Devices International, Ltd FT232 USB-Serial (UART) IC
// 067b:2303 - Prolific Technology, Inc. PL2303 Serial Port
public class ConnectionSerial implements Connection, SerialPortEventListener {

	public static final int timeout = 500;

	protected final SerialControl plugin;
	protected final SerialPort comm;

	protected final InputStream  in;
	protected final OutputStream out;
	protected volatile String readBuffer = "";

	protected volatile CoolDown scanning = null;
	protected final Map<Integer, ArduinoDevice> devices = new HashMap<Integer, ArduinoDevice>();



	public ConnectionSerial(final SerialControl plugin,
			final CommPortIdentifier portIdent, final int baud)
			throws IOException, PortInUseException {
		this.plugin = plugin;
		final String portName = portIdent.getName();
		this._log = this.plugin.log().getWeak(utilsString.getLastPart("/", portName));
		this.log().finest("Attempting connection..");
		if(!utils.validBaud(baud)) throw new RuntimeException("Invalid baud rate: "+Integer.toString(baud));
		// open comm port
		final SerialPort port;
		try {
			final String owner = "GrowControl-"+this.plugin.getPluginName();
			if(portIdent.isCurrentlyOwned()) throw new PortInUseException();
			port = (SerialPort) portIdent.open(owner, timeout);
			port.setSerialPortParams(
				baud,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE
			);
		} catch (UnsupportedCommOperationException e) {
			throw new IOException("Failed to open comm port: "+portName, e);
		}
		this.comm = port;
		// io streams
		this.in  = this.comm.getInputStream();
		this.out = this.comm.getOutputStream();
		try {
			this.comm.addEventListener(this);
			this.comm.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			throw new IOException("Failed to register comm event listener", e);
		}
		this.log().finest("Opened comm port");
		// request device id's
		this.log().fine("Sending 'scan' packet..");
		this.scanning = CoolDown.get("2s");
		this.write(";\r\nscan;\r\n");
	}
	public void dispose() {
		this.close();
	}



	@Override
	public void close() {
		this.comm.removeEventListener();
		utils.safeClose(this.out);
		utils.safeClose(this.in);
		try {
			this.comm.close();
		} catch (Exception ignore) {}
	}



	private void processBuffer() {
		// parse lines from buffer
		synchronized(this.readBuffer) {
			while(this.readBuffer.contains("\n")) {
				// find line ending
				final int pos = this.readBuffer.indexOf("\n");
				if(pos == -1) return;
				// get line
				final String part = this.readBuffer.substring(0, pos);
				// remove from buffer
				this.readBuffer = this.readBuffer.substring(pos + 1);
				// process line
				if(utils.notEmpty(part))
					this.processLine(part);
			}
		}
	}
	private void processLine(final String line) {
		if(utils.isEmpty(line)) return;
		this.log().finest("RAW: "+line);
		final Integer id = utilsNumbers.toInteger(line.substring(0, 2));
		if(id == null) {
			this.log().warning("Invalid id in received packet");
			return;
		}
		// scanning for devices
		if(this.scanning != null) {

		}
		// found new device id
		if(!this.devices.containsKey(id)) {
			final String vers = line.substring(4);
			this.log().stats("Found arduino device id: "+id.toString()+" version: "+vers);
			this.devices.put(
				id,
				new ArduinoDevice(
					this,
					id.intValue(),
					vers
				)
			);
			return;
		}
		// process packet
		this.devices.get(id)
			.processLine(line.substring(2));
	}



	@Override
	public void serialEvent(final SerialPortEvent event) {
		// read data
		if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				// read data
				synchronized(this.readBuffer) {
					while(this.in.available() > 0) {
						final char chr = (char) this.in.read();
						if(chr == ';' || chr == '\r')
							this.readBuffer += '\n';
						else
							this.readBuffer += chr;
					}
				}
				// parse lines from buffer
				this.processBuffer();
			} catch (IOException e) {
				this.log().trace(e);
				return;
			}
		}
//		System.out.println("EVENT TYPE: "+Integer.toString(event.getEventType())+
//				" OLD: "+Boolean.toString(event.getOldValue())+
//				" NEW: "+Boolean.toString(event.getNewValue()));
//		try {
//			System.out.println("DATA: "+String.valueOf(this.in.read()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	@Override
	public void write(final String data) throws IOException {
		this.out.write(data.getBytes());
	}



	// logger
	private final xLog _log;
	@Override
	public xLog log() {
		return this._log;
	}



}
