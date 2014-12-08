package com.growcontrol.plugins.serialcontrol;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.growcontrol.plugins.serialcontrol.configs.DeviceConfig;
import com.growcontrol.plugins.serialcontrol.configs.PluginConfig;
import com.growcontrol.plugins.serialcontrol.connections.ConnectionSerial;
import com.growcontrol.server.plugins.gcServerPlugin;
import com.poixson.commonapp.config.xConfigLoader;
import com.poixson.commonjava.xVars;
import com.poixson.commonjava.Utils.utils;


//implements gcServerListenerOutput
public class SerialControl extends gcServerPlugin {

	// config.yml for plugin
	protected volatile PluginConfig config = null;
	// device configs
	protected final Set<DeviceConfig> configs = new HashSet<DeviceConfig>();

	// comm ports
	protected final Map<String, ConnectionSerial> comms = new HashMap<String, ConnectionSerial>();



	@Override
	public void onEnable() {
		// prepare rxtx library
		if(!utils.isRxtxAvailable()) {
			this.fail("rxtx library not found");
			return;
		}
		if(xVars.get().debug()) {
			System.setProperty("rxtx.rebundled.debug", "true");
			System.setProperty("rxtx.rebundled.suppress_error", "false");
			System.out.println("RXTX SEARCH PATH: "+System.getProperty("java.library.path"));
		} else {
			System.setProperty("rxtx.rebundled.debug", "false");
			System.setProperty("rxtx.rebundled.suppress_error", "true");
		}
		// load configs
		this.onLoadConfigs();
		// register listeners
		register(new Commands());
		// scan for devices
		this.onScanDevices();
	}
	@Override
	public void onDisable() {
		// disconnect all

	}



	public void onLoadConfigs() {
		final String SEP = File.separator;
		final String DIR = "plugins"+SEP+this.getPluginName();
		// load config.yml
		this.config = (PluginConfig) xConfigLoader.Load(
			DIR+SEP+"config.yml",
			PluginConfig.class
		);
		// load device configs
		synchronized(this.configs) {
			this.configs.clear();
			final File path = new File(DIR);
			for(final File file : path.listFiles()) {
				if(file == null) continue;
				final String filename = file.toString();
				if(utils.isEmpty(filename)) continue;
				// hidden file
				if(filename.startsWith(".")) continue;
				// ends with .yml
				if(!filename.endsWith(".yml")) continue;
				// ignore config.yml
				if(filename.endsWith(SEP+"config.yml")) continue;
				// load config file
				final DeviceConfig cfg = (DeviceConfig) xConfigLoader.Load(
					file,
					DeviceConfig.class
				);
				if(cfg == null) {
					log().severe("Failed to load config file: "+filename);
					continue;
				}
				cfg.init(this);
				this.configs.add(cfg);
				// use defined port
				{
					final String portName = cfg.getComm();
					if(utils.notEmpty(portName)) {
						this.log().stats("Explicit port: "+portName);
						this.openComm(portName);
					}
				}
			}
		}
		// finished loading configs
		log().stats("Loaded [ "+Integer.toString(this.configs.size())+" ] device config files "+
				"and [ "+Integer.toString(this.config.getScannable().length)+" ] interfaces");
	}
	public void onScanDevices() {
		final List<String> scannable = Arrays.asList( this.config.getScannable() );
		if(utils.isEmpty(scannable)) {
			this.log().stats("No scannable comm ports are set");
			return;
		}
		// existing comm ports
		final Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		final Set<CommPortIdentifier> idents = new HashSet<CommPortIdentifier>();
		while(portEnum.hasMoreElements())
			idents.add( (CommPortIdentifier) portEnum.nextElement() );
		if(idents.isEmpty()) {
			this.log().stats("No comm ports available");
			return;
		}
		log().stats("Scanning [ "+Integer.toString(idents.size())+" ] comm ports for available Arduino devices..");
		synchronized(this.comms){
			for(final CommPortIdentifier ident : idents) {
				final int type = ident.getPortType();
				final String portName = ident.getName();
				// is scannable
				if(!scannable.isEmpty() && !scannable.contains(portName))
					continue;
				// RS232 serial
				if(type == CommPortIdentifier.PORT_SERIAL) {
					// already opened this port
					if(this.comms.containsKey(portName))
						continue;
					// port already open
					if(ident.isCurrentlyOwned()) {
						log().fine("Comm port already in use: "+portName);
						continue;
					}
					this.log().stats("Scanning port: "+portName);
					// open comm port
					if(!this.openComm(portName))
						continue;
				} else
				// RS485 serial
				if(type == CommPortIdentifier.PORT_RS485) {
//TODO:
this.log().severe("RS485 PORT FOUND - this is unfinished");
				}
			}
		}
	}



	public boolean openComm(final String portName) {
		if(utils.isEmpty(portName)) throw new NullPointerException();
		final ConnectionSerial connection;
		try {
			final CommPortIdentifier ident = CommPortIdentifier.getPortIdentifier(portName);
			if(ident == null) throw new IOException("Unknown comm port: "+portName);
			connection = new ConnectionSerial(
				this,
				ident,
				this.getDefaultBaud()
			);
		} catch (NoSuchPortException e) {
			this.log().warning("No such port: "+portName);
			return false;
		} catch (PortInUseException e) {
			log().fine("Comm port already in use: "+portName);
			return false;
		} catch (IOException e) {
			this.log().warning("Failed to open comm port: "+portName);
			this.log().trace(e);
			return false;
		}
		this.comms.put(
			portName,
			connection
		);
		return true;
	}



	public int getDefaultBaud() {
		return this.config.getBaud();
	}



	public void removeComm(final String port) {
		synchronized(this.comms) {
			this.comms.remove(port);
		}
	}



}
