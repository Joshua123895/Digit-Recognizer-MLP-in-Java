package menu;

import java.util.ArrayList;
import java.util.Arrays;

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

	public void view() {
		ConsoleHandler.clear();
		
		ArrayList<Integer> numLayer = new ArrayList<>(Arrays.asList(784));
		for (Integer i : network.numHiddenLayers) {
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
		for (String str : network.hiddenActivations) {
			System.out.print("   " + str.toLowerCase().substring(0, 3));
		}
		System.out.println("   sig");
	}
	
	public void edit() {
		if (network.getEpoch() > 0) {
			System.out.println("network can't be changed once it's has been trained");
			scan.enter();
			return;
		}
		
		String input = "";
		while (true) {
			view();
			System.out.println(" You can only add, edit, or remove hidden layers");
			System.out.println(" First hidden layer has index 1, etc.");
			System.out.println(" Must have minimum 1 hidden layer");
			System.out.println(" Also, it cannot be edited once the training has started\n");
			
			System.out.println(" Function available: sigmoid, relu\n");
			
			System.out.println(" To insert a layer: insert [index] [neurons] [function]");
			System.out.println(" To change neurons: change [index] [neurons] [function]");
			System.out.println(" To delete a layer: delete [index]\n");
			
			System.out.println(" To exit: exit");
			input = scan.stringInput(" >> ").toLowerCase().trim();
			String[] part = input.split("\\s+");
			try {
				if (input.equalsIgnoreCase("exit")) break;
				int index = Integer.parseInt(part[1]) - 1;
				if (index < 0 || index > network.numHiddenLayers.size()) {
					throw new Exception();
				}
				int neuron;
				switch (part[0]) {
				case "insert":
					if (part.length != 4) {
						throw new Exception();
					}
					neuron = Integer.parseInt(part[2]);
					if (neuron <= 0) {
						throw new Exception();
					}
					network.numHiddenLayers.add(index, neuron);
					network.hiddenActivations.add(index, part[3]);
					break;
				case "change":
					if (part.length != 4) {
						throw new Exception();
					}
					neuron = Integer.parseInt(part[2]);
					if (neuron <= 0) {
						throw new Exception();
					}
					network.numHiddenLayers.set(index, neuron);
					network.hiddenActivations.set(index, part[3]);
					break;
				case "delete":
					if (part.length != 2 || network.numHiddenLayers.size() <= 1) {
						throw new Exception();
					}
					network.numHiddenLayers.remove(index);
					network.hiddenActivations.remove(index);
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
