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
        ground.set(0, 0, Constants.APP_WIDTH, 0 );
        borderBody.createFixture(ground, 0.0f);
        ground.dispose();
        
        EdgeShape leftWall = new EdgeShape();
        leftWall.set(0, 0, 0, Constants.APP_HEIGHT);
        borderBody.createFixture(leftWall, 0.0f);
        leftWall.dispose();
        
        EdgeShape rightWall = new EdgeShape();
        
        rightWall.set(Constants.APP_WIDTH, 0, 0, 0);
        borderBody.createFixture(rightWall, 0.0f);
        rightWall.dispose();
        
        
        EdgeShape roof = new EdgeShape();
        roof.set(Constants.APP_WIDTH, Constants.APP_HEIGHT,0, Constants.APP_HEIGHT );
        borderBody.createFixture(roof, 0.0f);
        roof.dispose();
        
        return borderBody;
        
    }
    
    public static Body createRunner(World world) {
    	
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, Constants.RUNNER_DENSITY);
        body.resetMassData();
        body.setAngularDamping(5);
        shape.dispose();
        return body;
    }

}
