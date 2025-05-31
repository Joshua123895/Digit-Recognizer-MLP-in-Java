package tool;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
	static Scanner scan = new Scanner(System.in);
	public int intInput(String text, int min, int max) {
		int result = 0;
		while (true) {
			System.out.print(text);
			try {
				result = Integer.parseInt(scan.nextLine());
			} catch (Exception e) {
				System.out.println("Input must be integer");
				continue;
			}
			if (result < min || result > max) {
				System.out.println("Input must be between " + min + " and " + max);
				continue;
			}
			break;
		}
		return result;
	}
	
	public int intInput(String text) {
		return intInput(text, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public double doubleInput(String text, double min, double max) {
		double result = 0;
		while (true) {
			System.out.print(text);
			try {
				result = Double.parseDouble(scan.nextLine());
			} catch (Exception e) {
				System.out.println("Input must be double");
				continue;
			}
			if (result < min || result > max) {
				System.out.println("Input must be between " + min + " and " + max);
				continue;
			}
			break;
		}
		return result;
	}
	
	public double doubleInput(String text) {
		return doubleInput(text, Double.MIN_VALUE, Double.MAX_VALUE);
	}
	
	public String stringInput(String text) {
		System.out.print(text);
		return scan.nextLine().trim();
	}
	
	public String stringInput(String text, String characters, int max) {
        String result = "";
        
        while (true) {
            System.out.print(text);
            result = scan.nextLine().trim();

            String regex = "[^" + Pattern.quote(characters) + "]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(result);

            if (!matcher.matches()) {
                System.out.println("Input must not include these: " + characters);
                continue;
            }

            if (result.length() > max) {
                System.out.println("Input must not be longer than " + max);
                continue;
            }

            break;
        }
        return result;
    }
	
	public void enter() {
		System.out.println("Press enter to continue...");
		scan.nextLine();
	}
	
	public void close() {
		scan.close();
	}
}
