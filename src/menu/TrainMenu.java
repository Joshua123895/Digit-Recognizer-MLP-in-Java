package menu;

import mnist.MNISTManager;
import neuralNetwork.NeuralNetwork;
import tool.ConsoleHandler;
import tool.InputHandler;
import tool.NumberTool;

public class TrainMenu {
	NeuralNetwork network;
	MNISTManager trainData;
	InputHandler scan;
	
	final int progressLength = 50;

	public TrainMenu(NeuralNetwork network, MNISTManager source, InputHandler scan) {
		this.network = network;
		this.trainData = source;
		this.scan = scan;
	}
	
	public void train() {
		int epochs = scan.intInput("How many epochs[1..100]? ", 1, 100);
		if (network.getEpoch() == 0)  {
			network.setDecayRate(scan.doubleInput("How much decay rate[0..1]? ", 0, 1));
			System.out.println("Warning! You can't edit your neural network after training!");
			String confirm = scan.stringInput("Are you sure[yes/no]? ");
			if (!"yes".equalsIgnoreCase(confirm)) return;
		}
		ConsoleHandler.clear();
		int trainSize = trainData.getSize();
		
		for (int e = 1; e <= epochs; e++) {
	        network.nextLearningate();
	        
			System.out.printf("epoch %d/%d: --------------------------------------------------   0%% %.3f", e, epochs, network.getLearningRate());
	        long startTime = System.nanoTime();
	        
	        int percent = 0;
	        int offset = NumberTool.countDigits(e) + NumberTool.countDigits(epochs); // 3 + 3
	        for (int i = 0; i < trainSize; i++) {
	            int currentPercent = (int) ((i+1) * 100.0 / trainSize);
	            if (currentPercent > percent) {
	            	percent++;
	                int cursorPos = (int) (currentPercent / 100.0 * progressLength);
	                cursorPos -= (percent > 50)? 1 : 0;
	                ConsoleHandler.moveCursorTo(10 + cursorPos + offset, e);
	                System.out.print("#");
	                ConsoleHandler.moveCursorTo(11 + progressLength + offset, e);
	                System.out.printf("%3d%%", percent);
	            }
	        	// real training
	        	network.feedForward(trainData.getImages(i));
	        	network.backpropagate(trainData.getLabels(i));
	        }
	        long endTime = System.nanoTime();
	        double time = (endTime - startTime) / 1_000_000_000.0;
	        System.out.printf(" time: %.3fs\n", time);
	        network.nextEpoch();
		}
		System.out.println("Done training!");
		scan.enter();
	}
}
