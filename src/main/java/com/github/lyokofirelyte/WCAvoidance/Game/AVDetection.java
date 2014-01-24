package com.github.lyokofirelyte.WCAvoidance.Game;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.github.lyokofirelyte.WCAvoidance.AVMain;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVPlayer;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVSystem;

import static com.github.lyokofirelyte.WCAvoidance.Internal.AVUtils.*;

public class AVDetection implements Listener {
	
	AVSystem system;
	AVPlayer player;
	Boolean cooldown;
	
	AVMain pl;

	public AVDetection(AVMain instance){
	pl = instance;
    } 
	
	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent e){
		
		try { 
			
			player = pl.avm.getAvPlayer(e.getPlayer().getName()); 
		
			if (player == null){
				return;
			}
		
		} catch (NullPointerException ex) { 
			
			ex.printStackTrace();
		}
		
		Player p = e.getPlayer();

		if (player.getInGame()){		
			
			system = pl.avm.getAvSystem("system");
			
			if (p.getLocation().getY() < system.getGrid(1).get(0).getY()){
				onFall(player, e.getPlayer());
				return;
			}
			
			double yy = p.getLocation().getY() - 1;
			Location pLoc = new Location(p.getWorld(), p.getLocation().getX(), yy, p.getLocation().getZ());
			
			if (pLoc.getBlock().getType() != Material.WOOL){
				return;
			}
            
            switch (pLoc.getBlock().getData()){
            
            	case 0x1: aP(p, PotionEffectType.SLOW, 8000, 3); break;
            	case 0xD: aP(p, PotionEffectType.SPEED, 8000, 3); break;
            	case 0xA: aP(p, PotionEffectType.JUMP, 8000, 3); break;
            	case 0xB: player.setScore(player.getScore() + 5); pl.avm.updateAvPlayers(p.getName(), player); av(p, "&61 point received!"); break;
            	
            	default: 
            		for (PotionEffect effect : p.getActivePotionEffects()){
            			if (effect.getType() != PotionEffectType.DAMAGE_RESISTANCE){
            				p.removePotionEffect(effect.getType());
            			}
            		}
            	break;        
            }	
		}
	}

	public void aP(Player p, PotionEffectType effect, int dur, int amt){
		p.addPotionEffect(new PotionEffect(effect, dur, amt));
	}
	
	public void onFall(AVPlayer pla, final Player p){
		
		system = pl.avm.getAvSystem("system");
		player = pla;
		
		int xx = 0;
		
		Random rand = new Random();
		int random = rand.nextInt(14) + 1;
		
		while (pl.utils.usedGrids.contains(random) && xx < 50){
			rand = new Random();
			random = rand.nextInt(14) + 1;
			xx++;
		}
		
		if (player.getLives() <= 0){
			av(p, "You're out of lives! Enjoy the depths below!");
			system.setPlayersLeft(system.getPlayersLeft() - 1);
			player.setLosses(player.getLosses() + 1);
			player.setInGame(false);
			
			pl.avm.updateAvSystem("system", system);
			pl.avm.updateAvPlayers(p.getName(), player);
			p.teleport(new Location(p.getWorld(), system.getGrid(random).get(24).getX(), system.getGrid(random).get(24).getY()-3, system.getGrid(random).get(24).getZ()));
			return;
		}
		
		if (!system.getGameStarted()){
			av(p, "The game has not started yet! Be more careful!");
			p.teleport(new Location(p.getWorld(), system.getGrid(random).get(24).getX(), system.getGrid(random).get(24).getY()+8, system.getGrid(random).get(24).getZ()));
			return;
		}
		
		player.setLives(player.getLives() - 1);
		player.setCombo(0);
		
		p.teleport(new Location(p.getWorld(), system.getGrid(random).get(24).getX(), system.getGrid(random).get(24).getY()+8, system.getGrid(random).get(24).getZ()));
		av(p, "Re-entered! You have &6" + player.getLives() + " &blives remaining.");
		
		pl.avm.updateAvPlayers(p.getName(), player);
		pl.avm.updateAvSystem("system", system);
	}

}
