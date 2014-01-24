package com.github.lyokofirelyte.WCAvoidance.Internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.lyokofirelyte.WCAvoidance.AVMain;

public class AVManager implements Listener {
	
	AVMain pl;
	
	public Map<String, AVPlayer> avPlayers = new HashMap<>();
	public Map<String, AVSystem> avSystem = new HashMap<>();
	
	public AVManager(AVMain instance){
	pl = instance;
    } 
	
	public AVPlayer getAvPlayer(String a){
		
		if (avPlayers.containsKey(a)){
			return avPlayers.get(a);
		} else {
			return null;
		}
	}
	
	public AVSystem getAvSystem(String a){
		
		if (avSystem.containsKey(a)){
			return avSystem.get(a);
		} else {
			return null;
		}
	}
	
	public void updateAvPlayers(String a, AVPlayer b){
		avPlayers.put(a, b);
	}
	
	public void updateAvSystem(String a, AVSystem b){
		avSystem.put(a, b);
	}
	
	public void setupAll(YamlConfiguration playerYaml, YamlConfiguration systemYaml){
		
		List<String> players = systemYaml.getStringList("RegisteredPlayers");
		
		for (String a : players){
			
			AVPlayer player = new AVPlayer(a);
			player.setFallen(false);
			player.setInGame(false);
			player.setLosses(playerYaml.getInt("Users." + a + ".Losses"));
			player.setWins(playerYaml.getInt("Users." + a + ".Wins"));
			player.setScore(playerYaml.getInt("Users." + a + ".Score"));
			avPlayers.put(a, player);
		}
		
		AVSystem system = new AVSystem("system");
		system.setArenaSet(systemYaml.getBoolean("ArenaSet"));
		system.setRegisteredPlayers(systemYaml.getStringList("RegisteredPlayers"));
		migrateLocations(system, systemYaml);
		avSystem.put("system", system);
	}
	
	public void migrateLocations(AVSystem system, YamlConfiguration systemYaml){
		
		if (system.getArenaSet()){
		
			for (int x = 1; x < 17; x++){
				
				List<Location> locList = new ArrayList<>();
				
				for (int i = 1; i < 26; i++){
					double xX = systemYaml.getInt("Grid" + x + ".X." + i);
					double yY = systemYaml.getInt("Grid" + x + ".Y." + i);
					double zZ = systemYaml.getInt("Grid" + x + ".Z." + i);
					Location l = new Location(Bukkit.getWorld(systemYaml.getString("GridWorld")), xX, yY, zZ);
					locList.add(l);
				}
				
				system.setGrid(locList, x);
			}
			
			updateAvSystem("system", system);
		}
	}

	public void saveAll(File playerFile, File systemFile, YamlConfiguration playerYaml, YamlConfiguration systemYaml){
		
		AVSystem system = getAvSystem("system");
		
		if (system.getArenaSet()){
		
			systemYaml.set("GridWorld", system.getGrid(1).get(0).getWorld().getName());
			systemYaml.set("ArenaSet", system.getArenaSet());
			
			for (int x = 1; x < 17; x++){
				
				int y = 0;
				
				for (Location loc : system.getGrid(x)){
					y++;
					systemYaml.set("Grid" + x + ".X." + y, loc.getX());
					systemYaml.set("Grid" + x + ".Y." + y, loc.getY());
					systemYaml.set("Grid" + x + ".Z." + y, loc.getZ());
				}
			}
		
			pl.getLogger().log(Level.INFO, "System file saved!");
			
		} else {
			systemYaml.set("ArenaSet", false);
		}
		
		try {systemYaml.save(systemFile);} catch (IOException e) {e.printStackTrace();}

		for (String player : systemYaml.getStringList("RegisteredPlayers")){
			AVPlayer avp = getAvPlayer(player);
			playerYaml.set("Users." + player + ".Losses", avp.getLosses());
			playerYaml.set("Users." + player + ".Wins", avp.getWins());
			playerYaml.set("Users." + player + ".Score", avp.getScore());
		}
		
		try {playerYaml.save(playerFile);} catch (IOException e) {e.printStackTrace();}
		pl.getLogger().log(Level.INFO, "Player file saved!");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent e){
		
		String p = e.getPlayer().getName();
		
		if (!avPlayers.containsKey(p)){
			avPlayers.put(p, new AVPlayer(p));
		}
	}
}
