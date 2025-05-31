package tool;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

interface MouseLibrary extends Library {
    MouseLibrary INSTANCE = (MouseLibrary) Native.load("mouse_tracker", MouseLibrary.class);

    // Declare the C function signatures
    void get_mouse_position(IntByReference x, IntByReference y);
    int is_mouse_clicked();
}

public class MouseTracker {	
	IntByReference x = new IntByReference();
	IntByReference y = new IntByReference();
	int []wrapper = new int[2];
		
	public boolean isClicked() {
		return (MouseLibrary.INSTANCE.is_mouse_clicked() != 0);
	}
	
	public int[] getPosition() {
		MouseLibrary.INSTANCE.get_mouse_position(x, y);
		wrapper[0] = x.getValue();
		wrapper[1] = y.getValue();
		return wrapper.clone();
	}
	
	public void waitToPress(long ms) {
		while (!isClicked()) {
			ConsoleHandler.sleep(ms);
		}
	}
	
	public void waitToRelease(long ms) {
		while (isClicked()) {
			ConsoleHandler.sleep(ms);
		}
	}
	
	public int[] click(long ms) {
		waitToRelease(ms);
		waitToPress(ms);
		return getPosition();
	}
}
