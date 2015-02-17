package com.akg.scoreboard;

import java.io.Serializable;

public class Match implements Serializable {

	private int event;
	private String playerA;
	private String playerB;
	private String playerX;
	private String playerY;
	private int set;
	
	private Player a;
	private Player b;
	private Player x;
	private Player y;
	
	public Match() {
		this.a = new  Player();
		this.b = new  Player();
		this.x = new  Player();
		this.y = new  Player();
	}
	
	public Player getA() {
		return a;
	}
	public void setA(Player a) {
		this.a = a;
	}
	public Player getB() {
		return b;
	}
	public void setB(Player b) {
		this.b = b;
	}
	public Player getX() {
		return x;
	}
	public void setX(Player x) {
		this.x = x;
	}
	public Player getY() {
		return y;
	}
	public void setY(Player y) {
		this.y = y;
	}
	public int getEvent() {
		return event;
	}
	public void setEvent(int event) {
		this.event = event;
	}
	public String getPlayerA() {
		return playerA;
	}
	public boolean setPlayerA(String playerA) {
		boolean val = checkPlayerName(playerA);
		if (val)
			this.playerA = playerA;
		a.setName(playerA);
		return val;
	}
	public String getPlayerB() {
		return playerB;
	}
	public boolean setPlayerB(String playerB) {
		boolean val = checkPlayerName(playerB);
		if (val)
			this.playerB = playerB;
		b.setName(playerB);
		return val;
	}
	public String getPlayerX() {
		return playerX;
	}
	public boolean setPlayerX(String playerX) {
		boolean val = checkPlayerName(playerX);
		if (val)
			this.playerX = playerX;
		x.setName(playerX);
		return val;
	}
	public String getPlayerY() {
		return playerY;
	}
	public boolean setPlayerY(String playerY) {
		boolean val = checkPlayerName(playerY);
		if (val)
			this.playerY = playerY;
		y.setName(playerY);
		return val;
	}

	public boolean checkPlayerName(String data) {
		return data != null && data.trim().length() > 0;
	}
	public int getSet() {
		return set;
	}
	public void setSet(int set) {
		this.set = set;
	}
}
