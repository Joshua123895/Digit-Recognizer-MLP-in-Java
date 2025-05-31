package menu;

import java.util.ArrayList;
import java.util.Arrays;

import mnist.MNISTManager;
import neuralNetwork.NeuralNetwork;
import tool.InputHandler;
import tool.ConsoleHandler;

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
//		System.out.println(" 7. Draw and Test");
	}
	
	void reset() {
		String validate = scan.stringInput("Are you sure[yes/no]? ");
		if (validate.equalsIgnoreCase("yes")) {
			network.setEpoch(0);
			network.numHiddenLayers = new ArrayList<>(Arrays.asList(128));
			network.hiddenActivations = new ArrayList<>(Arrays.asList("sigmoid"));
			network.reconstruct();
			System.out.println("neural network is resetted!");
		} else {
			System.out.println("neural network is NOT resetted!");
		}
	}
	
	void menu() {
		int menu = 0;
		ConsoleHandler.fullscreen();
		do {
			ConsoleHandler.clear();
			printTitle();
			printMenu();
			menu = scan.intInput(" >> ", -1, 6);
			switch (menu) {
			case 0:
				reset();
				scan.enter();
				break;
			case 1:
				viewer.view();
				scan.enter();
				break;
			case 2:
				viewer.edit();
				break;
			case 3:
				file.load();
				break;
			case 4:
				file.save();
				break;
			case 5:
				trainer.train();
				break;
			case 6:
				tester.test();
				break;
//			case 7:
//				tester.drawTest();
//				break;
			}
		} while (menu != -1);
		scan.close();
		ConsoleHandler.fullscreen();
	}

	public static void main(String[] args) {
		new MainMenu().menu();
	}

}
