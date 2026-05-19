package neuralNetwork;

import activationFunction.Softmax;
import initializer.Initializer;
import initializer.XavierInitializer;

public class OutputLayer extends Layer {
    Neuron[] neurons;
    int inputSize;

    public OutputLayer(int inputSize, int outputSize, String optimizerName) {
    	this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.neurons = new Neuron[outputSize];
        this.error = new double[outputSize];
        this.output = new double[outputSize];
        Initializer init = new XavierInitializer(inputSize);

        for (int i = 0; i < outputSize; i++) {
            neurons[i] = new Neuron(inputSize, null, init, optimizerName);
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
    	double[] logits = new double[outputSize];
    	for (int i = 0; i < outputSize; i++) {
    	    logits[i] = neurons[i].computeZ(input);
    	}
    	output = Softmax.apply(logits);
    	return output;
    }
    
    public double[] computeError(double[] target) {
        for (int i = 0; i < outputSize; i++) {
            error[i] = output[i] - target[i];
        }
        return error;
    }
    
    public double computeLoss(double[] target) {
    	// cross entropy loss
        double loss = 0;
        for (int i = 0; i < output.length; i++) {
            double prediction = Math.max(output[i], 1e-15);
            loss -= target[i] * Math.log(prediction);
        }
        return loss;
    }
    
    public double[] backward(double[] prevOutput, double[] target, double learningRate) {
        computeError(target);
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
