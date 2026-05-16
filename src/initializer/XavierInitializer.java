package initializer;

import tool.NumberTool;

public class XavierInitializer implements Initializer {
	private final int inputSize;
	
	public XavierInitializer(int inputSize) {
		this.inputSize = inputSize;
	}
	
	@Override
	public double initializeWeight() {
		double std = Math.sqrt(1 / inputSize);
		return NumberTool.randomGaussian() * std;
	}
	@Override
	public double initializeBias() {
		return 0;
	}
}
