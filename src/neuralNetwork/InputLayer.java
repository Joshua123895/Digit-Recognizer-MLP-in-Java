package neuralNetwork;

public class InputLayer extends Layer {
    public InputLayer(int size) {
        this.outputSize = size;
        output = new double[outputSize];
    }

    @Override
    public double[] forward(double[] input, double dropoutRate) {
    	output = input.clone();
        return output;
    }
}
