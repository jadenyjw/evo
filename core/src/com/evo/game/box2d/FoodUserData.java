package com.evo.game.box2d;

import com.evo.game.enums.UserDataType;

public class FoodUserData extends UserData{
	
	private boolean deleted = false;
	public FoodUserData() {
        super();
        userDataType = UserDataType.FOOD;
    }
	
	public boolean isDeleted(){
		return deleted;
	}
	public void delete(){
		deleted = true;
	}
	
}
