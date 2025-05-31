package tool;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class ConsoleHandler {	
    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void fullscreen() {
    	try {
	        Robot robot = new Robot();

            if (System.getProperty("os.name").contains("Windows")) {
            	robot.keyPress(KeyEvent.VK_ALT);       // Press Alt
            	robot.keyPress(KeyEvent.VK_ENTER);     // Press Enter
            	
            	robot.keyRelease(KeyEvent.VK_ENTER);   // Release Enter
            	robot.keyRelease(KeyEvent.VK_ALT);     // Release Alt
            } else {
            	robot.keyPress(KeyEvent.VK_CONTROL);   // Press Ctrl key
            	robot.keyPress(KeyEvent.VK_SHIFT);     // Press Shift key
            	robot.keyPress(KeyEvent.VK_F);         // Press F key
            	
            	robot.keyRelease(KeyEvent.VK_F);       // Release F key
            	robot.keyRelease(KeyEvent.VK_SHIFT);   // Release Shift key
            	robot.keyRelease(KeyEvent.VK_CONTROL); // Release Ctrl key
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

	public static void moveCursorTo(int x, int y) {
	    System.out.printf("\033[%d;%dH", y, x);
	}
	
	public static void changeColour(int a) {
	    System.out.printf("\033[48;5;%dm", a + 232);
	}
	
	public static void resetColour() {
	    System.out.printf("\033[0m");
	}
	
    public static void hideCursor() {
        System.out.print("\033[?25l");
    }

    public static void showCursor() {
        System.out.print("\033[?25h");
    }
    
    public static void sleep(long ms) {
    	try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
