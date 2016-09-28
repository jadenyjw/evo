package com.evo.game.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.evo.game.box2d.BotUserData;


public class Bot extends GameActor{

	
	public Bot(Body body) {
        super(body);
    }

	@Override
	 public BotUserData getUserData(){
		 return (BotUserData) userData;
	 }
}
