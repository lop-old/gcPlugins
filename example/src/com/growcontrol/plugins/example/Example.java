package com.growcontrol.plugins.example;

import com.growcontrol.server.plugins.gcServerPlugin;


public class Example extends gcServerPlugin {



	// load/unload plugin
	@Override
	public void onEnable() {
		this.log().info("Example plugin enabled!");
		// register listeners
		this.register(new exampleCommands());
//		// load configs
//		Config.get("plugins/"+getName()+"/");
//		if(!Config.isLoaded()) {
//			getLogger().severe("Failed to load "+Config.CONFIG_FILE);
//			return;
//		}
	}
	@Override
	public void onDisable() {
		this.log().info("Example plugin disabled!");
	}



}
