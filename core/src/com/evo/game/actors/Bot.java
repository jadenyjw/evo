package com.evo.game.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.evo.game.box2d.BotUserData;


public class Bot extends GameActor{

	float velX; 
	float velY;
	
	public Bot(Body body) {
        super(body);
    }

	@Override
	 public BotUserData getUserData(){
		 return (BotUserData) userData;
	 }
	
	 public void turnRight(){
		 body.setTransform(body.getPosition(), body.getAngle() - 0.1f);	 
	 }
	 public void turnLeft(){
		 body.setTransform(body.getPosition(), body.getAngle() + 0.1f);	 
	 }
	 public void moveForward(){
		 velX = MathUtils.cos(body.getAngle()) * getUserData().getVelocity();
	     velY = MathUtils.sin(body.getAngle())* getUserData().getVelocity();
	     body.setLinearVelocity(velX, velY);
	     this.setX(body.getPosition().x);
	     this.setY(body.getPosition().y);
	 }
	 public void stop(){
		 body.setLinearVelocity(0,0);
	 }
	 
	 public void grow(float r){
		 
		 Shape shape = body.getFixtureList().first().getShape();
		 shape.setRadius(shape.getRadius() + r);
		 getUserData().afterEat();
	 }
}
