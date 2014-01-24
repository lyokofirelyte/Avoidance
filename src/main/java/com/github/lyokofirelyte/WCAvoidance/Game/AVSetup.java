package com.github.lyokofirelyte.WCAvoidance.Game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static com.github.lyokofirelyte.WCAvoidance.Internal.AVUtils.*;

import com.github.lyokofirelyte.WCAvoidance.AVMain;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVSystem;

public class AVSetup {

	AVMain pl;
	AVSystem system;
	List<Location> cGrid;
	World w;
	double y;
	Entity e;
	List<Entity> entList = new ArrayList<Entity>();
	
	public AVSetup(AVMain instance){
	pl = instance;
    }
	
	public void createArena(final Player p){
		
		system = pl.avm.getAvSystem("system");
		
		if (!p.hasPermission("av.admin")){
			av(p, "You don't have permission for this.");
			return;
		}

		if (system.getArenaSet()){
			av(p, "The arena is already set! Use the arena delete function first.");
			return;
		}
		
		p.closeInventory();
		av(p, "Creating arena...");
		
		w = p.getWorld();
		Location playerLoc = p.getLocation();
		
		system.setArenaSet(true);
	
		double sX = playerLoc.getX();
		y = playerLoc.getY();
		double sZ = playerLoc.getZ();
		
		getLocs((int)sX-11, (int)sZ-11, 1);
		getLocs((int)sX-5, (int)sZ-11, 2);
		getLocs((int)sX+1, (int)sZ-11, 3);
		getLocs((int)sX+7, (int)sZ-11, 4);
		getLocs((int)sX+7, (int)sZ-5, 5);
		getLocs((int)sX+7, (int)sZ+1, 6);
		getLocs((int)sX+7, (int)sZ+7, 7);
		getLocs((int)sX+1, (int)sZ+7, 8);
		getLocs((int)sX-5, (int)sZ+7, 9);
		getLocs((int)sX-11, (int)sZ+7, 10);
		getLocs((int)sX-11, (int)sZ+1, 11);
		getLocs((int)sX-11, (int)sZ-5, 12);
		getLocs((int)sX-5, (int)sZ-5, 13);
		getLocs((int)sX+1, (int)sZ-5, 14);
		getLocs((int)sX+1, (int)sZ+1, 15);
		getLocs((int)sX-5, (int)sZ+1, 16);
		
		pl.avm.updateAvSystem("system", system);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
		public void run() { arenaChange(p, Material.WOOL);} }, 20L);

	}
	
	public void arenaVanish(Player p){
		
		final AVSystem system = pl.avm.getAvSystem("system");
		
		if (!system.getArenaSet()){
			av(p, "The arena has already been de-virtualized!");
			return;
		} else {
			p.closeInventory();
			av(p, "De-virtualizing!");
		}
		
		for (int x = 1; x < 17; x++){
				
			for (Location l : system.getGrid(x)){	
			
				e = l.getWorld().spawnFallingBlock(l, l.getBlock().getType(), (byte)0);
				entList.add(e);
				l.getBlock().setType(Material.AIR);
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
				public void run() { 
					
					for (Entity ent : entList){
						ent.remove();
					}
					
					entList = new ArrayList<Entity>();
	
				}}, 30L);
			}
		}
		
		for (int x = 1; x < 17; x++){
			system.setGrid(new ArrayList<Location>(), x);
		}
		
		system.setArenaSet(false);
		pl.avm.updateAvSystem("system", system);
	}

	public void arenaChange(Player p, final Material mat){
		
		final AVSystem system = pl.avm.getAvSystem("system");
		
		if (mat == Material.AIR && !system.getArenaSet()){
			av(p, "The arena has already been de-virtualized!");
			return;
		}
		
		long delay = 0L;

		for (int x = 1; x < 17; x++){
				
			for (final Location l : system.getGrid(x)){
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
				public void run() { 
					
					pl.utils.change(l, Material.WOOL, (byte) 0, 0L);
				
      	        	try {
    			
							pl.fireworks.playFirework(l.getWorld(), l,
							FireworkEffect.builder().with(Type.BURST).withColor(Color.WHITE).build());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}  
      	        	
				}}, delay);
				
				delay = delay+1L;
			}
		}
		
		delay = delay+20L;
		
		for (int x = 1; x < 17; x++){
			
			for (final Location l : system.getGrid(x)){

				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
					public void run() { 
				
						for (Player p : Bukkit.getOnlinePlayers()){
							pl.utils.forceBlockLight(p, (int)l.getX(), (int)l.getY(), (int)l.getZ(), 15);
							pl.utils.queueChunk(p, (int)l.getChunk().getX(), (int)l.getChunk().getZ());
						}
				
				}}, delay);
		
			}
		}
	}
	
	public void getLocs(int xStart, int zStart, int grid){
		
		Location temp = new Location(w, xStart, y, zStart);
		system.getGrid(grid).add(temp);
		
		int i = 0;
		int ii = 0;

		for (int x = 1; x < 5; x++){
			i++;
			temp = new Location(w, xStart+i, y, zStart);
			system.getGrid(grid).add(temp);
		}
		
		for (int x = 1; x < 5; x++){
			ii++;
			temp = new Location(w, xStart+i, y, zStart+ii);
			system.getGrid(grid).add(temp);
		}
		
		for (int x = 1; x < 5; x++){
			i--;
			temp = new Location(w, xStart+i, y, zStart+ii);
			system.getGrid(grid).add(temp);
		}
		
		for (int x = 1; x < 4; x++){
			ii--;
			temp = new Location(w, xStart, y, zStart+ii);
			system.getGrid(grid).add(temp);
		}
		
		i = 0;
		ii = 1;

		for (int x = 1; x < 4; x++){
			i++;
			temp = new Location(w, xStart+i, y, zStart+ii);
			system.getGrid(grid).add(temp);
		}
		
		for (int x = 1; x < 3; x++){
			ii++;
			temp = new Location(w, xStart+i, y, zStart+ii);
			system.getGrid(grid).add(temp);
		}
		
		for (int x = 1; x < 3; x++){
			i--;
			temp = new Location(w, xStart+i, y, zStart+ii);
			system.getGrid(grid).add(temp);
		}
		
		ii--;
		temp = new Location(w, xStart+i, y, zStart+ii);
		system.getGrid(grid).add(temp);
		
		temp = new Location(w, xStart+2, y, zStart+ii);
		system.getGrid(grid).add(temp);
	}
}
