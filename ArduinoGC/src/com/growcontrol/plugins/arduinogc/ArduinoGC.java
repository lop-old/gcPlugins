package com.growcontrol.arduinogc;

import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;


//implements gcServerListenerOutput
public class ArduinoGC extends gcServerPlugin {

//	// controllers map
//	protected static HashMap<String, ArduinoInterface> controllers = new HashMap<String, ArduinoInterface>();


	@Override
	public void onEnable() {
		// register listeners
		register(Commands.get());
//		registerListenerOutput(this);
		// load configs
		Config.get("plugins/"+getName()+"/");
		if(!Config.isLoaded()) {
			getLogger().severe("Failed to load "+Config.CONFIG_FILE);
			return;
		}
	}
	@Override
	public void onDisable() {
//		for(ArduinoInterface controller : controllersMap.values())
//			controller.StopInterface();
//		getLogger().info("ArduinoGC disabled!");
	}


//	// load arduino configs
//	private void LoadConfig() {
//		pxnConfig config = pxnConfig.loadFile("plugins/ArduinoGC", "config.yml");
//		if(config == null) {
//			log.severe("Failed to load config.yml");
//			return;
//		}
//		List<String> controllers = config.getStringList("Controllers");
//		if(controllers == null) {
//			log.severe("Failed to load controllers from config.yml");
//			return;
//		}
//		// load controller configs
//		for(String controller : controllers)
//			LoadArduinoConfig(controller);
//	}
//	private void LoadArduinoConfig(String configFile) {
//		if(!configFile.endsWith(".yml")) configFile += ".yml";
//		pxnConfig config = pxnConfig.loadFile("plugins/ArduinoGC/controllers", configFile);
//		if(config == null) {
//			log.severe("Failed to load "+configFile);
//			return;
//		}
//		String name = configFile.substring(0, configFile.lastIndexOf("."));
//		String title = config.getString("Title");
//		String type = config.getString("Type");
//log.severe("TITLE: "+title);
//log.severe("TYPE:  "+type);
//		// load new controller
//		ArduinoInterface controller = newController(config, name, title, type);
//		if(controller == null) {
//			log.severe("Failed to load controller! "+configFile);
//			return;
//		}
//		controller.StartInterface();
//	}


//	// create new controller
//	public static ArduinoInterface newController(pxnConfig config, String name, String title, String type) {
//		return newController(config, name, title, ArduinoInterface.controllerTypeFromString(type));
//	}
//	public static ArduinoInterface newController(pxnConfig config, String name, String title, ArduinoInterface.ControllerType type) {
//		if(config==null || name==null || title==null || type==null) return null;
//		ArduinoInterface controller = null;
//		synchronized(controllersMap) {
//			if(controllersMap.containsKey(name)) {
//				log.warning("A controller named \""+name+"\" already exists!");
//				return null;
//			}
//			if(type.equals(ArduinoInterface.ControllerType.USB)) {
//				String port = config.getString("Port");
//				controller = new ArduinoUSB(port);
//			} else if(type.equals(ArduinoInterface.ControllerType.NET)) {
//				String host = config.getString("Host");
//				int port = config.getInt("Port");
//				controller = new ArduinoNet(host, port);
//			} else					log.severe("Unknown controller type: "+type.toString());
//			if(controller == null)	log.severe("Unable to create new controller!");
//			// add to hash map
//			controllersMap.put(name, controller);
//		}
//		return controller;
//	}


//	@Override
//	public boolean onOutput(String[] args) {
//		if(!controllersMap.containsKey(args[0])) return false;
//		ArduinoInterface controller = controllersMap.get(args[0]);
//		int pinNum = -1;
//		try {
//			pinNum = Integer.valueOf(args[1]);
//		} catch(Exception ignore) {}
//		int pinState = -1;
//		try {
//			if(args[2].equalsIgnoreCase("on"))
//				pinState = 1;
//			else if(args[2].equalsIgnoreCase("off"))
//				pinState = 0;
//			else
//				pinState = Integer.valueOf(args[2]);
//		} catch(Exception ignore) {}
//		if(pinNum<0 || pinState<0) {
//			String msg = "";
//			for(String arg : args) msg += " "+arg;
//			log.severe("Invalid command"+msg);
//			return false;
//		}
//		controller.sendPinState(pinNum, pinState);
//		return true;
//	}


}
