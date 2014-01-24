package com.github.lyokofirelyte.WCAvoidance;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.lyokofirelyte.WCAPI.Command.WCCommand;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVPlayer;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVSystem;

import static com.github.lyokofirelyte.WCAvoidance.Internal.AVUtils.*;

public class AVCommand implements Listener {
	
	AVMain pl;
	Inventory inv;
	Boolean firstRun = false;
	Boolean mode = false;
	Boolean created = false;
	
	Boolean started = false;
	List<String> players = new ArrayList<>();
	
	public AVCommand(AVMain i){
		pl = i;
    } 
	
	public void setUp(){
		
		inv = Bukkit.createInventory(null, 9, "&bAVOIDANCE!");
		inv.addItem(makeItem("&aJoin", "&2Join a game...", true, Enchantment.DURABILITY, 1, Material.DIAMOND, 1));
		inv.addItem(makeItem("&aLeave", "&2Leave a game...", false, Enchantment.DURABILITY, 1, Material.EMERALD, 1));
		inv.addItem(makeItem("&aStart", "&2Start a game...", false, Enchantment.DURABILITY, 1, Material.GOLD_INGOT, 1));
		inv.addItem(makeItem("&4Reset", "&2Force-Reset a game...", false, Enchantment.DURABILITY, 1, Material.IRON_INGOT, 1));
		inv.addItem(makeItem("&6Create", "&2Create the arena!", false, Enchantment.DURABILITY, 1, Material.REDSTONE, 1));
		inv.addItem(makeItem("&6Delete", "&2Delete the arena!", false, Enchantment.DURABILITY, 4, Material.INK_SACK, 1));
		inv.addItem(makeItem("&bMy Stats", "&2View your stats!", false, Enchantment.DURABILITY, 1, Material.GLOWSTONE_DUST, 1));
		inv.addItem(makeItem("&3High Scores", "&2Leaderboards!", false, Enchantment.DURABILITY, 1, Material.SULPHUR, 1));
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onLeave(PlayerQuitEvent e){
		avLeave(e.getPlayer()); 
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onClick(InventoryClickEvent e){
		
		if (e.isCancelled()){
			return;
		}
		
		if (e.getWhoClicked() instanceof Player && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getInventory().getName().contains("AVOIDANCE!")){
			
			e.setCancelled(true);
			
			Player p = ((Player)e.getWhoClicked());

			switch (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).toLowerCase()){
			
				case "join": avJoin(p); break;
				case "leave": avLeave(p); break;
				case "start": avStart(p); break;
				case "reset": avReset(p); break;
				case "create": pl.avs.createArena(p); break;
				case "delete": pl.avs.arenaVanish(p); break;
				case "my stats": stats(p); break;
				case "high scores": hs(p); break;
			}
		}
	}

	@WCCommand(aliases = {"av", "avoidance"}, help = "/av", desc = "Avoidance Root Command")
	public void onAv(Player sender, String[] args){

		if (!firstRun){
			firstRun = true;
			setUp();
		}
		
		sender.openInventory(inv);
			
		if (args.length == 1 && args[0].equals("debug") && ((Player)sender).getName().equals("Hugh_Jasses")){
			AVSystem system = pl.avm.getAvSystem("system");
			for (int x = 1; x < 17; x++){
				for (Location l : system.getGrid(x)){
					pl.utils.change(l, Material.WOOL, 5, 0);
				}
			}
		}
	}
	
	public void avJoin(Player p){
		
		AVSystem system = pl.avm.getAvSystem("system");
		AVPlayer player = pl.avm.getAvPlayer(p.getName());
		List<String> players = pl.systemYaml.getStringList("RegisteredPlayers");

		if (!players.contains(p.getName())){
			players.add(p.getName());
			pl.systemYaml.set("RegisteredPlayers", players);
		}
		
		if (player == null){
			
			player = new AVPlayer(p.getName());
			pl.avm.updateAvPlayers(p.getName(), player);
		}
		
		if (started || system.getRegisteredPlayers().contains(p.getName())){
			av(p, "The game has already started or you have already joined!");
			return;
		}
		
		if (!p.hasPermission("av.join")){
			av(p, "You don't have permission to join a game!");
			return;
		}
		
		if (!system.getArenaSet()){
			av(p, "The arena has not been formed yet!");
			return;
		}
		
		if (system.getGameStarted()){
			av(p, "The game has already started!");
			return;
		}
		
		system.getRegisteredPlayers().add(p.getName());
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 990000, 10));
		
