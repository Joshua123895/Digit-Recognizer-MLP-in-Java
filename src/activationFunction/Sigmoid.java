package activationFunction;

public class Sigmoid implements ActivationFunction {
    @Override
    public double activate(double input) {
        return 1.0 / (1.0 + Math.exp(-input));
    }
    
    @Override
    public double derivative(double input) {
    	double func = activate(input);
    	return func * (1 - func);
    }
}