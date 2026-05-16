package menu;

import neuralNetwork.NeuralNetwork;
import tool.ConsoleHandler;
import tool.InputHandler;
import tool.MNISTManager;
import tool.MouseTracker;

public class TestMenu {

    NeuralNetwork network;
    MNISTManager testData;
    InputHandler scan;
    MouseTracker mouse = new MouseTracker();

    final int lenShade = 24;
    final int SIZE = 28;

    public TestMenu(NeuralNetwork network, MNISTManager source, InputHandler scan) {
        this.network = network;
        this.testData = source;
        this.scan = scan;
    }

    // =========================================================
    // FAST IMAGE DRAW
    // =========================================================

    private void drawImage(double[] image) {

        StringBuilder sb = new StringBuilder(5000);

        sb.append(" +--------------------------------------------------------+\n");

        for (int i = 0; i < SIZE; i++) {

            sb.append(" |");

            int prevBit = -1;

            for (int j = 0; j < SIZE; j++) {

                int bit = (int) (image[i * SIZE + j] * lenShade);

                if (bit >= lenShade)
                    bit = lenShade - 1;

                if (bit != prevBit) {
                    sb.append(ConsoleHandler.getColourCode(bit));
                    prevBit = bit;
                }

                sb.append("  ");
            }

            sb.append(ConsoleHandler.RESET);
            sb.append("|\n");
        }

        sb.append(" +--------------------------------------------------------+\n");

        System.out.print(sb);
        System.out.flush();
    }
    
	public void test() {
		ConsoleHandler.clear();
		System.out.print("Test accuracy: Loading...");
		
		int correct = 0;
        for (int i = 0; i < testData.getSize(); i++) {
        	double []image = testData.getImages(i);
        	double []label = testData.getLabels(i);
        	if (label[checkImage(image)] > 0.0) {
        		correct++;
        	}
        }
        ConsoleHandler.clear();
        System.out.printf("Test accuracy: %.3f%%\n", (double) correct * 100.0/testData.getSize());
        scan.enter();
        int index = 0;
        while (true) {
        	ConsoleHandler.clear();
        	index = scan.intInput("Test from 0 to 9.999 [-1 to exit]: ", -1, 9_999);
        	if (index == -1) break;

        	double []image = testData.getImages(index);
        	double []label = testData.getLabels(index);
        	
        	drawImage(testData.getImages(index));
        	
        	int expected = 0;
        	double max = label[0];
        	for (int i = 1; i < label.length; i++) {
        		if (max < label[i]) {
        			max = label[i];
        			expected = i;
        		}
        	}
        	
            int predicted = checkImage(image);
            System.out.println("Expected: " + expected);
            System.out.println("Predicted: " + predicted);
            scan.enter();
    	}
	}

    // =========================================================
    // FAST CHECK IMAGE
    // =========================================================

    private int checkImage(double[] image) {

        double[] result = network.feedForward(image);

        double max = result[0];
        int predicted = 0;

        for (int i = 1; i < 10; i++) {

            if (result[i] > max) {
                max = result[i];
                predicted = i;
            }
        }

        return predicted;
    }

    // =========================================================
    // FAST PAINT
    // =========================================================

    private void add(double[] grid, int idx, double val) {

    	double v = grid[idx] + val;
        grid[idx] = v > 1.0f ? 1.0f : v;
    }

    private boolean paintGrid(double[] grid, int gx, int gy) {

        if (gx <= 0 || gx >= 27 || gy <= 0 || gy >= 27)
            return false;

        int index = gy * SIZE + gx;

        add(grid, index, 0.4f);

        add(grid, index - 1, 0.3f);
        add(grid, index + 1, 0.3f);
        add(grid, index - SIZE, 0.3f);
        add(grid, index + SIZE, 0.3f);

        add(grid, index - 29, 0.15f);
        add(grid, index - 27, 0.15f);
        add(grid, index + 27, 0.15f);
        add(grid, index + 29, 0.15f);

        return true;
    }

    // =========================================================
    // DRAW TEST
    // =========================================================

    public void drawTest() {

        ConsoleHandler.clear();
        ConsoleHandler.hideCursor();

        Runtime.getRuntime().addShutdownHook(
            new Thread(ConsoleHandler::showCursor)
        );

        mouse.enableMouseOnly();

        int[] xy1 = new int[2];
        int[] xy2 = new int[2];

        // calibration markers
        ConsoleHandler.changeColour(23);
        System.out.print("  ");
        ConsoleHandler.changeColour(15);
        System.out.print("  ");
        ConsoleHandler.resetColour();
        System.out.println();

        ConsoleHandler.changeColour(15);
        System.out.print("  ");
        ConsoleHandler.changeColour(23);
        System.out.print("  ");
        ConsoleHandler.resetColour();

        for (int i = 1; i < 28; i++) {
            System.out.println();
        }

        for (int i = 0; i < 28; i++) {
            System.out.print("  ");
        }

        ConsoleHandler.changeColour(23);
        System.out.print("  ");
        ConsoleHandler.changeColour(15);
        System.out.print("  ");
        ConsoleHandler.resetColour();
        System.out.println();

        for (int i = 0; i < 28; i++) {
            System.out.print("  ");
        }

        ConsoleHandler.changeColour(15);
        System.out.print("  ");
        ConsoleHandler.changeColour(23);
        System.out.print("  ");
        ConsoleHandler.resetColour();

        System.out.print("click in order: top left corner");

        mouse.click(50);
        mouse.getPosition(xy1);

        System.out.println(", then bottom right corner");

        mouse.click(50);
        mouse.getPosition(xy2);

        // =========================================================
        // PRECOMPUTED SCALE
        // =========================================================

        double scaleX = 28.0 / (xy2[0] - xy1[0]);
        double scaleY = 28.0 / (xy2[1] - xy1[1]);

        double[] grid = new double[784];

        ConsoleHandler.clear();

        drawImage(grid);

        System.out.println("Now you can draw with just ONE stroke!");

        int paintedX = -1;
        int paintedY = -1;

        int[] mouseXY = new int[2];

        mouse.click(50);

        mouse.enableMouseOnly();

        while (mouse.isClicked()) {

            mouse.getPosition(mouseXY);

            int gx = (int) ((mouseXY[0] - xy1[0]) * scaleX);
            int gy = (int) ((mouseXY[1] - xy1[1]) * scaleY);

            if (gx == paintedX && gy == paintedY)
                continue;

            if (!paintGrid(grid, gx, gy))
                continue;

            paintedX = gx;
            paintedY = gy;

            ConsoleHandler.moveCursorTo(1, 1);

            drawImage(grid);
        }

        ConsoleHandler.clear();

        drawImage(grid);

        int predicted = checkImage(grid);

        System.out.println("Predicted: " + predicted);

        ConsoleHandler.showCursor();

        scan.enter();
    }
}