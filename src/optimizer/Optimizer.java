package optimizer;

public interface Optimizer {
    void update(double[] weights, double[] gradients, double learningRate, int timestep);
    double updateBias(double bias, double biasGradient, double learningRate, int timestep);
}