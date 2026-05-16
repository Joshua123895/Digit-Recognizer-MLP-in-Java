package tool;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

interface MouseLibrary extends Library {
    MouseLibrary INSTANCE = (MouseLibrary) Native.load("mouse_tracker", MouseLibrary.class);

    // Declare the C function signatures
    void get_mouse_position(IntByReference x, IntByReference y);
    int is_mouse_clicked();
    void enableMouseOnly();
}

public class MouseTracker {	
	IntByReference x = new IntByReference();
	IntByReference y = new IntByReference();
	
	public void enableMouseOnly() {
		MouseLibrary.INSTANCE.enableMouseOnly();
	}
		
	public boolean isClicked() {
		return (MouseLibrary.INSTANCE.is_mouse_clicked() != 0);
	}
	
	public void getPosition(int[] out) {
		MouseLibrary.INSTANCE.get_mouse_position(x, y);
		out[0] = x.getValue();
		out[1] = y.getValue();
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
	
	public void click(long ms) {
		waitToRelease(ms);
		waitToPress(ms);
	}
}
