package com.github.lyokofirelyte.WCAvoidance.Internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_7_R1.EnumSkyBlock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.github.lyokofirelyte.WCAPI.WCUtils;
import com.github.lyokofirelyte.WCAvoidance.AVMain;

public class AVUtils {
	
	AVMain pl;
	static ItemStack i;
	static ItemMeta iMeta;
	List<String> loreSplit;
	List<Entity> entList = new ArrayList<Entity>();
	public List<Integer> usedGrids = new ArrayList<Integer>();
	List<Location> gridLoc;
	
	public AVUtils(AVMain instance){
	pl = instance;
    }
	
	public static ItemStack makeItem(String dispName, String lore, Boolean e, Enchantment enchant, int itemType, Material mat, int itemAmount){
		
		i = new ItemStack(mat, itemAmount, (short) itemType);
        iMeta = i.getItemMeta();
    	List<String> loreList = new ArrayList<>();
    	loreList.add(WCUtils.AS(lore));
        
        if (e){
        	iMeta.addEnchant(enchant, 10, true);
        }

        iMeta.setDisplayName(WCUtils.AS(dispName));
        iMeta.setLore(loreList);
        i.setItemMeta(iMeta);
		
		return i;
	}
	
	public static void bc(AVSystem system, String s){
		
		List<String> players = system.getRegisteredPlayers();
		
			for (String p : players){
				av(Bukkit.getPlayer(p), s);
			}
	}
	
	public static void av(Player p, String s){
		WCUtils.s2(p, ChatColor.translateAlternateColorCodes('&', "&bAV &f//&b " + s));
	}
	
	@SuppressWarnings("deprecation") 
	public void change(final Location l, final Material mat, final int type, long delay){
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
		public void run() { 
			
			if (l.getBlock().getType() == mat){
				l.getBlock().setData((byte) type);
			} else {
				l.getBlock().setType(mat);
				l.getBlock().setData((byte) type);
			}
		
		}}, delay);
		
	}
	
	@SuppressWarnings("deprecation")
	public void dropEntities(Location l){
		
		Entity ent = l.getWorld().spawnFallingBlock(l, l.getBlock().getType(), (byte) l.getBlock().getData());
		l.getBlock().setType(Material.AIR);
		entList.add(ent);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
		public void run() { 
			
			for (Entity e : entList){
				e.remove();
			}
			
			entList = new ArrayList<Entity>();
			
		}}, 30L);
		
	}
	
	@SuppressWarnings("deprecation")
	public int syncChange(AVSystem system, final Material mat, final int type){
		
		int rand = getRandom(15);
		
		while (usedGrids.contains(rand)){
			rand = getRandom(15);
		}
		
		if (type == 4 || type == 14 || type == 0){
			usedGrids.add(rand);			
		}
		
		gridLoc = system.getGrid(rand);
		
		for (Location l : gridLoc){
		
			if (l.getBlock().getType() == mat){
				l.getBlock().setData((byte) type);
			} else {
				l.getBlock().setType(mat);
				l.getBlock().setData((byte) type);
			}
		}
		
		try {
			pl.fireworks.playFirework(system.getGrid(1).get(0).getWorld(), system.getGrid(1).get(0),
			FireworkEffect.builder().with(Type.BURST).withColor(getRandomColor()).build());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rand;
	}
	
	public void whiteToAir(AVSystem system, long delay, long increment1, long increment2){
	
		int rand = getRandom(15);
		
		while (usedGrids.contains(rand)){
			rand = getRandom(15);
		}
			
		List<Location> gridLoc = system.getGrid(rand);
		usedGrids.add(rand);
		
		for (Location l : gridLoc){
			
			change(l, Material.WOOL, (byte) 4, delay);
			delay = delay + increment1;
		}
		
		delay = delay + increment2;
		
		for (Location l : gridLoc){
			
			change(l, Material.WOOL, (byte) 14, delay);
			delay = delay + increment1;
		}
		
		delay = delay + increment2;
		
		for (Location l : gridLoc){
	
			change(l, Material.AIR, (byte) 0, delay);
			delay = delay + increment1;
		}
	
	}
	
	public void yellowToAir(AVSystem system, long delay, long increment1, long increment2){
		
		int rand = getRandom(15);
		
		while (usedGrids.contains(rand)){
			rand = getRandom(15);
		}
			
		List<Location> gridLoc = system.getGrid(rand);
		usedGrids.add(rand);
		
		for (Location l : gridLoc){
			
			change(l, Material.WOOL, (byte) 14, delay);
			delay = delay + increment1;
		}
		
		delay = delay + increment2;
		
		for (Location l : gridLoc){
	
			change(l, Material.AIR, (byte) 0, delay);
			delay = delay + increment1;
		}
	
	}
	
	public void redToAir(AVSystem system, long delay, long increment1, long increment2){
		
		int rand = getRandom(15);
		
		while (usedGrids.contains(rand)){
			rand = getRandom(15);
		}
			
		List<Location> gridLoc = system.getGrid(rand);
		usedGrids.add(rand);
		
		for (Location l : gridLoc){
	
			change(l, Material.AIR, (byte) 0, delay);
			delay = delay + increment1;
		}
	
	}
	
	public void flingEntites(int type, int deny){
		
		for (int x = 1; x < 17; x++){
			
			for (Location l : pl.avm.getAvSystem("system").getGrid(x)){	
				
				if (x != deny){
			
					Entity e = l.getWorld().spawnFallingBlock(l, l.getBlock().getType(), (byte)type);
					e.setVelocity(new Vector(0, 2, 0));
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
		}
	}
	
	public void forceBlockLight(Player player, int x, int y, int z, int level) {
		net.minecraft.server.v1_7_R1.World w = ((CraftWorld) player.getWorld()).getHandle();
		w.b(EnumSkyBlock.BLOCK, x, y, z, level);
	}
    
    @SuppressWarnings("unchecked")
	public void queueChunk(Player player, int cx, int cz) {
    	((CraftPlayer) player).getHandle().chunkCoordIntPairQueue.add(new ChunkCoordIntPair(cx, cz));
    }
	
	public int getRandom(int a){
		Random rand = new Random();
		return rand.nextInt(a)+1;
	}
	
	public static Color getRandomColor(){
		
		final List<Color> colors = Arrays.asList(Color.RED, Color.WHITE, Color.BLUE, Color.ORANGE, Color.FUCHSIA, Color.AQUA, Color.PURPLE, Color.GREEN, Color.TEAL, Color.YELLOW);
		Random rand = new Random();
		int nextInt = rand.nextInt(10);
		return colors.get(nextInt);
	}
}
