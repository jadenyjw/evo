package com.evo.game;

import com.badlogic.gdx.Game;
import com.evo.game.screens.GameScreen;
import com.evo.game.screens.MainMenuScreen;

public class EvoGame extends Game {
	
    @Override
	public void create () {
    	setScreen(new MainMenuScreen());
	}

	

	
}
