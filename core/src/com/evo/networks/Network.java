package com.evo.networks;

import com.badlogic.gdx.utils.Array;

public class Network extends Array<Array<Float>> {
	
	private Array<Array<Float>> weights;

	public Network() {
	  
	}
	
	public void setWeights(Array<Float> gene){
		
		for (int x = 0; x < 25; x++){
			weights.get(0).set(x ,gene.get(x));
		}
		for (int x = 25; x < 50; x++){
			weights.get(1).set(x ,gene.get(x));
		}
		for (int x = 50; x < 64; x++){
			weights.get(2).set(x ,gene.get(x));
		}
	
	}
	
	
	
	public Array<Float> compute(Array<Float> inputs){
		
		
		return new Array<Float>(); //temp
	}


}
