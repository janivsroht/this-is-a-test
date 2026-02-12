import java.io.*;
import java.util.Scanner;

public class RestaurantManagementSystem {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        FileManager.initializeSystem();

        while (true) {
            System.out.println("\n=================================");
            System.out.println("      MAIN MENU      ");
            System.out.println("=================================");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Select Option: ");

            String choice = scanner.nextLine();

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

    private static void handleLogin() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.println("\n--- LOGIN ---");
            System.out.print("Enter Phone/ID: ");
            String id = scanner.nextLine();

            // --- CHANGED PROMPT ---
            System.out.print("Enter Password: ");
            String pass = scanner.nextLine();

            // --- RE-ENABLED VALIDATION ---
            if (pass.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                // We continue here so it counts as an attempt or just restarts the loop
                continue;
            }

            String[] userData = FileManager.authenticateUser(id, pass);

            if (userData != null) {
                String role = userData[0];
                String name = userData[2];
                User currentUser = null;

                switch (role) {
                    case "ADMIN":
                        currentUser = new Admin(id, name);
                        break;
                    case "STAFF":
                        currentUser = new Staff(id, name);
                        break;
                    case "CUSTOMER":
                        currentUser = new Customer(id, name);
                        break;
                }

                if (currentUser != null) {
                    System.out.println("Login Successful! Welcome " + name);
                    currentUser.showMenu();
                    return;
                }
            } else {
                System.out.println("Login Failed. Invalid Credentials.");
                attempts++;
            }
        }
        System.out.println("Maximum attempts reached.");
    }

    private static void handleCreateAccount() {
        System.out.println("\n--- CREATE ACCOUNT ---");
        System.out.println("1. Create Customer Account");
        System.out.println("2. Create Staff Account");
        System.out.println("3. Previous Page");
        System.out.print("Select: ");
        String ch = scanner.nextLine();

        if (ch.equals("1")) {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Phone (10 digits): ");
            String phone = scanner.nextLine();

            // --- ADDED PASSWORD PROMPT HERE ---
            System.out.print("Set Password: ");
            String pass = scanner.nextLine();

            if (phone.length() != 10) {
                System.out.println("Invalid Phone Number.");
                return;
            }
            if (pass.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
                return;
            }
            if (FileManager.userExists(phone)) {
                System.out.println("Account already exists.");
                return;
            }

            // Pass the new password to the file manager
            FileManager.saveUser("CUSTOMER", phone, name, pass);
            System.out.println("Account created successfully.");

        } else if (ch.equals("2")) {
            System.out.print("Enter Staff Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Staff ID: ");
            String id = scanner.nextLine();
            System.out.print("Set Password: ");
            String pass = scanner.nextLine();

            if (FileManager.userExists(id)) {
                System.out.println("User ID already exists.");
                return;
            }
            FileManager.saveUser("STAFF", id, name, pass);
            System.out.println("Staff account created successfully.");
        }
    }
}

// ==========================================
// DOMAIN CLASSES
// ==========================================

class MenuItem {
    private String id;
    private String name;
    private double price;

    public MenuItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String toFileString() {
        return id + "|" + name + "|" + price;
    }

    public String toDisplayString() {
        return "ID: " + id + " | " + name + " | $" + price;
    }
}

abstract class User {
    protected String id;
    protected String name;
    protected Scanner scanner = new Scanner(System.in);

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract void showMenu();
}

class Admin extends User {
    public Admin(String id, String name) {
        super(id, name);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Create Staff Account");
            System.out.println("2. Delete Staff Account");
            System.out.println("3. Enter/Edit Restaurant Details");
            System.out.println("4. Logout");
            System.out.print("Select: ");
            String ch = scanner.nextLine();

            switch (ch) {
                case "1":
                    createStaff();
                    break;
                case "2":
                    deleteStaff();
                    break;
                case "3":
                    editDetails();
                    break;
                case "4":
                    return;
                default:
                    break;
            }
        }
    }

    private void createStaff() {
        System.out.print("Staff Name: ");
        String sName = scanner.nextLine();
        System.out.print("Staff ID: ");
        String sId = scanner.nextLine();
        System.out.print("Password: ");
        String sPass = scanner.nextLine();

        if (FileManager.userExists(sId)) {
            System.out.println("ID already exists.");
        } else {
            FileManager.saveUser("STAFF", sId, sName, sPass);
            System.out.println("Staff created.");
        }
    }

    private void deleteStaff() {
        System.out.print("Enter Staff ID to delete: ");
        String delId = scanner.nextLine();
        FileManager.deleteUser(delId);
    }

    private void editDetails() {
        System.out.print("Restaurant Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String addr = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        FileManager.overwriteFile(FileManager.FILE_DETAILS, name + "|" + addr + "|" + phone);
        System.out.println("Details saved.");
    }
}

