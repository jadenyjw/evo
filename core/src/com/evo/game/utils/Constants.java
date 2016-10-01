package com.evo.game.utils;

import com.badlogic.gdx.math.Vector2;

public class Constants {
	public static final int APP_WIDTH = 1366;
    public static final int APP_HEIGHT = 768;
    
    public static final float WORLD_WIDTH = 30f;
    public static final float WORLD_HEIGHT = 30f;
    
    public static final Vector2 WORLD_GRAVITY = new Vector2(0, 0);
   

    public static final float GROUND_Y = 0;

    public static final float GROUND_HEIGHT = 2f;
    public static final float SPAWN_RADIUS = 10f;
    public static final float RUNNER_X = 2;
    public static final float RUNNER_Y = GROUND_Y + GROUND_HEIGHT;
    public static final float RUNNER_WIDTH = 1f;
    public static final float RUNNER_HEIGHT = 2f;
    public static float RUNNER_DENSITY = 0.5f;
}
