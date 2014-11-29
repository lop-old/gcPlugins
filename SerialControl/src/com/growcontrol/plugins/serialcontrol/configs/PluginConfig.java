package com.growcontrol.plugins.serialcontrol.configs;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.poixson.commonapp.config.xConfig;


public class PluginConfig extends xConfig {

	private final Set<String> scannablePorts = new HashSet<String>();
	private final int baud;



	public PluginConfig(final Map<String, Object> data) {
		super(data);
		// scannable comm ports
		this.scannablePorts.clear();
		this.scannablePorts.addAll(
			this.getStringList("Scan Comm Ports")
		);
		// default baud rate
		this.baud = this.getInt("Default Baud", 9600);
	}



	// scannable comm ports
	public String[] getScannable() {
		return this.scannablePorts.toArray(new String[0]);
	}



	// default baud rate
	public int getBaud() {
		return this.baud;
	}



}
