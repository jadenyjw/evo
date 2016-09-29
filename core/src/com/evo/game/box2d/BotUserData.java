package com.evo.game.box2d;

import com.evo.game.enums.UserDataType;

public class BotUserData extends UserData {
	
	private float velocity;
	private float radius;
	private int id;
	
	private float distanceToNearestPlayer;
	private float angleToNearestPlayer;
	
	// In the actual Neural Network, this will be a binary input, whether it is bigger or not.
	private float sizeOfNearestPlayer;
	
	private float distanceToNearestFood;
	private float angleToNearestFood;
	
	
	
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
	public void setID(int i){
		id = i;
	}
	public int getID(){
		return id;
	}
	
	//Neural Network Inputs

	public float getDistanceToNearestPlayer(){
		return distanceToNearestPlayer;
	}
	public float getAngleToNearestPlayer(){
			return angleToNearestPlayer;
	}
	public float getSizeOfNearestPlayer(){
		return sizeOfNearestPlayer;
	}
	public float getDistanceToNearestFood(){
		return distanceToNearestFood;
	}
	public float getAngleToNearestFood(){
			return angleToNearestFood;
	}
	
	
	public void setDistanceToNearestPlayer(float f){
		distanceToNearestPlayer = f;
	}
	public void setAngleToNearestPlayer(float f){
			 angleToNearestPlayer = f;
	}
	public void setSizeOfNearestPlayer(float f){
		sizeOfNearestPlayer = f;
	}
	public void setDistanceToNearestFood(float f){
		 distanceToNearestFood = f;
	}
	public void setAngleToNearestFood(float f){
			 angleToNearestFood = f;
	}
	
	
}
