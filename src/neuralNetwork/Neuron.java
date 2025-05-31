package neuralNetwork;

import activationFunction.ActivationFunction;
import tool.NumberTool;

public class Neuron {
    private ActivationFunction activationFunction;
    private double[] weights;
    private double bias;

    public Neuron(int inputSize, ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        this.weights = new double[inputSize];
        this.bias = NumberTool.random() * 0.2 - 0.1;
        // Initialize weights
        for (int i = 0; i < inputSize; i++) {
            weights[i] = NumberTool.random() * 0.2 - 0.1;
        }
    }

    public double activate(double[] input) {
        double z = computeZ(input);
        return activationFunction.activate(z);
    }
    
    public double derivate(double input) {
    	return activationFunction.derivative(input);
    }

    public double computeZ(double[] input) {
        double sum = 0.0;
        for (int i = 0; i < input.length; i++) {
            sum += weights[i] * input[i];
        }
        return sum + bias;
    }

    // Getters for weights and bias (required for back propagation)
    public double[] getWeights() {
    	return weights; 
    }
    
    public double getBias() {
    	return bias;
    }
    
    public void setWeights(double[] weights) {
    	this.weights = weights;
    }
    
    public void setBias(double bias) {
    	this.bias = bias;
    }

    // Methods to update weights and bias during training
    public void updateWeights(double[] result, double[] error, double learningRate) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] -= result[i] * error[i] * learningRate;
        }
    }

    public void updateBias(double error, double learningRate) {
        bias -= error * learningRate;
    }
}
