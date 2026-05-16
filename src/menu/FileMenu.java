package menu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import activationFunction.ActivationManager;
import initializer.InitializerManager;
import neuralNetwork.HiddenLayer;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.Neuron;
import neuralNetwork.OutputLayer;
import tool.ConsoleHandler;
import tool.InputHandler;

public class FileMenu {
	NeuralNetwork network;
	InputHandler scan;
	String prefixDirectory = "result/";

	public FileMenu(NeuralNetwork network, InputHandler scan) {
		this.network = network;
		this.scan = scan;
	}
	
	public void save() {
		String filename = scan.stringInput("Save file as: ", "\\/:*?\"<>| ", 252) + ".dat";
		filename = prefixDirectory + filename;
		try {
			DataOutputStream file = new DataOutputStream(new FileOutputStream(filename));
			
			int hiddenLayerSize = network.getNumHiddenLayers().size();
			
			file.writeInt(hiddenLayerSize);
			file.writeInt(network.getEpoch());
			
			for (Integer i : network.getNumHiddenLayers()) {
				file.writeInt(i);
			}
			
			for (String i : network.getHiddenActivations()) {
				char a = i.toLowerCase().charAt(0);
				file.writeChar(a);
			}
			
			for (String i : network.getHiddenInitializers()) {
				char a = i.toLowerCase().charAt(0);
				file.writeChar(a);
			}
			
			for (int indexLayer = 1; indexLayer <= hiddenLayerSize; indexLayer++) {
				HiddenLayer hiddenLayer = (HiddenLayer) network.getLayer(indexLayer);
				Neuron[] neurons = hiddenLayer.getNeurons();
				for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
					Neuron neuron = neurons[indexNeuron];
					double[] weights = neuron.getWeights();
					for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
						file.writeDouble(weights[indexWeight]);
					}
					file.writeDouble(neuron.getBias());
				}
			}
			
