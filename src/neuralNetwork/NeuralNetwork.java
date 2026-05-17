package neuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;

import activationFunction.ActivationFunction;
import activationFunction.ActivationManager;
import initializer.Initializer;
import initializer.InitializerManager;
import menu.TrainMenu;

public class NeuralNetwork {
    private ArrayList<Layer> layers;
    private ArrayList<Integer> numHiddenLayers = new ArrayList<>(Arrays.asList(128));
    private ArrayList<String> hiddenActivations = new ArrayList<>(Arrays.asList("sigmoid"));
    private ArrayList<String> hiddenInitializers = new ArrayList<>(Arrays.asList("xavier"));
    private double initialLearningRate = 0.01;
    private double learningRate = initialLearningRate;
    private int epoch = 0;	
    private int timestep = 0;
    
    public NeuralNetwork() {
    	reconstruct();
    }
    
    public void reconstruct() {
    	if (numHiddenLayers.size() != hiddenActivations.size()) {
    		System.out.println("numHiddenLayers.length != hiddenActivations.length");
    		System.out.println("numHiddenLayers: " + numHiddenLayers.size());
    		for (int i : numHiddenLayers) {
    			System.out.println(i);
    		}
    		System.out.println("hiddenActivations: " + hiddenActivations.size());
    		for (String i : hiddenActivations) {
    			System.out.println(i);
    		}
    		System.exit(1);
    	}
    	if (hiddenActivations.size() != hiddenInitializers.size()) {

    		System.out.println("hiddenInitializers.length != hiddenActivations.length");
    		System.out.println("hiddenInitializers: " + hiddenInitializers.size());
    		for (String i : hiddenInitializers) {
    			System.out.println(i);
    		}
    		System.out.println("hiddenActivations: " + hiddenActivations.size());
    		for (String i : hiddenActivations) {
    			System.out.println(i);
    		}
    		System.exit(1);
    	}
        layers = new ArrayList<>();
		layers.add(new InputLayer(784));
		int prev = 784;
    	for (int i = 0; i < numHiddenLayers.size(); i++) {
    		int num = numHiddenLayers.get(i);
    		ActivationFunction func = ActivationManager.stringToFunction(hiddenActivations.get(i));
    		Initializer init = InitializerManager.stringToInit(hiddenInitializers.get(i), (i == 0)? 784 : numHiddenLayers.get(i-1));
    		layers.add(new HiddenLayer(prev, num, func, init));
    		prev = num;
    	}
		layers.add(new OutputLayer(prev, 10));
    }
    
    public double[] feedForward(double[] input) {
        double[] currentInput = input;

        for (Layer layer : layers) {
            currentInput = layer.forward(currentInput);
        }
        
        return currentInput;
    }
    
    public void backpropagate(double[] result) {
        this.timestep++;
    	// assume MLS has minimum 1 hidden layer
    	int layerSize = layers.size();
    	
        OutputLayer outputLayer = (OutputLayer) layers.get(layerSize - 1);

        // hidden-output
        double[] nextError = outputLayer.backward(layers.get(layerSize - 2).output, result, learningRate);
        
        // hidden-hidden-output
        HiddenLayer hiddenLayer = (HiddenLayer) layers.get(layerSize - 2);
        nextError = hiddenLayer.backward(layers.get(layerSize - 3).output, nextError, outputLayer.neurons, learningRate);
        
        // hidden-hidden
        for (int i = layers.size() - 3; i > 0; i--) {
            hiddenLayer = (HiddenLayer) layers.get(i);
            HiddenLayer nextLayer = (HiddenLayer) layers.get(i + 1);
            
            nextError = hiddenLayer.backward(layers.get(i - 1).output, nextError, nextLayer.neurons, learningRate);
        }
        
    }
    
    public void applyBatchGradients(int batchSize) {
        for (Layer layer : layers) {
            if (layer instanceof HiddenLayer) {
                ((HiddenLayer) layer).applyGradients(learningRate, batchSize);
            } else if (layer instanceof OutputLayer) {
                ((OutputLayer) layer).applyGradients(learningRate, batchSize);
            }
        }
    }
    
    public Layer getLayer(int index) {
    	return layers.get(index);
    }
    
    public void setLayer(int index, Layer layer) {
    	layers.set(index, layer);
    }

	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}

	public void nextEpoch() {
		this.epoch++;
	}

	public int getEpoch() {
		return epoch;
	}
    
    public void nextLearningate() {
    	this.learningRate = initialLearningRate * Math.exp(-TrainMenu.getDecayRate() * this.epoch);
    }

	public double getLearningRate() {
		return learningRate;
	}
	
	public void setInitialLearningRate(double lr) {
		this.initialLearningRate = lr;
	}

	public double getInitialLearningRate() {
		return initialLearningRate;
	}

	public ArrayList<Integer> getNumHiddenLayers() {
		return numHiddenLayers;
	}
	
	public void resetNumHiddenLayers() {
		numHiddenLayers = new ArrayList<>();
	}
	
	public void addNumHiddenLayers(int element) {
		numHiddenLayers.add(element);
	}
	
	public void addNumHiddenLayers(int index, int element) {
		numHiddenLayers.add(index, element);
	}
	
	public void setNumHiddenLayers(int index, int element) {
		numHiddenLayers.set(index, element);
	}
	
	public void removeNumHiddenLayers(int index) {
		numHiddenLayers.remove(index);
	}

	public ArrayList<String> getHiddenActivations() {
		return hiddenActivations;
	}
	
	public void resetHiddenActivations() {
		hiddenActivations = new ArrayList<>(Arrays.asList());
	}
	
	public void addHiddenActivations(String element) {
		hiddenActivations.add(element);
	}
	
	public void addHiddenActivations(int index, String element) {
		hiddenActivations.add(index, element);
	}
	
	public void addHiddenActivations(String[] elements) {
		for (String element : elements) {
			addHiddenActivations(element);
		}
	}

	public void setHiddenActivations(int index, String element) {
		hiddenActivations.set(index, element);
	}
	
	public void removeHiddenActivations(int index) {
		hiddenActivations.remove(index);
	}

	public ArrayList<String> getHiddenInitializers() {
		return hiddenInitializers;
	}
	
	public void resetHiddenInitializers() {
		hiddenInitializers = new ArrayList<>(Arrays.asList());
	}
	
	public void addHiddenInitializers(String element) {
		hiddenInitializers.add(element);
	}
	
	public void addHiddenInitializers(int index, String element) {
		hiddenInitializers.add(index, element);
	}
	
	public void addHiddenInitializers(String[] elements) {
		for (String element : elements) {
			addHiddenInitializers(element);
		}
	}

	public void setHiddenInitializers(int index, String element) {
		hiddenInitializers.set(index, element);
	}
	
	public void removeHiddenInitializers(int index) {
		hiddenInitializers.remove(index);
	}
	
	public double computeLoss(double[] label) {
		return ((OutputLayer) layers.get(layers.size()-1)).computeLoss(label);
	}
}
