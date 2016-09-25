package com.evo.game.enums;

import com.evo.game.box2d.UserData;

public class RunnerUserData extends UserData{

	private float velocity;
	public RunnerUserData() {
        super();
        velocity = 3f;
    }
	
	public float getVelocity() {
        return velocity;
    }
}
