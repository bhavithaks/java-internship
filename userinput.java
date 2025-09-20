import java.util.Scanner;

public class JavaTask {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the first integer: ");
        int num1 = sc.nextInt();

        System.out.print("Enter the second integer: ");
        int num2 = sc.nextInt();

        System.out.print("Enter a floating-point number: ");
        double floatNum = sc.nextDouble();

        
        System.out.print("Enter a single character: ");
        char ch = sc.next().charAt(0);

    
        System.out.print("Enter a boolean value (true/false): ");
        boolean boolVal = sc.nextBoolean();

        
        sc.nextLine(); 
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

    
        System.out.println("\nSum of " + num1 + " and " + num2 + " is: " + (num1 + num2));
        System.out.println("Difference between " + num1 + " and " + num2 + " is: " + (num1 - num2));
        System.out.println("Product of " + num1 + " and " + num2 + " is: " + (num1 * num2));
        System.out.println("Quotient of " + num1 + " / " + num2 + " is: " + (num1 / num2) + " and remainder is: " + (num1 % num2));

        
        System.out.println(floatNum + " multiplied by 2 is: " + (floatNum * 2));
        System.out.println("Square of " + floatNum + " is: " + (floatNum * floatNum));

        
        System.out.println("The next character after '" + ch + "' is: " + (char)(ch + 1));
        System.out.println("The previous character before '" + ch + "' is: " + (char)(ch - 1));

    
        System.out.println("The opposite of " + boolVal + " is: " + !boolVal);
        System.out.println("Was the original value true? " + (boolVal ? "Yes" : "No"));


        System.out.println("HELLO, " + name.toUpperCase() + "!");
        
        sc.close();
    }

}
