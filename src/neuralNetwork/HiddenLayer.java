package neuralNetwork;

import activationFunction.ActivationFunction;
import initializer.Initializer;
import tool.NumberTool;

public class HiddenLayer extends Layer {
    Neuron[] neurons;
    int inputSize;
    boolean[] dropped;

    public HiddenLayer(int inputSize, int outputSize, ActivationFunction activationFunction, Initializer initializer, String optimizerName) {
    	this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.neurons = new Neuron[outputSize];
        this.error = new double[outputSize];
        this.output = new double[outputSize];
        this.dropped = new boolean[outputSize];
        
        for (int i = 0; i < outputSize; i++) {
            neurons[i] = new Neuron(inputSize, activationFunction, initializer, optimizerName);
        }
    }
    
    public void setNeurons(Neuron[] neurons) {
    	this.neurons = neurons;
    }
    
    public Neuron[] getNeurons() {
    	return neurons;
    }

    @Override
    public double[] forward(double[] input, double dropoutRate) {        
        for (int i = 0; i < outputSize; i++) {
            output[i] = neurons[i].activate(input);
            if (NumberTool.random() < dropoutRate) {
                output[i] = 0;
                dropped[i] = true;
            } else {
                dropped[i] = false;
                output[i] /= (1.0 - dropoutRate);
            }
        }
        return output;
    }
    
    public double[] computeError(double[] nextError, Neuron[] nextNeuron) {
    	for (int i = 0; i < outputSize; i++) {
    	    if (dropped[i]) {
    	        error[i] = 0;
    	        continue;
    	    }
            double sum = 0.0;
            for (int j = 0; j < nextError.length; j++) {
                sum += nextError[j] * nextNeuron[j].getWeights()[i];
            }
            error[i] = sum * neurons[i].derivate(output[i]);
        }

        return error;
    }
    
    public double[] backward(double[] prevOutput, double[] nextError, Neuron[] nextNeuron, double learningRate) {
		computeError(nextError, nextNeuron);
		for (int i = 0; i < outputSize; i++) {
			neurons[i].accumulateGradients(prevOutput, error[i]);
		}
		return error;
	}
    
    public void applyGradients(double learningRate, int timestep, int batchSize) {
        for (Neuron neuron : neurons) {
            neuron.applyGradients(learningRate, timestep, batchSize);
        }
    }
    
    public void resetGradients() {
    	for (Neuron n : neurons) {
    		n.resetGradients();
    	}
    }
}