class Staff extends User {
    public Staff(String id, String name) {
        super(id, name);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- STAFF MENU ---");
            System.out.println("1. View Menu");
            System.out.println("2. Edit Items");
            System.out.println("3. Add New Items");
            System.out.println("4. Logout");
            System.out.print("Select: ");
            String ch = scanner.nextLine();

            switch (ch) {
                case "1":
                    viewMenu();
                    break;
                case "2":
                    editItem();
                    break;
                case "3":
                    addItem();
                    break;
                case "4":
                    return;
                default:
                    break;
            }
        }
    }

    private void viewMenu() {
        MenuItem[] menu = FileManager.loadMenu();
        if (menu.length == 0)
            System.out.println("Menu is empty.");
        for (int i = 0; i < menu.length; i++) {
            System.out.println(menu[i].toDisplayString());
        }
    }

    private void addItem() {
        System.out.print("Enter Item Name: ");
        String iName = scanner.nextLine();
        System.out.print("Enter Price: ");
        try {
            double price = Double.parseDouble(scanner.nextLine());
            String iId = String.valueOf(System.currentTimeMillis() % 10000);
            FileManager.saveMenuItem(new MenuItem(iId, iName, price));
            System.out.println("Item added.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void editItem() {
        viewMenu();
        System.out.print("Enter Item ID to Edit: ");
        String targetId = scanner.nextLine();

        MenuItem[] menu = FileManager.loadMenu();
        boolean found = false;

        // Create a temporary array of Strings to hold the new file content
        String[] newContent = new String[menu.length];

        for (int i = 0; i < menu.length; i++) {
            MenuItem m = menu[i];
            if (m.getId().equals(targetId)) {
                found = true;
                System.out.println("Editing: " + m.getName());
                System.out.println("1. Edit Name\n2. Edit Price");
                String opt = scanner.nextLine();

                String newName = m.getName();
                double newPrice = m.getPrice();

                if (opt.equals("1")) {
                    System.out.print("New Name: ");
                    newName = scanner.nextLine();
                } else {
                    System.out.print("New Price: ");
                    newPrice = Double.parseDouble(scanner.nextLine());
                }
                newContent[i] = new MenuItem(targetId, newName, newPrice).toFileString();
                System.out.println("Item updated.");
            } else {
                newContent[i] = m.toFileString();
            }
        }

        if (found)
            FileManager.writeAllLines(FileManager.FILE_MENU, newContent);
        else
            System.out.println("Item ID not found.");
    }
}

class Customer extends User {
    public Customer(String id, String name) {
        super(id, name);
    }

    public void showMenu() {
        if (!FileManager.isRestaurantReady()) {
            System.out.println("Restaurant setup incomplete. Please contact admin.");
            return;
        }

        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. View Menu / Order");
            System.out.println("2. View Order Status");
            System.out.println("3. Logout");
            System.out.print("Select: ");
            String ch = scanner.nextLine();

            if (ch.equals("1"))
                placeOrder();
            else if (ch.equals("2"))
                viewStatus();
            else if (ch.equals("3"))
                return;
        }
    }

    private void placeOrder() {
        MenuItem[] menu = FileManager.loadMenu();
        for (MenuItem menu1 : menu) {
            System.out.println(menu1.toDisplayString());
        }

        System.out.print("Enter Item IDs (comma separated, e.g. 101,102): ");
        String input = scanner.nextLine();
        String[] ids = input.split(",");

        String validIds = "";
        boolean first = true;

        for (int i = 0; i < ids.length; i++) {
            String cleanId = ids[i].trim();
            // Check if ID exists in menu
            boolean exists = false;
            for (int j = 0; j < menu.length; j++) {
                if (menu[j].getId().equals(cleanId))
                    exists = true;
            }

            if (exists) {
                if (!first)
                    validIds += ",";
                validIds += cleanId;
                first = false;
            }
        }

        if (!validIds.isEmpty()) {
            String orderRecord = this.id + "|" + validIds + "|ORDERED";
            FileManager.saveOrder(orderRecord);
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("No valid items selected.");
        }
    }

    private void viewStatus() {
        String[] orders = FileManager.getOrdersForUser(this.id);
        if (orders.length == 0) {
            System.out.println("No items are in order.");
        } else {
            System.out.println("\n--- ORDER STATUS ---");
            MenuItem[] menu = FileManager.loadMenu();

            for (int k = 0; k < orders.length; k++) {
                String[] parts = orders[k].split("\\|"); // ID|Items|Status
                String[] itemIds = parts[1].split(",");
                String itemNames = "";

                for (int i = 0; i < itemIds.length; i++) {
                    String iId = itemIds[i];
                    for (int j = 0; j < menu.length; j++) {
                        if (menu[j].getId().equals(iId))
                            itemNames += menu[j].getName() + ", ";
                    }
                }
                System.out.println("Items: " + itemNames + " | Status: " + parts[2]);
            }
        }
    }
}

