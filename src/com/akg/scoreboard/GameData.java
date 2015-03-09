package com.akg.scoreboard;

import java.util.ArrayList;

public class GameData {
	public ArrayList<Long> forced_error;
	public ArrayList<Long> unforced_error;
	public ArrayList<Long> winner;

	public GameData() {
		forced_error = new ArrayList<Long>();
		unforced_error = new ArrayList<Long>();
		winner = new ArrayList<Long>();
	}
}
