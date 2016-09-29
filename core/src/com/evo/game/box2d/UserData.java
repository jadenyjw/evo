package com.evo.game.box2d;

import com.evo.game.enums.UserDataType;

public abstract class UserData {
	
	protected UserDataType userDataType;

    
    public UserData() {

    }

    public UserDataType getUserDataType() {
        return userDataType;
    }


}
