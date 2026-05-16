package tool;

public class TestMouse {
	public void test() {
		MouseTracker mouse = new MouseTracker();
		System.out.println("click once");
		mouse.click(50);

		System.out.println("then click once more");
		mouse.click(50);
		
		System.out.println("Now draw");
		mouse.waitToRelease(50);
		System.out.println("Mouse released");
		mouse.waitToPress(50);
		while (mouse.isClicked()) {
			System.out.println("drawing...");
		}
		System.out.println("done");
		
	}
	public static void main(String[] args) {
		TestMouse t = new TestMouse();
		t.test();
	}
}
