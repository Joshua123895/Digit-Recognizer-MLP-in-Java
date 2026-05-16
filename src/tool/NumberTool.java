package tool;

import java.util.Random;

public class NumberTool {
	private static Random random = new Random(System.currentTimeMillis());
	
	public static int countDigits(int a) {
		if (a == 0) return 1;
		return (int) (Math.log10(Math.abs(a))) + 1;
	}
	
	public static double random() {
		return random.nextDouble();
	}
	
	public static double randomGaussian() {
		return random.nextGaussian();
	}
}
