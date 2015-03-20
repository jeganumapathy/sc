package com.akg.scoreboard;

import java.io.Serializable;
import java.util.ArrayList;

public class GameData implements Serializable {
	public ArrayList<Object> forced_error;
	public ArrayList<Object> unforced_error;
	public ArrayList<Object> winner;

	public GameData() {
		forced_error = new ArrayList<Object>();
		unforced_error = new ArrayList<Object>();
		winner = new ArrayList<Object>();
	}
}
