package com.evo.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.evo.game.enums.RunnerUserData;

public class WorldUtils {
	public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, false);
    }


    public static Body createBorder(World world) {
    	
    	 // Create our body definition
        BodyDef borderBodyDef = new BodyDef();
        borderBodyDef.type = BodyType.StaticBody;

        // Create a body from the defintion and add it to the world
        Body borderBody = world.createBody(borderBodyDef);
        
        EdgeShape ground = new EdgeShape();
        ground.set(0f, 0f, Constants.WORLD_WIDTH, 0 );
        borderBody.createFixture(ground, 0.0f);
        ground.dispose();
        
        EdgeShape leftWall = new EdgeShape();
        leftWall.set(0f, 0f, 0f, Constants.WORLD_HEIGHT);
        borderBody.createFixture(leftWall, 0.0f);
        leftWall.dispose();
        
        EdgeShape rightWall = new EdgeShape();
        rightWall.set(Constants.WORLD_WIDTH, 0f, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        borderBody.createFixture(rightWall, 0.0f);
        rightWall.dispose();
        
        EdgeShape roof = new EdgeShape();
        roof.set(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT,0, Constants.WORLD_HEIGHT );
        borderBody.createFixture(roof, 0.0f);
        roof.dispose();
        
        return borderBody;
        
    }
    
    public static Body createRunner(World world) {
    	
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));
        CircleShape shape = new CircleShape();
        shape.setRadius(0.2f);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.RUNNER_DENSITY);
        body.resetMassData();
        body.setAngularDamping(5);
        body.setUserData(new RunnerUserData());
        shape.dispose();
        return body;
    }
    
    public static Body createFood(World world, float x, float y){
    	
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyDef.BodyType.StaticBody;
    	bodyDef.position.set(new Vector2(x, y));
        CircleShape shape = new CircleShape();
        shape.setRadius(0.15f);
        Body foodBody = world.createBody(bodyDef);
        foodBody.createFixture(shape, Constants.RUNNER_DENSITY);
        foodBody.resetMassData();
        shape.dispose();
        
    	return foodBody;
    }

}