		Location lc = system.getGrid(1).get(3);
		lc.setY(lc.getY() + 2);
		p.teleport(lc);
		lc.setY(lc.getY() - 2);
		player.setInGame(true);
		
		av(p, "Joined!");
		bc(system, p.getDisplayName() + " &bhas joined the game!");
		
		pl.avm.updateAvSystem("system", system);
		pl.avm.updateAvPlayers(p.getName(), player);
	
	}
	
	public void avLeave(Player p){
		
		AVPlayer player = pl.avm.getAvPlayer(p.getName());
		AVSystem system = pl.avm.getAvSystem("system");
		
		if (!player.getInGame()){
			return;
		}
		
		bc(system, p.getDisplayName() + " &bhas left the game!");
		
		system.getRegisteredPlayers().remove(p.getName());
		system.setPlayersLeft(system.getPlayersLeft() - 1);
		
		player.setInGame(false);
		player.setLives(3);
		player.setLosses(player.getLosses() + 1);
		player.setFallen(false);
	
		pl.avm.updateAvPlayers(p.getName(), player);
		pl.avm.updateAvSystem("system", system);
		
		for (PotionEffect effect : p.getActivePotionEffects()){		
			if (effect.getType() != PotionEffectType.DAMAGE_RESISTANCE){
				p.removePotionEffect(effect.getType());
			}		
		}
		
		Bukkit.getServer().getScheduler().cancelTask(player.getTask());
		av(p, "Left!");
	}
	
	public void avStart(Player p){
		
		if (!p.hasPermission("av.admin")){
			av(p, "You're not permitted to start the game.");
			return;
		}
		
		AVSystem system = pl.avm.getAvSystem("system");
		
		if (system.getGameStarted()){
			av(p, "A game is already in progress!");
			return;
		}
		
		system.setGameStarted(true);
		system.setPlayersLeft(system.getRegisteredPlayers().size());
		bc(system, p.getDisplayName() + " &bhas started the game!");
		pl.avm.updateAvSystem("system", system);
		p.closeInventory();
		
		pl.round.startGame();

	}
	
	public void avReset(Player p){
		
		AVSystem system = pl.avm.getAvSystem("system");
		
		if (!p.hasPermission("av.admin")){
			av(p, "You're not permitted to reset the game.");
			return;
		}

		p.closeInventory();
		
		for (int x = 1; x < 17; x++){
			
			for (Location l : system.getGrid(x)){
				pl.utils.change(l, Material.WOOL, 0, 0);
			}
		}

		system.setGameStarted(false);
		system.setRegisteredPlayers(new ArrayList<String>());
		system.setGameStarted(false);
		system.setPlayersLeft(0);
		system.setRound(100);
		
		av(p, "Arena reset.");
		
		pl.avm.updateAvSystem("system", system);
	}
	
	public void stats(Player p){
		
		AVPlayer player = pl.avm.getAvPlayer(p.getName());
		
		av(p, "Stats for " + p.getDisplayName());
		av(p, "Wins: " + player.getWins());
		av(p, "Losses: " + player.getLosses());
		av(p, "Score: " + player.getScore());
	}
	
	public void hs(Player pla){
		
		String Top = null;
		String Middle = null;
		String Bottom = null;
		
		int top = 0;
		int middle = 0;
		int bottom = 0;
		
		List<String> players = pl.systemYaml.getStringList("RegisteredPlayers");
		
		for (String p : players){
			
			AVPlayer player = pl.avm.getAvPlayer(p);
			
			if (player.getScore() > top){
				top = player.getScore();
				Top = p;
			} else if (player.getScore() > middle){
				middle = player.getScore();
				Middle = p;
			} else if (player.getScore() > bottom){
				bottom = player.getScore();
				Bottom = p;
			}
		}
		
		av(pla, "First: " + Top + " &f//&b " + top);
		av(pla, "Second: " + Middle + " &f//&b " + middle);
		av(pla, "Third: " + Bottom + " &f//&b " + bottom);
	}
}