// FILE HANDLING UTILITY

class FileManager {
    static final String FILE_USERS = "users.txt";
    static final String FILE_MENU = "menu.txt";
    static final String FILE_ORDERS = "orders.txt";
    static final String FILE_DETAILS = "details.txt";

    public static void initializeSystem() {
        try {
            createFile(FILE_USERS);
            createFile(FILE_MENU);
            createFile(FILE_ORDERS);
            createFile(FILE_DETAILS);

            // Check if users file is empty
            File f = new File(FILE_USERS);
            if (f.length() == 0) {
                saveUser("ADMIN", "admin", "System Admin", "admin123");
            }
        } catch (IOException e) {
            System.out.println("System Init Error: " + e.getMessage());
        }
    }

    private static void createFile(String name) throws IOException {
        File f = new File(name);
        if (!f.exists())
            f.createNewFile();
    }

    // --- READ HELPER (ARRAY BASED) ---
    public static String[] readLines(String fileName) {
        int count = 0;
        // Pass 1: Count lines
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null)
                count++;
        } catch (IOException e) {
            return new String[0];
        }

        String[] lines = new String[count];
        // Pass 2: Read lines
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                lines[i++] = line;
            }
        } catch (IOException e) {
        }

        // Filter out nulls if any issue occurred
        int actualCount = 0;
        for (int k = 0; k < lines.length; k++)
            if (lines[k] != null && !lines[k].trim().isEmpty())
                actualCount++;

        String[] result = new String[actualCount];
        int idx = 0;
        for (int k = 0; k < lines.length; k++) {
            if (lines[k] != null && !lines[k].trim().isEmpty())
                result[idx++] = lines[k];
        }
        return result;
    }

    public static void writeAllLines(String fileName, String[] lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] != null) {
                    bw.write(lines[i]);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendLine(String fileName, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overwriteFile(String fileName, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- USER METHODS ---
    public static void saveUser(String type, String id, String name, String pass) {
        appendLine(FILE_USERS, type + "|" + id + "|" + name + "|" + pass);
    }

    public static boolean userExists(String id) {
        String[] lines = readLines(FILE_USERS);
        for (int i = 0; i < lines.length; i++) {
            // FIX: Added -1
            String[] parts = lines[i].split("\\|", -1);
            if (parts.length > 1 && parts[1].equals(id))
                return true;
        }
        return false;
    }

    public static String[] authenticateUser(String id, String pass) {
        String[] lines = readLines(FILE_USERS);
        for (int i = 0; i < lines.length; i++) {
            // FIX: Added -1 limit to split to keep empty passwords
            String[] parts = lines[i].split("\\|", -1);

            if (parts.length < 4)
                continue;

            if (parts[1].equals(id)) {
                if (parts[3].equals(pass))
                    return parts;
            }
        }
        return null;
    }

    public static void deleteUser(String id) {
        String[] lines = readLines(FILE_USERS);
        // We write back all lines EXCEPT the one with the ID
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_USERS))) {
            boolean deleted = false;
            for (int i = 0; i < lines.length; i++) {
                String[] parts = lines[i].split("\\|");
                if (parts.length > 1 && parts[1].equals(id)) {
                    deleted = true; // Skip writing this line
                } else {
                    bw.write(lines[i]);
                    bw.newLine();
                }
            }
            if (deleted)
                System.out.println("User deleted.");
            else
                System.out.println("User ID not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- MENU METHODS ---
    public static void saveMenuItem(MenuItem item) {
        appendLine(FILE_MENU, item.toFileString());
    }

    public static MenuItem[] loadMenu() {
        String[] lines = readLines(FILE_MENU);
        MenuItem[] items = new MenuItem[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split("\\|");
            items[i] = new MenuItem(parts[0], parts[1], Double.parseDouble(parts[2]));
        }
        return items;
    }

    // --- ORDER METHODS ---
    public static void saveOrder(String line) {
        appendLine(FILE_ORDERS, line);
    }

    public static String[] getOrdersForUser(String userId) {
        String[] allOrders = readLines(FILE_ORDERS);
        int count = 0;
        // Count matches
        for (int i = 0; i < allOrders.length; i++) {
            if (allOrders[i].startsWith(userId + "|"))
                count++;
        }

        String[] userOrders = new String[count];
        int idx = 0;
        for (int i = 0; i < allOrders.length; i++) {
            if (allOrders[i].startsWith(userId + "|"))
                userOrders[idx++] = allOrders[i];
        }
        return userOrders;
    }

    public static boolean isRestaurantReady() {
        File f1 = new File(FILE_MENU);
        File f2 = new File(FILE_DETAILS);
        return f1.length() > 0 && f2.length() > 0;
    }
}