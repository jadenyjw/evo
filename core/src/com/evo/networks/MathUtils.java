package com.evo.networks;

public class MathUtils {

	public float sigmoid(float x){
		
		return (float) (1 / (1 + Math.pow(Math.E, x * -1)));
		
	}
}
