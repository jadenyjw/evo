package com.evo.game.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.evo.game.box2d.WallUserData;

public class Wall extends GameActor {
	public Wall(Body body) {
        super(body);
    }
	
	@Override
	 public WallUserData getUserData(){
		 return (WallUserData) userData;
	 }
}
