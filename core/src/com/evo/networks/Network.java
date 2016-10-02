package com.evo.networks;

import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

import com.badlogic.gdx.utils.Array;

public class Network extends BasicNetwork {

	BasicNetwork network = new BasicNetwork();

	public Network() {

		this.addLayer(new BasicLayer(new ActivationTANH(), false, 8));
		this.addLayer(new BasicLayer(new ActivationTANH(), false, 4));
		this.addLayer(new BasicLayer(new ActivationTANH(), false, 4));
		this.addLayer(new BasicLayer(new ActivationTANH(), false, 4));
		this.getStructure().finalizeStructure();

	}

	public void setWeights(Array<Float> gene) {
		int count = 0;
		
		for (int x = 0; x < this.getLayerCount() - 1; x ++){
			for (int a = 0; a < this.getLayerNeuronCount(x); a++){
				for (int b = 0; b < this.getLayerNeuronCount(x + 1); b++){
					this.setWeight(x, a, b, gene.get(count));
					count++;
					//System.out.println(this.getWeight(x, a, b));
				}
			}
		}
		
		

	}

}
