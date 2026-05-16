package activationFunction;

public class Softmax {
    public static double[] apply(double[] z) {
        double[] output = new double[z.length];
        double max = z[0];
        for (int i = 1; i < z.length; i++) {
            if (z[i] > max) max = z[i];
        }
        double sum = 0;
        for (int i = 0; i < z.length; i++) {
            output[i] = Math.exp(z[i] - max);
            sum += output[i];
        }
        for (int i = 0; i < z.length; i++) {
            output[i] /= sum;
        }
        return output;
    }
}