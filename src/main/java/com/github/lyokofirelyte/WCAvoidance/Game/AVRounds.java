package com.github.lyokofirelyte.WCAvoidance.Game;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.WCAvoidance.AVMain;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVPlayer;
import com.github.lyokofirelyte.WCAvoidance.Internal.AVSystem;

import static com.github.lyokofirelyte.WCAvoidance.Internal.AVUtils.*;

public class AVRounds implements Listener {
	
	AVMain pl;
	AVPlayer player;
	AVSystem system;
	int zed = 0;
	int counter = 0;
	int mainTask;
	int memoryGrid;

	public AVRounds(AVMain instance){
	pl = instance;
    } 

	
	public void startGame(){
		
		mainTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new Runnable(){
		public void run() { 
			
			system = pl.avm.getAvSystem("system");
			
			switch (counter){
			
				case 1: bc(system, "Round 1 is starting! Pay attention to the squares!"); break;
				
				case 4: bc(system, "The &fwhite &bsquares will turn &eyellow&b, which will turn &cred&b."); break;
				
				case 7: bc(system, "Let's try some easy ones."); break;
				
				case 9: case 12: case 17: case 22: case 27: case 32: case 37: case 42: pl.utils.whiteToAir(system, 0L, 1L, 40L); break;
				
				case 47: upRound(2); break;
				
				case 50: bc(system, "Time for round 2! If you fell already, time to look alive! We're getting quicker!"); break;
				
				case 58: case 61: case 64: case 67: case 70: case 73: case 76: case 79: case 82: case 85: pl.utils.whiteToAir(system, 0L, 1L, 40L); break;
				
				case 90: upRound(3); break;
				
				case 95: bc(system, "Time to introduce new content! The &agreen &bsquares will speed you up, while the &6orange &bones will slow you down!"); break;
				
				case 100: case 101: case 102: case 103: pl.utils.syncChange(system, Material.WOOL, 1); break;
				
				case 115: case 118: case 121: case 124: case 127: case 130: pl.utils.whiteToAir(system, 0L, 1L, 40L); break;
				
				case 135: case 136: case 137: case 138: pl.utils.syncChange(system, Material.WOOL, 13); break;
				
				case 146: case 149: case 152: case 155: case 158: case 161: pl.utils.whiteToAir(system, 0L, 1L, 40L); break;
				
				case 165: upRound(4); break;
				
				case 170: bc(system, "Who needs white wool anyway?"); break;
				
				case 173: allChange(4); break;
				
				case 176: case 178: case 180: case 182: case 184: case 186: case 188: case 190: case 192: case 194: case 196: case 198: pl.utils.yellowToAir(system, 0L, 1L, 40L); break;
				
				case 204: bc(system, "Huzzah! Bonus round! Step on &1blue&b wool for bonus points! *seizure warning*"); break;
				
				case 206: case 207: case 208: case 209: case 210: case 211: case 213: case 214: case 215: randomChange(); break;
				
				case 216: bc(system, "Memorize the following pink square!"); break;
				
				case 217: allChange(0); break;
				
				case 218: memoryGrid = pl.utils.syncChange(system, Material.WOOL, 6); break;
				
				case 222: bc(system, "Round 5 awaits!"); break;
				
				case 223: upRound(5); break;
				
				case 226: allChange(4); break;
				
				case 227: case 228: case 229: case 230: case 231: case 232: case 233: case 234: case 235: case 236: case 237: case 238: case 239: case 240: case 241: pl.utils.yellowToAir(system, 0L, 1L, 20L); break;
				
				case 246: bc(system, "Not bad! Gather those points!"); pl.utils.usedGrids = new ArrayList<>(); break;
				
				case 247: case 248: case 249: case 250: randomChange(); break;
				
				case 252: upRound(6); break;
				
				case 255: bc(system, "Remember that grid I told you to remember? You should stand on it right about now..."); break;
				
				case 258: allChange(14); break;
				
				case 264: pl.utils.flingEntites(14, memoryGrid); break;
				
				case 267: upRound(7); break;
				
				case 272: bc(system, "Boss round! Only one square will survive!"); break;
				
				case 280: case 282: case 284: case 286: case 288: pl.utils.whiteToAir(system, 0L, 1L, 20L); pl.utils.whiteToAir(system, 0L, 1L, 20L); pl.utils.whiteToAir(system, 0L, 1L, 20L); break;
				
				case 295: bc(system, "Disco!"); pl.utils.usedGrids = new ArrayList<>(); break;
				
				case 296: case 297: case 298: case 299: case 300: randomChange(); break;
				
				case 301: upRound(8); break;
				
				case 303: bc(system, "Purple wool is a jump boost!"); break;
				
				case 305: pl.utils.syncChange(system, Material.WOOL, 10); pl.utils.syncChange(system, Material.WOOL, 10); pl.utils.syncChange(system, Material.WOOL, 10); pl.utils.syncChange(system, Material.WOOL, 10); break;
				
				case 307: case 309: case 310: case 312: pl.utils.whiteToAir(system, 0L, 1L, 20L); pl.utils.whiteToAir(system, 0L, 1L, 20L); pl.utils.whiteToAir(system, 0L, 1L, 20L); break;
				
				case 316: bc(system, "Kryptic!"); pl.utils.usedGrids = new ArrayList<>(); break;
				
				case 317: case 318: case 319: case 320: case 321: randomChange(); break;
				
				case 322: upRound(9); break;
				
				case 325: pl.utils.syncChange(system, Material.WOOL, 10); pl.utils.syncChange(system, Material.WOOL, 1); pl.utils.syncChange(system, Material.WOOL, 13); pl.utils.syncChange(system, Material.WOOL, 1); pl.utils.syncChange(system, Material.WOOL, 13); break;
				
				case 326: case 327: case 328: case 329: pl.utils.whiteToAir(system, 0L, 1L, 20L); pl.utils.whiteToAir(system, 0L, 1L, 20L); pl.utils.whiteToAir(system, 0L, 1L, 20L); break;
				
				case 335: upRound(10); break;
				
				case 338: allChange(14); break;
				
				case 339: bc(system, "Who needs yellow wool anyway?"); break;
				
				case 340: case 342: case 344: case 346: pl.utils.redToAir(system, 0L, 1L, 20L); pl.utils.redToAir(system, 0L, 1L, 20L); pl.utils.redToAir(system, 0L, 1L, 20L); break;
				
				case 350: case 351: case 352: case 553: pl.utils.usedGrids = new ArrayList<>(); randomChange(); break;
				
				case 357: upRound(11); break;
				
				case 360: 
				
				case 365: Bukkit.getServer().getScheduler().cancelTasks(pl); break;
				
				default: break;	
			}
			
			counter++;
			
		}}, 0L, 35L);
	}
	
	@SuppressWarnings("deprecation")
	public void allChange(int type){
		
		for (int x = 1; x < 17; x++){
			for (final Location l : system.getGrid(x)){
				l.getBlock().setType(Material.WOOL);
				l.getBlock().setData((byte)type);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void randomChange(){
		
		for (int x = 1; x < 17; x++){
			for (final Location l : system.getGrid(x)){
				
				if (l.getBlock().getType() != Material.WOOL){
					l.getBlock().setType(Material.WOOL);
				}
				l.getBlock().setData((byte)pl.utils.getRandom(15));
			}
		}
	}
	
	public void upRound(final int r){
		
		system = pl.avm.getAvSystem("system");
		
		for (int x = 1; x < 17; x++){
			
			for (final Location l : system.getGrid(x)){
				pl.utils.change(l, Material.WOOL, (byte) 0, 0L);
			}
		}
		
		for (int x = 1; x < 17; x++){	
			for (final Location l : system.getGrid(x)){
				for (Player p : Bukkit.getOnlinePlayers()){
					pl.utils.forceBlockLight(p, (int)l.getX(), (int)l.getY(), (int)l.getZ(), 15);
					pl.utils.queueChunk(p, (int)l.getChunk().getX(), (int)l.getChunk().getZ());
				}
			}
		}
		
		statusUpdate(system);
		pl.utils.usedGrids = new ArrayList<Integer>();
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
		public void run() { 
			
			system.setRound(r);
			pl.avm.updateAvSystem("system", system);
			allChange(0);
			
			for (String s : system.getRegisteredPlayers()){
				
				AVPlayer player = pl.avm.getAvPlayer(s);
				
				if (player.getInGame()){
					player.setFallen(false);
					player.setCombo(player.getCombo() + 1);
					player.setScore(player.getScore() + (5 * player.getCombo()));
					av(Bukkit.getPlayer(s), "&6You now have " + player.getScore() + " &6points.");
					pl.avm.updateAvPlayers(s, player);
				}
			}
			
		}}, 80L);
	}
	
	public void statusUpdate(AVSystem system){
		
		String statusList = "";
		
		bc(system, "Life Status: ");
		
		for (String s : system.getRegisteredPlayers()){
			
			AVPlayer player = pl.avm.getAvPlayer(s);
			
			if (!statusList.equals("")){
				statusList = statusList + " - " + Bukkit.getPlayer(s).getDisplayName() + ": " + player.getLives();
			} else {
				statusList = Bukkit.getPlayer(s).getDisplayName() + ": " + player.getLives();
			}

		}
		
		bc(system, statusList);
	}
}
