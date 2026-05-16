package initializer;

import tool.NumberTool;

public class SimpleInitializer implements Initializer {
	@Override
	public double initializeWeight() {
		return NumberTool.random() * 0.2 - 0.1;
	}
	@Override
	public double initializeBias() {
		return NumberTool.random() * 0.2 - 0.1;
	}
}
