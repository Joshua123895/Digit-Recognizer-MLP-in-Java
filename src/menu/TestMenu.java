package menu;

import mnist.MNISTManager;
import neuralNetwork.NeuralNetwork;
import tool.ConsoleHandler;
import tool.InputHandler;

public class TestMenu {
	NeuralNetwork network;
	MNISTManager testData;
	InputHandler scan;
//	MouseTracker mouse = new MouseTracker();
	final int lenShade = 24;
	
	public TestMenu(NeuralNetwork network, MNISTManager source, InputHandler scan) {
		this.network = network;
		this.testData = source;
		this.scan = scan;
	}
	
	private void drawImage(double []image) {
		System.out.println(" +--------------------------------------------------------+");
		for (int i = 0; i < 28; i++) {
			System.out.print(" |");
			for (int j = 0; j < 28; j++) {
				int bit = (int) (image[i*28 + j] * lenShade);
				bit -= bit / lenShade; // wont go >= len, not out of bound
				ConsoleHandler.changeColour(bit);
				System.out.print("  ");
			}
			ConsoleHandler.resetColour();
			System.out.println("|");
		}
		System.out.println(" +--------------------------------------------------------+");
	}
	
	private int checkImage(double []image) {
		double[] result = network.feedForward(image);
    	double max = result[0];
    	int predicted = 0;
    	for (int a = 1; a < 10; a++) {
    		if (max < result[a]) {
    			max = result[a];
    			predicted = a;
    		}
    	}
    	return predicted;
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
	
//	private void findIndex(int []mouseXY, int []xy1, int []xy2, int []gridXY) {
//		for (int i = 0; i < 2; i++) {
//			gridXY[i] = (mouseXY[i] - xy1[i]) * 28 / (xy2[i] - xy1[i]);;
//		}
//	}
//	
//	private boolean paintGrid(double []grid, int []gridXY) {
//		boolean left = (gridXY[0] > 0);
//		boolean right = (gridXY[0] < 27);
//		boolean up = (gridXY[1] > 0);
//		boolean down = (gridXY[1] < 27);
//		if (!left || !right || !up || !down) return false;
//		
//		int index = gridXY[1] * 28 + gridXY[0];
//		
//	    grid[index] = Math.min(grid[index] + 0.4, 1.0);
//	    
//	    grid[index - 1] = Math.min(grid[index - 1] + 0.3, 1.0);
//	    grid[index + 1] = Math.min(grid[index + 1] + 0.3, 1.0);
//	    grid[index - 28] = Math.min(grid[index - 28] + 0.3, 1.0);
//	    grid[index + 28] = Math.min(grid[index + 28] + 0.3, 1.0);
//
//	    grid[index - 1 - 28] = Math.min(grid[index - 1 - 28] + 0.15, 1.0);
//	    grid[index + 1 - 28] = Math.min(grid[index + 1 - 28] + 0.15, 1.0);
//	    grid[index + 1 + 28] = Math.min(grid[index + 1 + 28] + 0.15, 1.0);
//	    grid[index - 1 + 28] = Math.min(grid[index - 1 + 28] + 0.15, 1.0);
//	    
//	    return true;
//	}
//	
//	public void drawTest() {
//		ConsoleHandler.clear();
//		ConsoleHandler.hideCursor();
//	    int []xy1 = new int[2];
//	    int []xy2 = new int[2];
//		
//	    // test drawing
//        ConsoleHandler.changeColour(23);
//        System.out.print("  ");
//        ConsoleHandler.changeColour(15);
//        System.out.print("  ");
//        ConsoleHandler.resetColour();
//        System.out.println();
//        ConsoleHandler.changeColour(15);
//        System.out.print("  ");
//        ConsoleHandler.changeColour(23);
//        System.out.print("  ");
//        ConsoleHandler.resetColour();
//
//        for (int i = 1; i < 28; i++) {
//            System.out.println();
//        }
//        
//        for (int i = 0; i < 28; i++) {
//            System.out.print("  ");
//        }
//        ConsoleHandler.changeColour(23);
//        System.out.print("  ");
//        ConsoleHandler.changeColour(15);
//        System.out.print("  ");
//        ConsoleHandler.resetColour();
//        System.out.println("");
//
//        for (int i = 0; i < 28; i++) {
//            System.out.print("  ");
//        }
//        
//        ConsoleHandler.changeColour(15);
//        System.out.print("  ");
//        ConsoleHandler.changeColour(23);
//        System.out.print("  ");
//        ConsoleHandler.resetColour();
//	    
//	    System.out.print("click in order: top left corner");
//	    
//	    xy1 = mouse.click(50);
//	    
//	    System.out.println(", then bottom right corner");
//
//	    for (int i = 0; i < 2; i++) {
//	    	System.out.println(xy1[i] + " " + xy2[i]);
//	    }
//	    
//	    xy2 = mouse.click(50);
//	    
//	    for (int i = 0; i < 2; i++) {
//	    	System.out.println(xy1[i] + " " + xy2[i]);
//	    }
//	    
//        double []grid = new double[784];
//        
//        ConsoleHandler.clear();
//        drawImage(grid);
//        System.out.println("Now you can draw with just ONE stroke!");
//        
//        int []paintedXY = new int[2];
//        int []gridXY = new int[2];
//        int []mouseXY = new int[2];
//        
//        mouse.click(50);
//        mouse.waitToRelease(50);
//        while (!mouse.isClicked()) {
//    		// is drawing (no delay)
//    		mouseXY = mouse.getPosition();
//    		findIndex(mouseXY, xy1, xy2, gridXY);
//    		if (!Arrays.equals(gridXY, paintedXY)) {
//    			paintGrid(grid, gridXY);
//        		ConsoleHandler.moveCursorTo(1, 1);
//        		drawImage(grid);
//    			
//    			paintedXY = gridXY.clone();
//    		}
//    	}
//        
//	    ConsoleHandler.clear();
//	    drawImage(grid);
//        int predicted = checkImage(grid);
//        
//        System.out.println("Predicted: " + predicted);
//        ConsoleHandler.showCursor();
//        scan.enter();
//	}
}
