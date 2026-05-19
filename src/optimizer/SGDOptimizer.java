package optimizer;

public class SGDOptimizer implements Optimizer {
    @Override
    public void update(double[] weights, double[] gradients, double learningRate, int timestep) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] -= learningRate * gradients[i];
        }
    }

    @Override
    public double updateBias(double bias, double biasGradient, double learningRate, int timestep) {
        return bias - learningRate * biasGradient;
    }
}