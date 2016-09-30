package com.evo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.evo.game.EvoGame;
import com.evo.game.utils.Constants;

public class DesktopLauncher {
    public static void main (String[] arg) {
    	//System.out.println(System.getProperty("user.dir"));
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.APP_WIDTH;
        config.height = Constants.APP_HEIGHT;
        new LwjglApplication(new EvoGame(), config);
 }
}
