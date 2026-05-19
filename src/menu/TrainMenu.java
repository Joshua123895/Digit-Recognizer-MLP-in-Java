package menu;

import neuralNetwork.NeuralNetwork;
import optimizer.OptimizerManager;
import tool.ConsoleHandler;
import tool.InputHandler;
import tool.MNISTManager;
import tool.NumberTool;

public class TrainMenu {
	NeuralNetwork network;
	MNISTManager trainData;
	InputHandler scan;
	
	final int progressLength = 50; // for loading visual ===--- 50%

	public TrainMenu(NeuralNetwork network, MNISTManager source, InputHandler scan) {
		this.network = network;
		this.trainData = source;
		this.scan = scan;
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
	            
	            network.feedForward(trainData.getImages(i), true);
	            totalLoss += network.computeLoss(trainData.getLabels(i));
	            network.backpropagate(trainData.getLabels(i));
	            
				// Apply updates every batch
				if ((i + 1) % network.getBatchSize() == 0) {
				    network.applyGradients();
				}
	        }
	        // Last remaining batch if not applied
	        if (trainSize % network.getBatchSize() != 0) {
	            network.applyGradients(trainSize % network.getBatchSize());
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
			System.out.println(" - Batch Size: " + network.getBatchSize());
			System.out.println(" - Decay Rate: " + network.getDecayRate());
			System.out.println(" - Learning Rate: " + network.getInitialLearningRate());
			System.out.println(" - Optimizer: " + network.getOptimizerName());
			System.out.println(" - Dropout Rate: " + network.getDropoutRate());
			System.out.println("");
			if (network.getEpoch() > 0) {
				scan.enter();
				break;
			}	
			System.out.println(" You can only change these settings before training");	
			System.out.println(" To change: change [name] [value]");
			System.out.println(" - Batch Size: 2^n, like 1, 2, 4, ..., 256");
			System.out.println(" - Decay Rate: 0-1");
			System.out.println(" - Learning Rate: 0-1");
			System.out.println(" - Optimizer: adam, sgd");
			System.out.println(" - Dropout Rate: 0-1");
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
				if (network.getEpoch() > 0) {
					System.out.println(" You have trained your model. You cannot update this anymore.");
					scan.enter();
					continue;
				}
				String[] part = input.split("\\s+");
				switch (part[1].substring(0, 5)) {
				case "batch":
					int n = Integer.parseInt(part[part.length - 1]);
					if (! (n > 0 && (n & (n - 1)) == 0)) {
						throw new Exception();
					}
					network.setBatchSize(n);
					System.out.print(" Batch Size");
					break;
				case "decay":
					double d = Double.parseDouble(part[part.length - 1]);
					if (! (d >= 0 && d < 1)) {
						throw new Exception();
					}
					network.setDecayRate(d);
					System.out.print(" Decay Rate");
					break;
				case "learn":
					double l = Double.parseDouble(part[part.length - 1]);
					if (! (l > 0 && l < 1)) {
						throw new Exception();
					}
					network.setInitialLearningRate(l);
					System.out.print(" Learning Rate");
					break;
				case "optim":
					String o = OptimizerManager.charToString(part[part.length - 1].charAt(0));
					network.setOptimizerName(o);
					network.reconstruct();
					System.out.print(" Optimizer");
					break;
				case "dropo":
					double r = Double.parseDouble(part[part.length - 1]);
					if (! (r >= 0 && r < 1)) {
						throw new Exception();
					}
					network.setDropoutRate(r);
					System.out.print(" Dropout Rate");
					break;				
				default:
					System.out.print(" Nothing");
				}
				System.out.println(" has been updated.");
				scan.enter();
			} catch (Exception e) {
				System.out.println(" Input must follow the rules above");
				scan.enter();
			}
		}
	}
}
