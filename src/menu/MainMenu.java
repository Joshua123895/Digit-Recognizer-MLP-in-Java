package menu;

import neuralNetwork.NeuralNetwork;
import tool.ConsoleHandler;
import tool.InputHandler;
import tool.MNISTManager;

public class MainMenu {
	InputHandler scan = new InputHandler();
    
    NeuralNetwork network = new NeuralNetwork();
    MNISTManager trainData = new MNISTManager("train-labels.idx1-ubyte", "train-images.idx3-ubyte");
    MNISTManager testData = new MNISTManager("t10k-labels.idx1-ubyte", "t10k-images.idx3-ubyte");
    
    FileMenu file = new FileMenu(network, scan);
    ViewEditMenu viewer = new ViewEditMenu(network, scan);
    TrainMenu trainer = new TrainMenu(network, trainData, scan);
    TestMenu tester = new TestMenu(network, testData, scan);
	
	void printTitle() {
		System.out.println(
				  "    ____  _       _ __     ____                              _                \r\n"
				+ "   / __ \\(_)___ _(_) /_   / __ \\___  _________  ____ _____  (_)___  ___  _____\r\n"
				+ "  / / / / / __ `/ / __/  / /_/ / _ \\/ ___/ __ \\/ __ `/ __ \\/ /_  / / _ \\/ ___/\r\n"
				+ " / /_/ / / /_/ / / /_   / _, _/  __/ /__/ /_/ / /_/ / / / / / / /_/  __/ /    \r\n"
				+ "/_____/_/\\__, /_/\\__/  /_/ |_|\\___/\\___/\\____/\\__, /_/ /_/_/ /___/\\___/_/     \r\n"
				+ "        /____/                               /____/                           ");
	}
	
	void printMenu() {
		System.out.println("-1. Exit");
		System.out.println(" 0. Reset progress");
		System.out.println(" 1. View Layer");
		System.out.println(" 2. Edit Layer");
		System.out.println(" 3. Load .dat File");
		System.out.println(" 4. Save As .dat File");
		System.out.println(" 5. Train");
		System.out.println(" 6. Test");
		System.out.println(" 7. Draw and Test");
		System.out.println(" 8. Settings");
		System.out.println(" 9. Help");
	}
	
	void reset() {
		String validate = scan.stringInput("Are you sure[yes/no]? ");
		if (validate.equalsIgnoreCase("yes")) {
			network.setEpoch(0);
			network.resetNumHiddenLayers();
			network.addNumHiddenLayers(128);
			network.resetHiddenActivations();
			network.addHiddenActivations("sigmoid");
			network.resetHiddenInitializers();
			network.addHiddenInitializers("xavier");
			network.reconstruct();
			System.out.println("neural network is resetted!");
			scan.enter();
		} else {
			System.out.println("neural network is NOT resetted!");
		}
	}
	
