package neuralNetwork;

import activationFunction.ActivationFunction;
import initializer.Initializer;
import optimizer.Optimizer;
import optimizer.OptimizerManager;

public class Neuron {
    private ActivationFunction activationFunction;
    private Optimizer optimizer;

    private double[] weights;
    private double bias;
    private double[] weightGradients;
    private double biasGradient;    

    public Neuron(int inputSize, ActivationFunction activationFunction, Initializer initializer, String optimizerName) {
        this.activationFunction = activationFunction;
        this.optimizer = OptimizerManager.stringToFunction(optimizerName, inputSize);
        this.weights = new double[inputSize];
        this.weightGradients = new double[inputSize];
        this.bias = initializer.initializeBias();
        for (int i = 0; i < inputSize; i++) {
            weights[i] = initializer.initializeWeight();
        }
    }

    public double activate(double[] input) {
        double z = computeZ(input);
        if (activationFunction == null) {
        	return z;
        }
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
    
    public void accumulateGradients(double[] prevOutput, double error) {
        for (int i = 0; i < weights.length; i++) {
            weightGradients[i] += error * prevOutput[i];
        }
        biasGradient += error;
    }
    
    private void normalizeGradients(int batchSize) {
        for (int i = 0; i < weightGradients.length; i++) {
            weightGradients[i] /= batchSize;
        }
        biasGradient /= batchSize;
    }
    
    public void applyGradients(double learningRate, int timestep, int batchSize) {
    	normalizeGradients(batchSize);
        optimizer.update(weights, weightGradients, learningRate, timestep);
        bias = optimizer.updateBias(bias, biasGradient, learningRate, timestep);
//        resetGradients(); // already done in the layers
    }
    
    public void resetGradients() {
        for (int i = 0; i < weightGradients.length; i++) {
            weightGradients[i] = 0.0;
        }
        biasGradient = 0.0;
    }

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
}