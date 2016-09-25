package com.evo.game.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.evo.game.box2d.FoodUserData;


public class Food extends GameActor{
	public Food(Body body) {
        super(body);
    }

	@Override
	 public FoodUserData getUserData(){
		 return (FoodUserData) userData;
	 }
}
