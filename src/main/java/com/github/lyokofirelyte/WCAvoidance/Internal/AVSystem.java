package com.github.lyokofirelyte.WCAvoidance.Internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class AVSystem {

	String name;
	Boolean gameStarted = false;
	Boolean arenaSet = false;
	int round = 0;
	int playersLeft = 0;
	List<String> registeredPlayers;
	List<String> grids;
	List<Location> grid1 = new ArrayList<>();
	List<Location> grid2 = new ArrayList<>();
	List<Location> grid3 = new ArrayList<>();
	List<Location> grid4 = new ArrayList<>();
	List<Location> grid5 = new ArrayList<>();
	List<Location> grid6 = new ArrayList<>();
	List<Location> grid7 = new ArrayList<>();
	List<Location> grid8 = new ArrayList<>();
	List<Location> grid9 = new ArrayList<>();
	List<Location> grid10 = new ArrayList<>();
	List<Location> grid11 = new ArrayList<>();
	List<Location> grid12 = new ArrayList<>();
	List<Location> grid13 = new ArrayList<>();
	List<Location> grid14 = new ArrayList<>();
	List<Location> grid15 = new ArrayList<>();
	List<Location> grid16 = new ArrayList<>();

	public AVSystem(String name){
	this.name = name;
	}
	
	public void setRound(int a){
		round = a;
	}
	
	public void setPlayersLeft(int a){
		playersLeft = a;
	}
	
	public void setRegisteredPlayers(List<String> a){
		registeredPlayers = a;
	}
	
	public void setGameStarted(Boolean a){
		gameStarted = a;
	}
	
	public void setArenaSet(Boolean a){
		arenaSet = a;
	}
	
	public void setGrids(List<String> a){
		grids = a;
	}
	
	public List<String> getGrids(){
		return grids;
	}
	
	public void setGrid(List<Location> a, int b){
		
		switch (b){
			case 1: grid1 = a; break;
			case 2: grid2 = a; break;
			case 3: grid3 = a; break;
			case 4: grid4 = a; break;
			case 5: grid5 = a; break;
			case 6: grid6 = a; break;
			case 7: grid7 = a; break;
			case 8: grid8 = a; break;
			case 9: grid9 = a; break;
			case 10: grid10 = a; break;
			case 11: grid11 = a; break;
			case 12: grid12 = a; break;
			case 13: grid13 = a; break;
			case 14: grid14 = a; break;
			case 15: grid15 = a; break;
			case 16: grid16 = a; break;
		}
	}
	
	public List<Location> getGrid(int b){
		
		switch (b){
			case 1: return grid1;
			case 2: return grid2;
			case 3: return grid3;
			case 4: return grid4;
			case 5: return grid5;
			case 6: return grid6;
			case 7: return grid7;
			case 8: return grid8;
			case 9: return grid9;
			case 10: return grid10;
			case 11: return grid11;
			case 12: return grid12;
			case 13: return grid13;
			case 14: return grid14;
			case 15: return grid15;
			case 16: return grid16;
		}
		return null;
	}
	
	public Boolean getGameStarted(){
		return gameStarted;
	}
	
	public Boolean getArenaSet(){
		return arenaSet;
	}
	
	public int getPlayersLeft(){
		return playersLeft;
	}
	
	public List<String> getRegisteredPlayers(){
		return registeredPlayers;
	}
	
	public int getRound(){
		return round;
	}
}
