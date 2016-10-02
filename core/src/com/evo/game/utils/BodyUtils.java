package com.evo.game.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.evo.game.box2d.UserData;
import com.evo.game.enums.UserDataType;

public class BodyUtils {
	public static boolean bodyIsRunner(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.RUNNER;
    }

    public static boolean bodyIsFood(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.FOOD;
    }
    
    public static boolean bodyIsBot(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.BOT;
    }
    
    public static boolean bodyIsWall(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.WALL;
    }
    
   
}
