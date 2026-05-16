package initializer;

public class InitializerManager {
	public static String autoChooseFromFunction(String text) {
		switch (text.substring(0, 3)) {
		case "sig":
			return "xavier";
		case "rel":
			return "he";
		default:
			return "simple";
		}
	}
	
	public static String charToString(char chr) {
		switch (chr) {
		case 'x':
			return "xavier";
		case 'h':
			return "he";
		case 's':
			return "simple";
		default:
			return "simple";
		}
	}
	
	public static Initializer stringToInit(String text, int inputSize) {
    	switch(text.toLowerCase().trim()) {
	    case "simple":
	    	return new SimpleInitializer();
	    case "he":
	    	return new HeInitializer(inputSize);
	    case "xavier":
	    	return new XavierInitializer(inputSize);
	    default:
	    	return new SimpleInitializer();
		}
    }
}
