package com.github.lyokofirelyte.WCAvoidance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.lyokofirelyte.WCAPI.WCAPI;
import com.github.lyokofirelyte.WCAPI.WCNode;
import com.github.lyokofirelyte.WCAvoidance.Game.AVDetection;
import com.github.lyokofirelyte.WCAvoidance.Game.AVRounds;
import com.github.lyokofirelyte.WCAvoidance.Game.AVSetup;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVManager;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVUtils;
import com.github.lyokofirelyte.WCAvoidance.Internal.FireworkShenans;

public class AVMain extends WCNode {
	
	File playerFile;
	File systemFile;
	
	YamlConfiguration playerYaml;
	YamlConfiguration systemYaml;
	
	public AVSetup avs;
	public AVManager avm;
	public AVUtils utils;
	public AVRounds round;
	public FireworkShenans fireworks;
	public WCAPI api;
	
	public void onEnable(){

		PluginManager pm = getServer().getPluginManager();
		Plugin WCAPI = pm.getPlugin("WCAPI");
		api = (WCAPI) WCAPI;
		avs = new AVSetup(this);  
		avm = new AVManager(this);
		utils = new AVUtils(this);
		round = new AVRounds(this);
		fireworks = new FireworkShenans();
		reg(pm);
		try { loadFiles(); } catch (IOException e) { e.printStackTrace(); }
		
		getLogger().log(Level.INFO, "Thanks for downloading my plugin! It means a lot to me :)");	
	}

	public void onDisable(){
		avm.saveAll(playerFile, systemFile, playerYaml, systemYaml);
	}
	
	public void reg(PluginManager pm){
		
		api.reg.registerCommands(new ArrayList<Class<?>>(Arrays.asList(AVCommand.class)), this);
		pm.registerEvents(new AVCommand(this),this);
		pm.registerEvents(avm,this);
		pm.registerEvents(new AVRounds(this),this);
		pm.registerEvents(new AVDetection(this),this);
	}
	
	private void loadFiles() throws IOException {
		
		playerFile = new File("./plugins/Avoidance/players.yml");
		systemFile = new File("./plugins/Avoidance/system.yml");
	
		if (!playerFile.exists()){
			playerFile.getParentFile().mkdirs();
			playerFile.createNewFile();
		}
		
		if (!systemFile.exists()){
			playerFile.getParentFile().mkdirs();
			playerFile.createNewFile();
		}

		playerYaml = YamlConfiguration.loadConfiguration(playerFile);
		systemYaml = YamlConfiguration.loadConfiguration(systemFile);
		avm.setupAll(playerYaml, systemYaml);
	}

}
