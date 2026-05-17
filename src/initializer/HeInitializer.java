package initializer;

import tool.NumberTool;

public class HeInitializer implements Initializer {
	private final int inputSize;
	
	public HeInitializer(int inputSize) {
		this.inputSize = inputSize;
	}
	
	@Override
	public double initializeWeight() {
		double std = Math.sqrt(2.0 / inputSize);
		return NumberTool.randomGaussian() * std;
	}
	@Override
	public double initializeBias() {
		return 0;
	}
}
