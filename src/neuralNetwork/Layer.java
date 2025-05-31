package neuralNetwork;

public abstract class Layer {
    protected int outputSize;
    protected double[] error;
    protected double[] output;

    public abstract double[] forward(double[] input);
}