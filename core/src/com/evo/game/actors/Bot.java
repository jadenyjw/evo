package com.evo.game.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.evo.game.box2d.BotUserData;
import com.evo.genetics.Gene;
import com.evo.networks.Network;


public class Bot extends GameActor{

	private float velX; 
	private float velY;
	public Gene gene = new Gene();
	public Network network = new Network();
	
	public Bot(Body body, Gene gene, Network network) {
        super(body);
        this.network = network;
        this.gene = gene;
    }
	
	public void setGene(Gene gene){
		this.gene = gene;
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
		 
		 getUserData().afterEat();
		 Shape shape = body.getFixtureList().first().getShape();
		 shape.setRadius(shape.getRadius() + r);
		 
	 }
}
