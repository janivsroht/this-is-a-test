import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class RMS {

    static Scanner sc = new Scanner(System.in);
    
    public class Customer{
        String name;
        long phoneNumber;
        String id;

        void Customer(String name, long phoneNumber, String id){
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.id = id;
        }
    }

    

    public static void main(String[] args) {
        initialize();

        while (true) {
            System.out.println("\n=================================");
            System.out.println("      MAIN MENU      ");
            System.out.println("=================================");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Select Option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleCreateAccount();
                    break;
                case "3":
                    System.out.println("Exiting System. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }

    }

    static String myFiles[] = {"customers.txt", "staffs.txt", "menu.txt"};

    public static void initialize(){
        for (String name : myFiles) {
            Path path = Paths.get(name);
            try {
                if (Files.notExists(path)) {
                    Files.createFile(path);
                    System.out.println("Initialized: " + name);
                } else {
                    System.out.println("Skipped: " + name + " (already exists)");
                }
            } catch (IOException e) {
                System.err.println("Could not initialize " + name + ": " + e.getMessage());
            }
        }
    }

    public static void handleLogin(){
        System.out.println("\n------Login------\n");
        System.out.print("Enter your Phone Number: ");
        String phNumber = sc.next();
        System.out.println("Enter your password(leave empty if customer): ");
        String pass = sc.next();

        if (pass.trim().isEmpty()) pass = "null";

        for (String file: myFiles){
            String[] lines = readLines(file);
        }
    }
}