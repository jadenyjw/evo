package com.evo.game.box2d;

import com.evo.game.enums.UserDataType;

public class BotUserData extends UserData {
	
	private float velocity;
	private float radius;
	private int userID;
	
	
	public BotUserData() {
        super();
        velocity = 5f;
        radius = 0.2f;
        userDataType = UserDataType.BOT;
    }
	
	public float getRadius(){
		return radius;
	}
	public float getVelocity() {
        return velocity;
    }
	
	public void afterEat(){
		radius += 0.02f;
		velocity = velocity * 0.99f;
	}
	
	
	public void setID(int id){
		userID = id;
	}
	
	public int getID(){
		return userID;
	}
	
}
