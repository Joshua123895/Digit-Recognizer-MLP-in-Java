package optimizer;

public class AdamOptimizer implements Optimizer {
    private double[] mWeights;
    private double[] vWeights;

    private double mBias;
    private double vBias;

    private static final double beta1 = 0.9;
    private static final double beta2 = 0.999;
    private static final double epsilon = 1e-8;

    public AdamOptimizer(int size) {
        mWeights = new double[size];
        vWeights = new double[size];
    }

    @Override
    public void update(double[] weights, double[] gradients, double learningRate, int timestep) {
        for (int i = 0; i < weights.length; i++) {
            mWeights[i] = beta1 * mWeights[i] + (1 - beta1) * gradients[i];
            vWeights[i] = beta2 * vWeights[i] + (1 - beta2) * gradients[i] * gradients[i];

            double mHat = mWeights[i] / (1 - Math.pow(beta1, timestep));
            double vHat = vWeights[i] / (1 - Math.pow(beta2, timestep));

            weights[i] -= learningRate * mHat / (Math.sqrt(vHat) + epsilon);
        }
    }

    @Override
    public double updateBias(double bias, double biasGradient, double learningRate, int timestep) {
        mBias = beta1 * mBias + (1 - beta1) * biasGradient;
        vBias = beta2 * vBias + (1 - beta2) * biasGradient * biasGradient;

        double mHat = mBias / (1 - Math.pow(beta1, timestep));
        double vHat = vBias / (1 - Math.pow(beta2, timestep));

        return bias - learningRate * mHat / (Math.sqrt(vHat) + epsilon);
    }
}