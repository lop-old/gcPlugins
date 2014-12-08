package com.growcontrol.plugins.serialcontrol.devices;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import com.growcontrol.gccommon.meta.Meta;
import com.growcontrol.gccommon.meta.MetaAddress;
import com.growcontrol.gccommon.meta.MetaEvent;
import com.growcontrol.gccommon.meta.MetaListener;
import com.growcontrol.gccommon.meta.metaTypes.MetaIO;
import com.growcontrol.plugins.serialcontrol.SerialControl;
import com.growcontrol.plugins.serialcontrol.configs.DeviceConfig;
import com.growcontrol.plugins.serialcontrol.connections.Connection;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.utilsNumbers;
import com.poixson.commonjava.Utils.utilsString;
import com.poixson.commonjava.xLogger.xLog;


public class ArduinoDevice implements MetaListener, Closeable {

	public static final String LINE_ENDING = ";\r\n";

	public static final int OFFSET_ID  = 0;
	public static final int OFFSET_CMD = 2;
	public static final int OFFSET_AR1 = 4;
	public static final int OFFSET_AR2 = 8;
	public static final int SIZE_ID  = 2;
	public static final int SIZE_ARG = 4;

	protected final Connection connection;
	protected final int id;

	protected volatile String firmwareVersion = null;



	public ArduinoDevice(final Connection connection, final int id, final String version) {
		this.connection = connection;
		this.id = id;
		this.firmwareVersion = version;
		// register meta listeners
		{
			final DeviceConfig cfg = this.getPlugin().getDeviceConfig(id);
			if(cfg != null) {
				final Map<String, MetaAddress> addrMap = cfg.getAddresses();
				if(utils.notEmpty(addrMap)) {
					for(final MetaAddress addr : addrMap.values())
						this.getPlugin().register(addr, this);
				}
			}
		}
	}



	public void finalize() {
		utils.safeClose(this);
	}
	@Override
	public void close() throws IOException {
	}



	public void processLine(final String line) {
		if(utils.isEmpty(line)) return;
		final String cmd = line.substring(0, 2);
@SuppressWarnings("unused")
		final String arg = line.substring(2);
		switch(cmd) {
		case "id":
		case "al":
		case "pm":
		case "dw":
		case "aw":
		case "dr":
		case "ar":
		default:
System.out.println("LINE: "+line);
		}
	}



	protected SerialControl getPlugin() {
		return this.connection.getPlugin();
	}



	@Override
	public void onMetaEvent(final MetaEvent event) {
		// event destination
		final MetaAddress addr = event.destination;
		// get pin
		final int pin;
		{
			final String pinStr = addr.getTag("pin");
			final Integer pinInt = utilsNumbers.toInteger(pinStr);
			if(pinInt == null)
				throw new NumberFormatException("Invalid pin: "+pinStr);
			pin = pinInt.intValue();
		}
		// get value
		final String valueStr;
		try {
			final MetaIO meta = (MetaIO) Meta.convert(MetaIO.class, event.meta);
			valueStr =
				meta.value().booleanValue()
					? "HIGH"
					: "LOW_";
		} catch (ReflectiveOperationException e) {
			this.log().trace(e);
			return;
		}
		// send to arduino
		{
			final StringBuilder str = new StringBuilder();
			str.append(utilsString.padFront(
				SIZE_ID,
				Integer.toString(this.id),
				'0'
			));
			str.append("dw");
			str.append(utilsString.padFront(
				SIZE_ARG,
				Integer.toString(pin),
				'0'
			));
			str.append(valueStr);
			str.append(LINE_ENDING);
			try {
				this.write(str.toString());
			} catch (IOException e) {
				this.log().trace(e);
			}
		}
	}



	public void write(final String data) throws IOException {
		this.log().stats("ARDUINO: "+data);
		this.connection.write(data);
	}



	// logger
	public xLog log() {
		return this.connection.log();
	}



//	protected static final int THREAD_HEARTBEAT = 50;
//	protected static final int THREAD_WAIT = 100;

//	// controller types
//	public static enum ControllerType{USB, NET};
//	// pins map
//	public HashMap<Integer, ArduinoPin> outputPins = new HashMap<Integer, ArduinoPin>();
//	// queue
//	protected msgQueue queue = new msgQueue();
//	// interface state
//	protected boolean ready = false;
//	protected boolean stopping = false;


//	public void StartInterface() {
//		this.start();
//	}
//	public void StopInterface() {
//		stopping = true;
//	}
//	public boolean isReady() {
//		return ready && !stopping;
//	}


//	public void setReady() {
//		// register output pins
////		for(Map.Entry<Integer, Pin> entry : outputPins.entrySet()) {
////			registerOutputs(entry.getKey());
////		}
//		for(int pin : outputPins.keySet())
//			registerOutputs(pin);
//		super.setReady();
//	}


//	public boolean sendPinMode(int pinNum, PinMode pinMode) {
//TODO:
//ArduinoGC.log.severe("mode "+Integer.toString(pinNum)+" "+pinMode.toString());
//		sendMessage("mode "+Integer.toString(pinNum)+" "+pinMode.toString());
//		return false;
//	}
//	public boolean sendPinState(int pinNum, int pinState) {
//TODO: is command handled?
//		queue.setPinState(pinNum, pinState);
//		return true;
//ArduinoGC.log.warning("ASDFKJBGDSFN BDSJKFNKLJASDNFASDF "+Integer.toString(pinNum));
//		if(!super.setOutput(pinNum, pinState)) return false;
//		gcPin p = outputPins.get(pinNum);
//		p.pinState = pinState;
//		String data;
//		// io output
//		if(p.pinMode.equals(PinMode.io))
//			data = "pin "+Integer.toString(pinNum)+" "+(pinState==0?"off":"on");
//		// pwm output
//		else if(p.pinMode.equals(PinMode.pwm))
//			data = "pin "+Integer.toString(pinNum)+" "+Integer.toString(GrowControl.MinMax(pinState, 0, 255));
//		else
//			return false;
//		netTester.sendMessage(data);
//		return true;
//	}


//	public boolean onOutput(String[] args) {
//		return false;
//	}


//	// enum from string
//	public static ControllerType controllerTypeFromString(String type) {
//		if(type == null) return null;
//		if(type.equalsIgnoreCase("usb") || type.equalsIgnoreCase("com"))
//			return ControllerType.USB;
//		else if(type.equalsIgnoreCase("net") || type.equalsIgnoreCase("ethernet"))
//			return ControllerType.NET;
//		return null;
//	}


}
