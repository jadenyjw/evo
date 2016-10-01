package com.evo.game.box2d;

import com.evo.game.enums.UserDataType;

public class RunnerUserData extends UserData{

	private float velocity;
	//private float radius;
	
	
	
	public RunnerUserData() {
        super();
        
        velocity = 5f;
        //radius = 0.2f;
        userDataType = UserDataType.RUNNER;
    }
	
	public float getVelocity() {
        return velocity;
    }
	/*
	public float getRadius(){
		return radius;
	}
	*/
	
	public void afterEat(){
		//radius += 0.02f;
		velocity = velocity * 0.98f;
	}
	
	
	
		
	}
