package neuralNetwork;

import activationFunction.ActivationFunction;
import initializer.Initializer;

public class Neuron {

    private ActivationFunction activationFunction;

    private double[] weights;
    private double bias;
    private double[] weightGradients;
    private double biasGradient;
    
    private double[] mWeights;
    private double[] vWeights;
    private double mBias;
    private double vBias;

    public Neuron(int inputSize, ActivationFunction activationFunction, Initializer initializer) {
        this.activationFunction = activationFunction;
        this.weights = new double[inputSize];
        this.weightGradients = new double[inputSize];
        this.bias = initializer.initializeBias();
        for (int i = 0; i < inputSize; i++) {
            weights[i] = initializer.initializeWeight();
        }
        mWeights = new double[inputSize];
        vWeights = new double[inputSize];
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
    
    public void applyGradients(double learningRate, int batchSize) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] -= learningRate *
                    (weightGradients[i] / batchSize);

            weightGradients[i] = 0;
        }
        bias -= learningRate *
                (biasGradient / batchSize);
        biasGradient = 0;
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