			OutputLayer outputLayer = (OutputLayer) network.getLayer(hiddenLayerSize + 1);
			Neuron[] neurons = outputLayer.getNeurons();
			for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
				Neuron neuron = neurons[indexNeuron];
				double[] weights = neuron.getWeights();
				for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
					file.writeDouble(weights[indexWeight]);
				}
				file.writeDouble(neuron.getBias());
			}
			
			file.close();
			System.out.println("Succesfully saved");
		} catch (Exception e) {
			System.out.println("Can't save the neural network");
		}
		scan.enter();
	}
	
	public void load() {
		File folder = new File("result");
		ArrayList<File> files = new ArrayList<>();
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".dat")) {
				files.add(file);
			}
		}
		String filename;

		int hiddenLayerSize;
		int epoch;
		int[] numHiddenLayers;
		String[] hiddenActivations = null;
		String[] hiddenInitializers = null;
		
		DataInputStream fileStream = null;
		
		while (true) {
			ConsoleHandler.clear();
			
			System.out.println("Available File:");
			if (files.size() == 0) {
				System.out.println(" Empty");
				scan.enter();
				return;
			}
			
			for (File currentFile : files) {
				if (currentFile.getName().endsWith(".dat")) {
					System.out.println(" - " + currentFile.getName());
				}
			}
			
			System.out.println("________________________________________________________________________________________________________________________________________________________________________________");
			filename = scan.stringInput("Load file as ('exit' to exit): ", "\\/:*?\"<>| ", 255);
			if ("exit".equalsIgnoreCase(filename)) return;
			if (! filename.endsWith(".dat")) filename = filename.concat(".dat");
			File file = null;
			for (File currentFile : files) {
				if (currentFile.getName().equals(filename)) {
					file = currentFile;
					break;
				}
			}
			
			if (file == null) {
				System.out.println("Can't find the file named \"" + filename + "\".");
				scan.enter();
				continue;
			}
			
			try {
				fileStream = new DataInputStream(new FileInputStream(prefixDirectory + filename));
				hiddenLayerSize = fileStream.readInt();
				epoch = fileStream.readInt();
				numHiddenLayers = new int[hiddenLayerSize];
				for (int i = 0; i < hiddenLayerSize; i++) {
					numHiddenLayers[i] = fileStream.readInt();
				}
				hiddenActivations = new String[hiddenLayerSize];
				for (int i = 0; i < hiddenLayerSize; i++) {
					String result = ActivationManager.charToString(fileStream.readChar());
					hiddenActivations[i] = result;
				}
				hiddenInitializers = new String[hiddenLayerSize];
				for (int i = 0; i < hiddenLayerSize; i++) {
					String result = InitializerManager.charToString(fileStream.readChar());
					hiddenInitializers[i] = result;
				}
				fileStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			System.out.println("Succesfully loaded \"" + filename + "\".");
			System.out.println();
			System.out.printf("Layers: %d\n", hiddenLayerSize+2);
			System.out.printf("Epoch: %d\n", epoch);
			System.out.print("Neurons: 784");
			for (int i : numHiddenLayers) {
				System.out.print(", " + i);
			}
			System.out.println(", 10");
			System.out.print("Function Activations: ");
			for (String str : hiddenActivations) {
				System.out.print(str + ", ");
			}
			System.out.println("softmax");
			System.out.print("Initializers: ");
			for (String str : hiddenInitializers) {
				System.out.print(str + ", ");
			}
			System.out.println("xavier");
			System.out.println();
			// are you sure?
			String confirm = scan.stringInput("Load this file[yes/no]? ");
			if (confirm.toLowerCase().equals("yes")) break;
		}
		
		try {
			fileStream = new DataInputStream(new FileInputStream(prefixDirectory + filename));
			hiddenLayerSize = fileStream.readInt();
			epoch = fileStream.readInt();
			numHiddenLayers = new int[hiddenLayerSize];
			
			for (int i = 0; i < hiddenLayerSize; i++) {
				numHiddenLayers[i] = fileStream.readInt();
			}
			
			hiddenActivations = new String[hiddenLayerSize];
			for (int i = 0; i < hiddenLayerSize; i++) {
				String result = ActivationManager.charToString(fileStream.readChar());
				hiddenActivations[i] = result;
			}
			
			hiddenInitializers = new String[hiddenLayerSize];
			for (int i = 0; i < hiddenLayerSize; i++) {
				String result = InitializerManager.charToString(fileStream.readChar());
				hiddenInitializers[i] = result;
			}
			
			network.setEpoch(epoch);
			network.resetNumHiddenLayers();
	        for (int i : numHiddenLayers) {
	        	network.addNumHiddenLayers(i);
	        }
	        network.resetHiddenActivations();
			network.addHiddenActivations(hiddenActivations);
	        network.resetHiddenInitializers();
			network.addHiddenInitializers(hiddenInitializers);
			network.reconstruct();
			
			for (int indexLayer = 1; indexLayer <= hiddenLayerSize; indexLayer++) {
				HiddenLayer hiddenLayer = (HiddenLayer) network.getLayer(indexLayer);
				Neuron[] neurons = hiddenLayer.getNeurons();
				
				for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
					Neuron neuron = neurons[indexNeuron];
					double[] weights = neuron.getWeights();
					for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
						weights[indexWeight] = fileStream.readDouble();
					}
					neuron.setWeights(weights);
					neuron.setBias(fileStream.readDouble());
				}
				hiddenLayer.setNeurons(neurons);
				network.setLayer(indexLayer, hiddenLayer);
			}
			
			OutputLayer outputLayer = (OutputLayer) network.getLayer(hiddenLayerSize + 1);
			Neuron[] neurons = outputLayer.getNeurons();
			for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
				Neuron neuron = neurons[indexNeuron];
				double[] weights = neuron.getWeights();
				for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
					weights[indexWeight] = fileStream.readDouble();
				}
				neuron.setWeights(weights);
				neuron.setBias(fileStream.readDouble());
			}
			outputLayer.setNeurons(neurons);
			network.setLayer(hiddenLayerSize + 1, outputLayer);
			
			fileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Succesfully loaded!");
		scan.enter();
	}
}
