package neuralNetwork;

import activationFunction.ActivationFunction;
import initializer.Initializer;

public class HiddenLayer extends Layer {
    Neuron[] neurons;
    int inputSize;

    public HiddenLayer(int inputSize, int outputSize, ActivationFunction activationFunction, Initializer initializer) {
    	this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.neurons = new Neuron[outputSize];
        this.error = new double[outputSize];
        this.output = new double[outputSize];

        for (int i = 0; i < outputSize; i++) {
            neurons[i] = new Neuron(inputSize, activationFunction, initializer);
        }
    }
    
    public void setNeurons(Neuron[] neurons) {
    	this.neurons = neurons;
    }
    
    public Neuron[] getNeurons() {
    	return neurons;
    }

    @Override
    public double[] forward(double[] input) {        
        for (int i = 0; i < outputSize; i++) {
            output[i] = neurons[i].activate(input);
        }
        return output;
    }
    
    public double[] computeError(double[] nextError, Neuron[] nextNeuron) {
    	for (int i = 0; i < outputSize; i++) {
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
    
    public void applyGradients(double learningRate, int batchSize) {
        for (Neuron neuron : neurons) {
            neuron.applyGradients(learningRate, batchSize);
        }
    }

}

