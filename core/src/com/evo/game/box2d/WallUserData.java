package com.evo.game.box2d;

import com.evo.game.enums.UserDataType;

public class WallUserData extends UserData{
	public WallUserData() {
        super();
        userDataType = UserDataType.WALL;
}

}
