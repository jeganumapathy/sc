package com.akg.scoreboard;

import java.io.Serializable;

public class Player implements Serializable {

	public static final String RIGHT_HANDED ="Right handed";
	public static final String LEFT_HANDED ="Left handed";
	public static final String SHAKE_HAND ="Shake hand";
	public static final String PEN_HOLD="Pen Hold";
	public static final String OFFENSIVE_STYLE ="Offensive Style";
	public static final String DEFENSIVE_STYLE="Defensive Style";
	
	
	
	private String name;
	private String club_name;
	private String handness = RIGHT_HANDED;
	private String style = SHAKE_HAND;
	private String grip = OFFENSIVE_STYLE;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClub_name() {
		return club_name;
	}

	public void setClub_name(String club_name) {
		this.club_name = club_name;
	}

	public String getHandness() {
		return handness;
	}

	public void setHandness(String handness) {
		this.handness = handness;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getGrip() {
		return grip;
	}

	public void setGrip(String grip) {
		this.grip = grip;
	}

}
