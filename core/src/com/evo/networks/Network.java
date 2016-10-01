package com.evo.networks;

import com.badlogic.gdx.utils.Array;
import com.evo.networks.MathUtils;
public class Network extends Array<Array<Float>> {
	
	private Array<Array<Float>> weights = new Array<Array<Float>>();

	public Network() {
	  
	}
	
	public void setWeights(Array<Float> gene){
		System.out.println(gene.size);
		Array<Float> tempArray = new Array<Float>();
		for (int x = 0; x < 25; x++){
			
			tempArray.add(gene.get(x));
	
		}
		weights.add(tempArray);
		tempArray.clear();
		
		for (int x = 25; x < 50; x++){
			tempArray.add(gene.get(x));
		}
		weights.add(tempArray);
		tempArray.clear();
		
		for (int x = 50; x < 64; x++){
			tempArray.add(gene.get(x));
		}
		weights.add(tempArray);
		tempArray.clear();
	
	}
	
	
	
	public Array<Float> compute(Array<Float> inputs){
		
	
	
		
		
		
		
		
		return new Array<Float>(); //temp
	}


}
