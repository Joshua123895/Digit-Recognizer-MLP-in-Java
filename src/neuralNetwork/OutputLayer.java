package neuralNetwork;

import activationFunction.ActivationFunction;

public class OutputLayer extends Layer {
    Neuron[] neurons;
    int inputSize;

    public OutputLayer(int inputSize, int outputSize, ActivationFunction activationFunction) {
    	this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.neurons = new Neuron[outputSize];
        this.error = new double[outputSize];
        this.output = new double[outputSize];

        for (int i = 0; i < outputSize; i++) {
            neurons[i] = new Neuron(inputSize, activationFunction);
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
    
    public double[] computeError(double[] target) {
        for (int i = 0; i < outputSize; i++) {
            error[i] = (output[i] - target[i]) * neurons[i].derivate(output[i]);
        }
        return error;
    }
    
    public double[] backward(double[] prevOutput, double[] target, double learningRate) {
        // Compute error at output layer
        computeError(target);

        // Update weights and biases
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < prevOutput.length; j++) {
                double deltaWeight = learningRate * error[i] * prevOutput[j];
                neurons[i].getWeights()[j] -= deltaWeight;  // Update weight
            }
            neurons[i].updateBias(error[i], learningRate); // Update bias
        }

        return error; // Return error for previous layer
    }

}
