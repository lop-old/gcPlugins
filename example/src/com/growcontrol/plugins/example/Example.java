package com.growcontrol.plugins.example;

import com.growcontrol.gccommon.plugins.gcServerPlugin;
import com.poixson.commonapp.plugin.xPluginManager;
import com.poixson.commonapp.plugin.xPluginYML;


public class Example extends gcServerPlugin {



	public Example(xPluginManager manager, xPluginYML yml) {
		super(manager, yml);
	}



	// load/unload plugin
	@Override
	public void onEnable() {
/*
		// register listeners
		register(Commands.get());
		// load configs
		Config.get("plugins/"+getName()+"/");
		if(!Config.isLoaded()) {
			getLogger().severe("Failed to load "+Config.CONFIG_FILE);
			return;
		}
*/
	}
	@Override
	public void onDisable() {
	}



}
