package menu;

import java.util.ArrayList;
import java.util.Arrays;

import initializer.InitializerManager;
import neuralNetwork.NeuralNetwork;
import tool.InputHandler;
import tool.ConsoleHandler;

public class ViewEditMenu {
	NeuralNetwork network;
	InputHandler scan;

	public ViewEditMenu(NeuralNetwork network, InputHandler scan) {
		this.network = network;
		this.scan = scan;
	}

	private void viewOnly() {
		ConsoleHandler.clear();
		
		ArrayList<Integer> numLayer = new ArrayList<>(Arrays.asList(784));
		for (Integer i : network.getNumHiddenLayers()) {
			numLayer.add(i);
		}
		numLayer.add(10);
		
		for (int i : numLayer) {
			System.out.printf(" %3d  ", i);
		}
		System.out.println();
		
		int n = 12;
		int scale = 5;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < numLayer.size(); j++) {
				int num = numLayer.get(j) / scale;
				if (num > 10 && i >= n/2-1 && i <= n/2) {
					System.out.print("  .  ");
				} else if (num / 2 < (int) (Math.abs((n-1)/2.0-i) + 0.5)) {
					System.out.print("     ");
				} else {
					System.out.print("  o  ");
				}
				if (j != numLayer.size() - 1) {
					if (Math.min(numLayer.get(j+1)/scale, num) / 2 < (int) (Math.abs((n-1)/2.0-i) + 0.5)) {
						System.out.print(" ");
					} else {
						System.out.print("~");
					}
				}
 			}
			System.out.println();
		}
		System.out.print(" ");
		for (String str : network.getHiddenActivations()) {
			System.out.print("   " + str.toLowerCase().substring(0, 3));
		}
		System.out.println("   sof");
		System.out.print(" ");
		for (String str : network.getHiddenInitializers()) {
			System.out.print("   " + str.toLowerCase().concat("   ").substring(0, 3));
		}
		System.out.println("   xav");
	}
	
	public void view() {
		this.viewOnly();
		scan.enter();
	}
	
	public void edit() {
		if (network.getEpoch() > 0) {
			System.out.println("network can't be changed once it's has been trained");
			scan.enter();
			return;
		}
		
		String input = "";
		while (true) {
			viewOnly();
			System.out.println(" You can only add, edit, or remove hidden layers");
			System.out.println(" First hidden layer has index 1, etc.");
			System.out.println(" Must have minimum 1 hidden layer");
			System.out.println(" Also, it cannot be edited once the training has started\n");
			
			System.out.println(" Function available: sigmoid, relu");
			System.out.println(" Initializer available: simple, xavier, he, auto\n");
			
			System.out.println(" To insert a layer: insert [index] [neurons] [function] [init]");
			System.out.println(" To change neurons: change [index] [neurons] [function] [init]");
			System.out.println(" To delete a layer: delete [index]\n");
			
			System.out.println(" To exit: exit");
			
			System.out.println(" For more info, choose menu Help in the main menu");
			input = scan.stringInput(" >> ").toLowerCase().trim();
			String[] part = input.split("\\s+");
			try {
				if (input.equalsIgnoreCase("exit")) break;
				int index = Integer.parseInt(part[1]) - 1;
				if (index < 0 || index > network.getNumHiddenLayers().size()) {
					throw new Exception();
				}
				int neuron;
				String init = part[4];
				switch (part[0]) {
				case "insert":
					if (part.length != 5) {
						throw new Exception();
					}
					neuron = Integer.parseInt(part[2]);
					if (neuron <= 0) {
						throw new Exception();
					}
					if (part[4].equalsIgnoreCase("auto")) 
						init = InitializerManager.autoChooseFromFunction(part[3]);
					
					network.addNumHiddenLayers(index, neuron);
					network.addHiddenActivations(index, part[3]);
					network.addHiddenInitializers(index, init);
					break;
				case "change":
					if (part.length != 5) {
						throw new Exception();
					}
					neuron = Integer.parseInt(part[2]);
					if (neuron <= 0) {
						throw new Exception();
					}
					if (part[4].equalsIgnoreCase("auto")) 
						init = InitializerManager.autoChooseFromFunction(part[3]);
					
					network.setNumHiddenLayers(index, neuron);
					network.setHiddenActivations(index, part[3]);
					network.setHiddenInitializers(index, init);
					break;
				case "delete":
					if (part.length != 2 || network.getNumHiddenLayers().size() <= 1) {
						throw new Exception();
					}
					network.removeNumHiddenLayers(index);
					network.removeHiddenActivations(index);
					network.removeHiddenInitializers(index);
					break;
				default:
					throw new Exception();
				}
				network.reconstruct();
				System.out.println("Command succesfully!");
			} catch (Exception e) {
				System.out.println("Input must follow the rules above");
			}
			scan.enter();
		}
	}
	
}
