package com.evo.game.box2d;

import com.evo.game.enums.UserDataType;

public class FoodUserData extends UserData{
	private int id;
	
	public FoodUserData() {
        super();
        userDataType = UserDataType.FOOD;
}
	public void setID(int i) {
		id = i;
	}

	public int getID() {
		return id;
	}
}