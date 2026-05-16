package menu;

import neuralNetwork.NeuralNetwork;
import tool.ConsoleHandler;
import tool.InputHandler;
import tool.MNISTManager;
import tool.NumberTool;

public class TrainMenu {
	NeuralNetwork network;
	MNISTManager trainData;
	InputHandler scan;
	
	private static int batchSize = 64;
	private static double decayRate = 0.01; 
	final int progressLength = 50; // for loading visual ===--- 50%

	public TrainMenu(NeuralNetwork network, MNISTManager source, InputHandler scan) {
		this.network = network;
		this.trainData = source;
		this.scan = scan;
	}
	
//	public static void setBatchSize(int batchSize) {
//		TrainMenu.batchSize = batchSize;
//	}
//	
//	public static void setDecayRate(double decayRate) {
//		TrainMenu.decayRate = decayRate;
//	}
		
	public static double getDecayRate() {
		return TrainMenu.decayRate;
	}
	
	public void train() {
		int epochs = scan.intInput("How many epochs[1..100]? ", 1, 100);
		if (network.getEpoch() == 0)  {
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
	        double totalLoss = 0;
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
	            
	            network.feedForward(trainData.getImages(i));
	            totalLoss += network.computeLoss(trainData.getLabels(i));
	            network.backpropagate(trainData.getLabels(i));
	            
				// Apply updates every batch
				if ((i + 1) % batchSize == 0) {
				    network.applyBatchGradients(batchSize);
				}
	        }
	        // Last remaining batch if not applied
	        if (trainSize % batchSize != 0) {
	            network.applyBatchGradients(trainSize % batchSize);
	        }
	        
	        long endTime = System.nanoTime();
	        double time = (endTime - startTime) / 1_000_000_000.0;
	        double avgLoss = totalLoss / trainSize;
	        System.out.printf(" time: %.3fs", time);
	        System.out.printf(" loss: %.3f\n", avgLoss);
	        network.nextEpoch();
		}
		System.out.println("Done training!");
		scan.enter();
	}
	
	public void setting() {
		while (true) {
			ConsoleHandler.clear();
			System.out.println(" Training setting:");
			System.out.println(" - Batch Size: " + TrainMenu.batchSize);
			System.out.println(" - Decay Rate: " + TrainMenu.decayRate);
			System.out.println("");
			System.out.println(" To change: change [name] [value]");
			System.out.println(" - Batch Size: 2^n, like 1, 2, 4, ..., 256");
			System.out.println(" - Decay Rate: 0-1");
			System.out.println("");
			System.out.println(" To exit: exit");
			System.out.println("");
			System.out.println(" For more info, choose menu Help in the main menu");
			String input = scan.stringInput(" >> ");
			if (input.equalsIgnoreCase("exit"))
				return;
			try {
				if (! input.startsWith("change ")) {
					throw new Exception();
				}
				String[] part = input.split("\\s+");
				switch (part[1].substring(0, 5)) {
				case "batch":
					int n = Integer.parseInt(part[part.length - 1]);
					if (! (n > 0 && (n & (n - 1)) == 0)) {
						throw new Exception();
					}
					TrainMenu.batchSize = n;
					System.out.println(" Batch Size has been succesfully updated.");
					scan.enter();
					break;
				case "decay":
					double d = Double.parseDouble(part[part.length - 1]);
					if (! (d > 0 && d < 1)) {
						throw new Exception();
					}
					TrainMenu.decayRate = d;
					System.out.println(" Decay Rate has been succesfully updated.");
					scan.enter();
					break;
				default:
					System.out.println(" No settings available");
					scan.enter();
				}
			} catch (Exception e) {
				System.out.println(" Input must follow the rules above");
				scan.enter();
			}
		}
	}
}
