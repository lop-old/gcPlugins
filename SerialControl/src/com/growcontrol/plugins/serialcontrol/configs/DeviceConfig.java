package com.growcontrol.plugins.serialcontrol.configs;

import java.util.Map;

import com.growcontrol.plugins.serialcontrol.SerialControl;
import com.poixson.commonapp.config.xConfig;


/**
 * Required Fields
 * ---------------
 * id: 0
 * Interface: serial
 * Description: This is my arduino
 *
 * Serial Only Fields
 * Baud: 9600
 *
 * Ethernet Only Fields
 * --------------------
 * Host: 192.168.1.100
 * Port: 1142
 * Password: Abc123!
 */
public final class DeviceConfig extends xConfig {

	private volatile SerialControl plugin = null;

	public static enum METHOD {RS232, TCP}
	public final METHOD method;

	private final int    id;
	private final String desc;
	// force device firmware version
//	private final String version;

	// serial only fields
	private final String  comm;
	private final Integer baud;

//	// ethernet only fields
//	private final String host;
//	private final int    port;
//	public final String pass;



	public DeviceConfig(final Map<String, Object> data) {
		super(data);
		this.method = methodFromString(
			this.getString("Method")
		);
		this.id   = this.getInt    ("id", 0);
		this.desc = this.getString ("Description");
//		this.version = this.getString("Version");
		this.comm = this.getString ("Comm Port");
		this.baud = this.getInteger("Baud");
//		this.host = this.getString ("Host");
//		this.port = this.getInt    ("Port", 0);
//		this.pass = this.getString ("pass");
	}
	public void init(final SerialControl plugin) {
		this.plugin = plugin;
	}



	public int getId() {
		return this.id;
	}
	public String getDesc() {
		return this.desc;
	}



	public String getComm() {
		return this.comm;
	}
	public int getBaud() {
		if(this.baud == null)
			return this.plugin.getDefaultBaud();
		return this.baud.intValue();
	}



	public static METHOD methodFromString(final String str) {
		if(str == null) return null;
		switch(str.toUpperCase()) {
		case "RS232":
		case "SERIAL":
		case "USB":
		case "COM":
		case "COMM":
			return METHOD.RS232;
		case "TCP":
		case "ETHERNET":
		case "ETH":
		case "NET":
		case "WEB":
			return METHOD.TCP;
		default:
		}
		return null;
	}



}