	void help() {
		int current_page = 1;
		String[] pages = {
			" This program is a handwritten digit recognizer made using a Multi\r\n"
			+ " Layer Perceptron (MLP) implemented fully in Java.\r\n"
			+ "\r\n"
			+ " The neural network is trained using the MNIST handwritten digit dataset.\r\n"
			+ "\r\n"
			+ " Main Features:\r\n"
			+ " - Custom neural network architecture\r\n"
			+ " - Multiple hidden layers\r\n"
			+ " - Different activation functions\r\n"
			+ " - Neural network visualization\r\n"
			+ " - Training and testing\r\n"
			+ " - Drawing canvas prediction\r\n"
			+ " - Save and load model\r\n"
			+ " - Configurable training settings\r\n"
			+ "\r\n"
			+ " The program is designed for:\r\n"
			+ " - learning neural networks\r\n"
			+ " - experimenting with architectures\r\n"
			+ " - understanding backpropagation\r\n"
			+ " - educational visualization\r\n"
			+ "\r\n"
			+ " Navigation:\r\n"
			+ " - Enter menu number to choose action\r\n"
			+ " - Enter -1 to exit\r\n"
			+ " - Follow on-screen instructions",
			
			" MAIN MENU EXPLANATION\r\n"
			+ "\r\n"
			+ " 0 - Reset Network: Resets the neural network into a new untrained\r\n"
			+ "                    state. WARNING: All trained weights will be lost\r\n"
			+ "\r\n"
			+ " 1 - View Network:  Displays the neural network structure, layers,\r\n"
			+ "                    neurons, weights, and information.\r\n"
			+ "\r\n"
			+ " 2 - Edit Network:  Modify your own neural network structure. Some\r\n"
			+ "                    feature are limited on purpose and editing is\r\n"
			+ "                    disabled after training starts.\r\n"
			+ "\r\n"
			+ " 3 - Load Model:    Loads a saved neural network from file.\r\n"
			+ "\r\n"
			+ " 4 - Save Model:    Saves the current neural network into file.\r\n"
			+ "\r\n"
			+ " 5 - Train Network: Starts neural network training using MNIST.\r\n"
			+ "\r\n"
			+ " 6 - Test Network:  Tests prediction accuracy using test dataset.\r\n"
			+ "\r\n"
			+ " 7 - Draw Test:     Draw a digit manually and let your AI predict.\r\n"
			+ "\r\n"
			+ " 8 - Settings:      Configure some training settings.\r\n"
			+ "\r\n"
			+ " 9 - Help:          Opens this help menu.",
			
			" NEURAL NETWORK ARCHITECTURE\r\n"
			+ "\r\n"
			+ " The neural network structure:\r\n"
			+ " Input -> Hidden  -> Output\r\n"
			+ " Layer    Layer(s)   Layer\r\n"
			+ "\r\n"
			+ " Input Layer:\r\n"
			+ " - Contains 784 neurons\r\n"
			+ " - Represents 28x28 MNIST image pixels\r\n"
			+ "\r\n"
			+ " Hidden Layers:\r\n"
			+ " - User configurable\r\n"
			+ " - Performs feature extraction\r\n"
			+ " - Uses activation functions\r\n"
			+ "\r\n"
			+ " Output Layer:\r\n"
			+ " - Contains 10 neurons\r\n"
			+ " - Represents digits:\r\n"
			+ "   0 1 2 3 4 5 6 7 8 9\r\n"
			+ "\r\n"
			+ " Prediction:\r\n"
			+ " The neuron with highest output value becomes the predicted digit.\r\n"
			+ "\r\n"
			+ " Example:\r\n"
			+ " Output:\r\n"
			+ "    0     1     2     3     4     5   ...\r\n"
			+ " [0.01, 0.02, 0.90, 0.46, 0.01, 0.78, ...]\r\n"
			+ "\r\n"
			+ " Prediction:\r\n"
			+ " 2",
			
			" ACTIVATION FUNCTIONS\r\n"
			+ "\r\n"
			+ " Activation functions introduce non-linearity into the neural network.\r\n"
			+ " Without it, the network behaves like simple linear mathematics.\r\n"
			+ "\r\n"
			+ " Current Supported Functions:\r\n"
			+ "\r\n"
			+ " SIGMOID                   1.0 |                                   ***********\r\n"
			+ " Range: (0, 1)                 |                              *****\r\n"
			+ " Formula:                      |                           ***\r\n"
			+ "  f(x) = 1 / (1 + e^-x)        |                         **\r\n"
			+ "                               |                        * \r\n"
			+ " Advantages:               0.5 |-----------------------*----------------------\r\n"
			+ " - smooth output               |                      *\r\n"
			+ " - easy to understand          |                    **\r\n"
			+ "                               |                 ***\r\n"
			+ " Disadvantages:                |            *****\r\n"
			+ " - vanishing gradient      0.0 +************----------------------------------\r\n"
			+ " - slower training               -4         -2         0         2         4\r\n"
			+ " \r\n"
			+ " RELU                      1.0 |                                          **\r\n"
			+ " RANGE: 0 to +inf              |                                        **\r\n"
			+ " Formula:                      |                                      **\r\n"
			+ "  f(x) = max(0, x)             |                                    **\r\n"
			+ "                               |                                  **\r\n"
			+ " Advantages:               0.5 |--------------------------------**------------\r\n"
			+ " - very fast computation       |                              **\r\n"
			+ " - reduces vanishing gradient  |                            **\r\n"
			+ "                               |                          **\r\n"
			+ " Disadvantages:                |                        **\r\n"
			+ " - neurons can \"die\"       0.0 +************************----------------------\r\n"
			+ " - negative values always 0       -1                   0                   1\r\n"
			+ "\r\n"
			+ " Future Recommended Functions:\r\n"
			+ " - LeakyReLU\r\n"
			+ " - Softmax",
			
			"TRAINING SETTINGS\r\n"
			+ "\r\n"
			+ "Batch Size: Number of training samples processed before updating its weights.\r\n"
			+ "\r\n"
			+ "Advantages:\r\n"
			+ "- smoother learning\r\n"
			+ "- more stable gradients\r\n"
			+ "- faster CPU performance\r\n"
			+ "\r\n"
			+ "Smaller: noisier updates and may generalize better\r\n"
			+ "Larger: more stable updates and uses more memory\r\n"
			+ "\r\n"
			+ "Recommended: 64 to 128, only powers of 2 are allowed.\r\n"
			+ "\r\n"
			+ "Decay Rate: Controls how quickly the learning rate decreases over epochs.\r\n"
			+ "\r\n"
			+ "Formula: lr = lr * e^(-decayRate * epoch)\r\n"
			+ "\r\n"
			+ "Purpose: Large learning rates are useful early, smaller learning rates\r\n"
			+ "are useful later.\r\n"
			+ "\r\n"
			+ "Small: learning rate changes slowly\r\n"
			+ "Large: learning rate decreases faster\r\n"
			+ "\r\n"
			+ "Recommended: 0.001 to 0.05",
		};
		String input;
		do {			
			ConsoleHandler.clear();
			printTitle();
			System.out.println("\n" + pages[current_page-1]);
			
			System.out.println("\n Page "+ current_page + " of " + pages.length);

			System.out.println("\n Type a page number, or press enter to continue, or type \"exit\" to go back");
			input = scan.stringInput(" >> ");
			try {
				int new_page = Integer.parseInt(input);
				if (new_page < 1 || new_page > pages.length) 
					throw new Exception();
				current_page = new_page - 1;
			} catch (Exception e) {}
			current_page++;
		} while (! input.equalsIgnoreCase("exit") && current_page <= pages.length);
	}
	
	void menu() {
		int menu = 0;
		ConsoleHandler.fullscreen();
		Runnable[] actions = {
	        () -> this.reset(),

	        () -> viewer.view(),

	        () -> viewer.edit(),

	        () -> file.load(),

	        () -> file.save(),

	        () -> trainer.train(),

	        () -> tester.test(),

	        () -> tester.drawTest(),

	        () -> trainer.setting(),

	        () -> this.help()
	    };
		
		do {

	        ConsoleHandler.clear();

	        printTitle();
	        printMenu();

	        menu = scan.intInput(" >> ", -1, actions.length - 1);

	        if (menu != -1) {
	            actions[menu].run();
	        }

	    } while (menu != -1);
		scan.close();
		ConsoleHandler.fullscreen();
	}

	public static void main(String[] args) {
		new MainMenu().menu();
	}

}
