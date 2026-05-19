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
import optimizer.OptimizerManager;
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
 
    // -------------------------------------------------------------------------
    // SAVE
    // -------------------------------------------------------------------------
 
    public void save() {
        String filename = scan.stringInput("Save file as: ", "\\/:*?\"<>| ", 252) + ".dat";
        filename = prefixDirectory + filename;
        try (DataOutputStream file = new DataOutputStream(new FileOutputStream(filename))) {
 
            int hiddenLayerSize = network.getNumHiddenLayers().size();
 
            // --- header ---
            file.writeInt(hiddenLayerSize);
            file.writeInt(network.getEpoch());
            file.writeInt(network.getBatchSize());
            file.writeDouble(network.getDecayRate());
            file.writeDouble(network.getInitialLearningRate());
            file.writeInt(network.getTimestep());
            file.writeChar(network.getOptimizerName().toLowerCase().charAt(0));
 
            // --- architecture ---
            for (int n : network.getNumHiddenLayers())       file.writeInt(n);
            for (String a : network.getHiddenActivations())  file.writeChar(a.toLowerCase().charAt(0));
            for (String i : network.getHiddenInitializers()) file.writeChar(i.toLowerCase().charAt(0));
 
            // --- hidden weights ---
            for (int indexLayer = 1; indexLayer <= hiddenLayerSize; indexLayer++) {
                writeLayerWeights(file, ((HiddenLayer) network.getLayer(indexLayer)).getNeurons());
            }
 
            // --- output weights ---
            writeLayerWeights(file, ((OutputLayer) network.getLayer(hiddenLayerSize + 1)).getNeurons());
 
            System.out.println("Successfully saved.");
 
        } catch (Exception e) {
            System.out.println("Can't save the neural network: " + e.getMessage());
        }
        scan.enter();
    }
 
    private void writeLayerWeights(DataOutputStream file, Neuron[] neurons) throws Exception {
        for (Neuron neuron : neurons) {
            for (double w : neuron.getWeights()) file.writeDouble(w);
            file.writeDouble(neuron.getBias());
        }
    }
 
    // -------------------------------------------------------------------------
    // LOAD
    // -------------------------------------------------------------------------
 
    public void load() {
        ArrayList<File> files = getDataFiles();
 
        while (true) {
            ConsoleHandler.clear();
            System.out.println("Available Files:");
 
            if (files.isEmpty()) {
                System.out.println(" Empty");
                scan.enter();
                return;
            }
 
            for (File f : files) System.out.println(" - " + f.getName());
            ConsoleHandler.printDivider();
 
            String input = scan.stringInput("Load file ('exit' to cancel): ", "\\/:*?\"<>| ", 255);
            if ("exit".equalsIgnoreCase(input)) return;
 
            String filename = input.endsWith(".dat") ? input : input + ".dat";
            File chosen = findFile(files, filename);
 
            if (chosen == null) {
                System.out.println("File \"" + filename + "\" not found.");
                scan.enter();
                continue;
            }
 
            // --- parse once for preview ---
            NetworkSnapshot snapshot = parseFile(prefixDirectory + filename);
            if (snapshot == null) { scan.enter(); return; }
 
            printSnapshot(snapshot);
 
            String confirm = scan.stringInput("Load this file [yes/no]? ");
            if (!"yes".equalsIgnoreCase(confirm)) continue;
 
            // --- apply to network ---
            applySnapshot(snapshot, prefixDirectory + filename);
            System.out.println("Successfully loaded!");
            scan.enter();
            return;
        }
    }
 
    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
 
    private ArrayList<File> getDataFiles() {
        ArrayList<File> list = new ArrayList<>();
        File folder = new File("result");
        File[] contents = folder.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (f.getName().endsWith(".dat")) list.add(f);
            }
        }
        return list;
    }
 
    private File findFile(ArrayList<File> files, String name) {
        for (File f : files) if (f.getName().equals(name)) return f;
        return null;
    }
 
    /** Holds everything read from a .dat file. */
    private static class NetworkSnapshot {
        int hiddenLayerSize, epoch, batchSize;
        double decayRate, learningRate;
        String optimizerName;
        int[] numHiddenLayers;
        int timestep;
        String[] hiddenActivations, hiddenInitializers;
        // weights[layer][neuron] = {w0, w1, ..., bias}
        double[][][] hiddenWeights;
        double[][] outputWeights;
    }
 
    private NetworkSnapshot parseFile(String path) {
        NetworkSnapshot s = new NetworkSnapshot();
        try (DataInputStream in = new DataInputStream(new FileInputStream(path))) {
 
            s.hiddenLayerSize  = in.readInt();
            s.epoch            = in.readInt();
            s.batchSize        = in.readInt();
            s.decayRate        = in.readDouble();
            s.learningRate     = in.readDouble();
            s.timestep		   = in.readInt();
            s.optimizerName    = OptimizerManager.charToString(in.readChar());
 
            s.numHiddenLayers   = new int[s.hiddenLayerSize];
            s.hiddenActivations  = new String[s.hiddenLayerSize];
            s.hiddenInitializers = new String[s.hiddenLayerSize];
 
            for (int i = 0; i < s.hiddenLayerSize; i++) s.numHiddenLayers[i]   = in.readInt();
            for (int i = 0; i < s.hiddenLayerSize; i++) s.hiddenActivations[i]  = ActivationManager.charToString(in.readChar());
            for (int i = 0; i < s.hiddenLayerSize; i++) s.hiddenInitializers[i] = InitializerManager.charToString(in.readChar());
 
            // hidden weights
            s.hiddenWeights = new double[s.hiddenLayerSize][][];
            for (int l = 0; l < s.hiddenLayerSize; l++) {
                int neurons    = s.numHiddenLayers[l];
                int prevSize   = (l == 0) ? 784 : s.numHiddenLayers[l - 1];
                s.hiddenWeights[l] = new double[neurons][prevSize + 1]; // +1 for bias
                for (int n = 0; n < neurons; n++) {
                    for (int w = 0; w < prevSize; w++) s.hiddenWeights[l][n][w] = in.readDouble();
                    s.hiddenWeights[l][n][prevSize] = in.readDouble(); // bias
                }
            }
 
            // output weights (10 neurons)
            int lastHidden = s.numHiddenLayers[s.hiddenLayerSize - 1];
            s.outputWeights = new double[10][lastHidden + 1];
            for (int n = 0; n < 10; n++) {
                for (int w = 0; w < lastHidden; w++) s.outputWeights[n][w] = in.readDouble();
                s.outputWeights[n][lastHidden] = in.readDouble(); // bias
            }
 
        } catch (Exception e) {
            System.out.println("Failed to read file: " + e.getMessage());
            return null;
        }
        return s;
    }
 
    private void printSnapshot(NetworkSnapshot s) {
        System.out.println("Layers:        " + (s.hiddenLayerSize + 2));
        System.out.println("Epoch:         " + s.epoch);
        System.out.println("Batch Size:    " + s.batchSize);
        System.out.printf ("Decay Rate:    %.3f%n", s.decayRate);
        System.out.printf ("Learning Rate: %.3f%n", s.learningRate);
        System.out.println("Optimizer:     " + s.optimizerName);
 
        System.out.print("Neurons: 784");
        for (int n : s.numHiddenLayers) System.out.print(", " + n);
        System.out.println(", 10");
 
        System.out.print("Activations: ");
        for (String a : s.hiddenActivations) System.out.print(a + ", ");
        System.out.println("softmax");
 
        System.out.print("Initializers: ");
        for (String i : s.hiddenInitializers) System.out.print(i + ", ");
        System.out.println("xavier");
        System.out.println();
    }
 
    private void applySnapshot(NetworkSnapshot s, String path) {
        // configure network metadata
        network.setEpoch(s.epoch);
        network.setBatchSize(s.batchSize);
        network.setDecayRate(s.decayRate);
        network.setInitialLearningRate(s.learningRate);
        network.setTimestep(s.timestep);
        network.setOptimizerName(s.optimizerName);
 
        // configure architecture
        network.resetNumHiddenLayers();
        for (int n : s.numHiddenLayers) network.addNumHiddenLayers(n);
 
        network.resetHiddenActivations();
        network.addHiddenActivations(s.hiddenActivations);
 
        network.resetHiddenInitializers();
        network.addHiddenInitializers(s.hiddenInitializers);
 
        // rebuild layers with correct initializers
        network.reconstruct();
 
        // restore learning rate to match saved epoch
        network.nextLearningate();
 
        // apply hidden weights
        for (int l = 0; l < s.hiddenLayerSize; l++) {
            HiddenLayer layer = (HiddenLayer) network.getLayer(l + 1);
            Neuron[] neurons = layer.getNeurons();
            int prevSize = (l == 0) ? 784 : s.numHiddenLayers[l - 1];
            for (int n = 0; n < neurons.length; n++) {
                double[] weights = neurons[n].getWeights();
                for (int w = 0; w < prevSize; w++) weights[w] = s.hiddenWeights[l][n][w];
                neurons[n].setWeights(weights);
                neurons[n].setBias(s.hiddenWeights[l][n][prevSize]);
            }
            layer.setNeurons(neurons);
            network.setLayer(l + 1, layer);
        }
 
        // apply output weights
        OutputLayer outputLayer = (OutputLayer) network.getLayer(s.hiddenLayerSize + 1);
        Neuron[] outNeurons = outputLayer.getNeurons();
        int lastHidden = s.numHiddenLayers[s.hiddenLayerSize - 1];
        for (int n = 0; n < outNeurons.length; n++) {
            double[] weights = outNeurons[n].getWeights();
            for (int w = 0; w < lastHidden; w++) weights[w] = s.outputWeights[n][w];
            outNeurons[n].setWeights(weights);
            outNeurons[n].setBias(s.outputWeights[n][lastHidden]);
        }
        outputLayer.setNeurons(outNeurons);
        network.setLayer(s.hiddenLayerSize + 1, outputLayer);
    }
}

