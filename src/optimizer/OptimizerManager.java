package optimizer;

public class OptimizerManager {
	public static String charToString(char chr) {
		switch (chr) {
		case 'a':
			return "adam";
		case 's':
			return "sgd";
		default:
			return "sgd";
		}
	}
	
	public static Optimizer stringToFunction(String text, int size) {
    	switch(text.toLowerCase().trim()) {
	    case "adam":
			return new AdamOptimizer(size);
		case "sgd":
			return new SGDOptimizer();
		default:
			return new SGDOptimizer();
		}
    }
}