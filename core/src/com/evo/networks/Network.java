package com.evo.networks;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

import com.badlogic.gdx.utils.Array;

public class Network extends BasicNetwork {

	BasicNetwork network = new BasicNetwork();

	public Network() {

		this.addLayer(new BasicLayer(new ActivationSigmoid(), false, 5));
		this.addLayer(new BasicLayer(new ActivationSigmoid(), false, 5));
		this.addLayer(new BasicLayer(new ActivationSigmoid(), false, 5));
		this.addLayer(new BasicLayer(new ActivationSigmoid(), false, 3));
		this.getStructure().finalizeStructure();

	}

	public void setWeights(Array<Float> gene) {
		int count = 0;
		
		  for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 5; b++) {
				
					this.setWeight(0, a, b, gene.get(count));
					count++;
					
				}
			}
		
		
		for (int a = 0; a < 5; a++) {
			
			  for (int b = 0; b < 5; b++) {
				
					this.setWeight(1, a, b, gene.get(count));
					count++;
					
				}
			}

		
		
		for (int a = 0; a < 5; a++) {
			
			  for (int b = 0; b < 3; b++) {
				
					this.setWeight(2, a, b, gene.get(count));
					count++;
				
				}
			}

		

	}

	public int getAnswer(Array<Float> inputs) {
		
        this.compute((MLData) inputs);
		
		return 1; // temp
	}

}
