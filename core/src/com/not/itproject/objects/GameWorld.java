package com.not.itproject.objects;

import com.not.itproject.zero.ProjectZero;

public class GameWorld {
	// declare variables
	ProjectZero game;
	Player player;
	
	// main constructor
	public GameWorld(ProjectZero game) {
		// define game
		this.game = game;
		
		// define player
//		player = new Player(game.GAME_WIDTH/2)
		
	}
	
	public Player getPlayer() {
		return player;
	}
}
