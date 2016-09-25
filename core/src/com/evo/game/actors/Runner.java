package com.evo.game.actors;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.evo.game.enums.RunnerUserData;
public class Runner extends GameActor{
	float velocity = 3f; // Your desired velocity of the car.

	float velX; 
	float velY; 

	
	 public Runner(Body body) {
	        super(body);

	    }
	 
	 @Override
	 public RunnerUserData getUserData(){
		 return (RunnerUserData) userData;
	 }
	 
	 public void turnRight(){
		 body.setTransform(body.getPosition(), body.getAngle() - 0.1f);	 
	 }
	 public void turnLeft(){
		 body.setTransform(body.getPosition(), body.getAngle() + 0.1f);	 
	 }
	 public void moveForward(){
		 velX = MathUtils.cos(body.getAngle()) * velocity;
	     velY = MathUtils.sin(body.getAngle())* velocity;
	     body.setLinearVelocity(velX, velY);
	 }
	 
	 public void stop(){
		 body.setLinearVelocity(0,0);
	 }
}
