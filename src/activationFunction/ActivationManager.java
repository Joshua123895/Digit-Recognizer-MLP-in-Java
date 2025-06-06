package activationFunction;

public class ActivationManager {
	public static String charToString(char chr) {
		switch (chr) {
		case 's':
			return "sigmoid";
		case 'r':
			return "relu";
		default:
			return "sigmoid";
		}
	}
	
	public static ActivationFunction stringToFunction(String text) {
    	switch(text.toLowerCase().trim()) {
	    case "sigmoid":
			return new Sigmoid();
		case "relu":
			return new ReLu();
		default:
			return new Sigmoid();
		}
    }
}
