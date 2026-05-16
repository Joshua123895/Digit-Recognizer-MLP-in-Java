package activationFunction;

public class LeakyReLu implements ActivationFunction {
	double leak = 0.01;
	@Override
	public double activate(double input) {
		return Math.max(input, leak*input);
	}

	@Override
	public double derivative(double input) {
		return (input < 0)? leak : 1.0;
	}

}
