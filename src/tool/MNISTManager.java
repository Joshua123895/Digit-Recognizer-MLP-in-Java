package tool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class MNISTManager {
	double[][] images;
	double[][] labels;
	int size;
	
	public MNISTManager(String labelPath, String imagePath) {
		labelPath = "mnist/" + labelPath;
		imagePath = "mnist/" + imagePath;
		try {
			DataInputStream imageInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(imagePath)));
			imageInputStream.readInt(); // magic number
			int numberOfItems = imageInputStream.readInt();
			imageInputStream.readInt(); // n row
			imageInputStream.readInt(); // n col

			DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelPath)));
			labelInputStream.readInt(); // magic number
			int numberOfLabels = labelInputStream.readInt();

			if (numberOfItems != numberOfLabels) {
				System.out.println("umberOfItems != numberOfLabels");
				System.exit(1);
			}
			
			images = new double[numberOfItems][784];
			labels = new double[numberOfLabels][10];
			size = numberOfItems;

			// Read images
			for (int i = 0; i < numberOfItems; i++) {
			    for (int j = 0; j < 784; j++) {
			        images[i][j] = imageInputStream.readUnsignedByte() / 255.0;
			    }
			}

			for (int i = 0; i < numberOfLabels; i++) {
			    int label = labelInputStream.readUnsignedByte();
			    for (int j = 0; j < 10; j++) {
			        if (j == label) {
			            labels[i][j] = 1.0;
			        } else {
			            labels[i][j] = 0.0;
			        }
			    }
			}
			imageInputStream.close();
			labelInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public double[] getImages(int i) {
		return images[i];
	}

	public double[] getLabels(int i) {
		return labels[i];
	}

	public int getSize() {
		return size;
	}
}
