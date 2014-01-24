package com.github.lyokofirelyte.WCAvoidance.Internal;

public class AVPlayer {

	String name;
	int score = 0;
	int wins = 0;
	int losses = 0;
	int lives = 3;
	int task = 0;
	int combo = 0;
	Boolean inGame = false;
	Boolean fallen = false;

	public AVPlayer(String name) {
	this.name = name;
	}
	
	public void setScore(int a){
		score = a;
	}
	
	public void setCombo(int a){
		combo = a;
	}
	
	public void setTask(int a){
		task = a;
	}
	
	public void setWins(int a){
		wins = a;
	}
	
	public void setLosses(int a){
		losses = a;
	}
	
	public void setLives(int a){
		lives = a;
	}
	
	public void setInGame(Boolean a){
		inGame = a;
	}
	
	public void setFallen(Boolean a){
		fallen = a;
	}
	
	
	
	public Boolean getFallen(){
		return fallen;
	}
	
	public Boolean getInGame(){
		return inGame;
	}
	
	public int getLosses(){
		return losses;
	}
	
	public int getWins(){
		return wins;
	}
	
	public int getScore(){
		return score;
	}
	
	public int getCombo(){
		return combo;
	}
	
	public int getLives(){
		return lives;
	}
	
	public int getTask(){
		return task;
	}
}
