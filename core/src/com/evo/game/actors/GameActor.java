package com.evo.game.actors;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.evo.game.box2d.UserData;
public abstract class GameActor extends Actor {
	
	 public Body body;
	 public UserData userData;
	 
	    public GameActor(Body body) {
	        this.body = body;
	        this.userData = (UserData) body.getUserData();
	    }

	    public abstract UserData getUserData();
}
