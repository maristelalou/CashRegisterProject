import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    static Scanner input = new Scanner(System.in);

    static ArrayList<String> uname = new ArrayList<String>();
    static ArrayList<String> pass = new ArrayList<>();

    static ArrayList<String> drinkN = new ArrayList<>();
    static ArrayList<String> drinkS = new ArrayList<>();
    static ArrayList<Double> drinkP = new ArrayList<>();
    static ArrayList<Integer> drinkQ = new ArrayList<>();

    static String customName = "";
    static String hereOrGo = "";
    static int choice = 0;

    static String loggedInUser = "";

    private static void signUp(ArrayList<String> uname, ArrayList<String> pass) {
        String username;
        String password;

        while (true) {
            System.out.print("Enter username (alphanumeric & 5–15 characters): ");
            username = input.nextLine();

            Pattern pattern = Pattern.compile("^[A-Za-z0-9]{5,15}$");
            Matcher matcher = pattern.matcher(username);

            if (!matcher.matches()) {
                System.out.println("Try another username. It must contain 5-15 alphanumeric characters.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter password (8-20 characters, at least one uppercase letter, and one number): ");
            password = input.nextLine();

            Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$");
            Matcher matcher = pattern.matcher(password);

            if (!matcher.matches()) {
                System.out.println("Invalid password. Must be 8-20 characters long, contain at least one uppercase letter & one number.");
                continue;
            }

            uname.add(username);
            pass.add(password);
            System.out.println("\nSignup successful!\n");
            break;
        }
    }

    private static boolean logIn(ArrayList<String> uname, ArrayList<String> pass) {
        
        while (true) {
            System.out.print("Enter username: ");
            String user = input.nextLine();

            for (int i = 0; i < uname.size(); i++) {
                if (uname.get(i).equals(user)) {
                    System.out.print("Enter password: ");
                    String pw = input.nextLine();

                    if (pass.get(i).equals(pw)) {
                        loggedInUser = user;
                        System.out.println("\nLogged in successfully!\n");
                        return true;
                    } else {
                        System.out.println("Incorrect password. Please try again.");
                        return false;
                    }
                }
            }
            System.out.println("Incorrect username. Please try again.");
        }
    }

    public static void addDrink(boolean isFirstOrder) {
        if (isFirstOrder) {
            System.out.print("What's your name? : ");
            customName = input.nextLine();

            System.out.print("For here or To go? : ");
            hereOrGo = input.nextLine();
        }

        boolean continueLoop = true;

        while (continueLoop) {
            System.out.print("\nEnter drink: ");
            String d1 = input.nextLine();
            drinkN.add(d1);

            System.out.print("Enter drink size (Tall, Grande, Venti): ");
            String dSize = input.nextLine();
            drinkS.add(dSize);

            double dPrice = 0;
            
            while (true) {
                try {
                    System.out.print("Enter drink price: ");
                    dPrice = Double.parseDouble(input.nextLine());
                    if (dPrice < 0) {
                        System.out.println("Price cannot be negative. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price. Please enter a number.");
                }
            }
            drinkP.add(dPrice);

            int dQuan = 0;
            
            while (true) {
                try {
                    System.out.print("Enter drink quantity: ");
                    dQuan = Integer.parseInt(input.nextLine());
                    if (dQuan <= 0) {
                        System.out.println("Quantity must be greater than zero. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity. Please enter a whole number.");
                }
            }
            drinkQ.add(dQuan);

            System.out.println("\nDrink/s successfully ordered!\n");

            System.out.print("Do you want to order another drink? (y/n): ");
            if (input.nextLine().equalsIgnoreCase("n")) {
                continueLoop = false;
                displayDrinks();
            }
        }
    }

    public static double displayDrinks() {
        System.out.println("\n***************** Order Summary ********************\n");
        System.out.printf("%-20s %s\n", "Customer Name:", customName);
        System.out.printf("%-20s %s\n", "Order Type:", hereOrGo);
        System.out.println("\n-----------------------------------------------------");
        System.out.printf("%-20s %-10s %-10s %-10s\n", "Drink", "Size", "Price", "Quantity");
        System.out.println("-----------------------------------------------------");

        double totalPrice = 0;
        for (int i = 0; i < drinkN.size(); i++) {
            double total = drinkP.get(i) * drinkQ.get(i);
            totalPrice += total;
            System.out.printf("%-20s %-10s %-10.2f %-10d\n", drinkN.get(i), drinkS.get(i), drinkP.get(i), drinkQ.get(i));
        }

        System.out.println("-----------------------------------------------------");
        System.out.printf("%-20s %.2f\n\n", "Total:", totalPrice);
        System.out.println("****************************************************\n");

        return totalPrice;
    }

    public static void payment() {
        double totalPrice = displayDrinks();

        double pAmount = 0;
        
        while (true) {
            try {
                System.out.print("Enter payment amount: ");
                pAmount = Double.parseDouble(input.nextLine());
                if (pAmount < totalPrice) {
                    System.out.println("Insufficient payment. Please enter a valid amount.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
            }
        }

        double change = pAmount - totalPrice;
        System.out.printf("Your change is: %.2f\n", change);
        System.out.println("\nThank you for your purchase! Come again :) \n");

        logTransaction(loggedInUser, totalPrice);

        drinkN.clear();
        drinkS.clear();
        drinkP.clear();
        drinkQ.clear();
        customName = "";
        hereOrGo = "";
    }

    public static void removeOrder() {
        if (drinkN.isEmpty()) {
            System.out.println("No drinks to remove.");
            return;
        }

        System.out.println("\nYour current orders:");
        for (int i = 0; i < drinkN.size(); i++) {
            System.out.println((i + 1) + ". " + drinkN.get(i) + " (" + drinkS.get(i) + ") - Quantity: " + drinkQ.get(i));
        }

        System.out.print("Enter the number of the drink you want to remove: ");
        int choice = -1;

        while (true) {
            try {
                choice = Integer.parseInt(input.nextLine());
                if (choice < 1 || choice > drinkN.size()) {
                    System.out.println("Invalid number. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        drinkN.remove(choice - 1);
        drinkS.remove(choice - 1);
        drinkP.remove(choice - 1);
        drinkQ.remove(choice - 1);

        System.out.println("Drink removed successfully!");
    }

    public static void updateQuantity() {
        if (drinkN.isEmpty()) {
            System.out.println("No drinks to update.");
            return;
        }

        boolean editing = true;

        while (editing) {
            System.out.println("\nCurrent Orders:");
            for (int i = 0; i < drinkN.size(); i++) {
                System.out.printf("%d. %s (%s) - Qty: %d\n", i + 1, drinkN.get(i), drinkS.get(i), drinkQ.get(i));
            }
            System.out.println();

            int choice = -1;
            while (true) {
                System.out.print("Select the drink number to update quantity: ");
                try {
                    choice = input.nextInt();
                    input.nextLine();
                    if (choice < 1 || choice > drinkN.size()) {
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a number.");
                    input.nextLine();
                }
            }

            int newQty = -1;
            while (true) {
                System.out.print("Enter new quantity: ");
                try {
                    newQty = input.nextInt();
                    input.nextLine();
                    if (newQty <= 0) {
                        System.out.println("Quantity must be greater than 0. Try again.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a number.");
                    input.nextLine();
                }
            }

            drinkQ.set(choice - 1, newQty);
            System.out.println("Quantity updated successfully!\n");

            System.out.print("Do you want to update another quantity? (y/n): ");
            String ans = input.nextLine();
            if (!ans.equalsIgnoreCase("y")) {
                editing = false;
            }
        }
    }

    public static void logTransaction(String username, double total) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt", true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            writer.write("Date & Time: " + now.format(formatter));
            writer.newLine();
            writer.write("Cashier: " + username);
            writer.newLine();
            writer.write("Customer Name: " + customName);
            writer.newLine();
            writer.write("Order Type: " + hereOrGo);
            writer.newLine();
            writer.write("Items:");
            writer.newLine();

            for (int i = 0; i < drinkN.size(); i++) {
                double price = drinkP.get(i) * drinkQ.get(i);
                String itemLine = String.format(" > %s (%s) - Qty: %d - Price: %.2f",
                        drinkN.get(i), drinkS.get(i), drinkQ.get(i), price);
                writer.write(itemLine);
                writer.newLine();
            }

            writer.write(String.format("Total Price: %.2f", total));
            writer.newLine();
            writer.write("-----------------------------------------------------");
            writer.newLine();

        } catch (Exception e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }

    public static void DispTrans() {
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            System.out.println("\n---------- Transaction Logs ----------");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("---------- End of Logs ----------\n");
        } catch (Exception e) {
            System.out.println("No transaction logs found or error reading the log.");
        }
    }

    public static void main(String[] args) {
        boolean isAuthenticated = false;

        System.out.println("\n-------------------------------------------------------");
        System.out.println("             Welcome to Lour's Coffee Shop             ");
        System.out.println("                   Lipa City, Batangas                 ");
        System.out.println("-------------------------------------------------------\n");

        while (!isAuthenticated) {
            System.out.println("\n1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String option = input.nextLine();

            switch (option) {
                case "1":
                    signUp(uname, pass);
                    break;
                case "2":
                    isAuthenticated = logIn(uname, pass);
                    break;
                case "3":
                    System.out.println("Thank you for visiting Lour's Coffee Shop. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        System.out.println("\n-------------------------------------------------------");
        System.out.println("                    Coffee Menu                        ");
        System.out.println("-------------------------------------------------------");
        System.out.println("Tall - ₱150 | Grande - ₱180 | Venti - ₱200");
        System.out.println("Iced Matcha Latte");
        System.out.println("Iced White Mocha");
        System.out.println("Kapeng Barako");
        System.out.println("Iced Spanish Latte");
        System.out.println("Caramel Macchiato\n");
        System.out.println("-------------------------------------------------------");

        addDrink(true);

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n-------------------------------------------------------");
            System.out.println("Would you like to:");
            System.out.println("[1] Add Order");
            System.out.println("[2] Remove Order");
            System.out.println("[3] Update Quantity");
            System.out.println("[4] View Order Summary");
            System.out.println("[5] Payment");
            System.out.println("[6] View Transaction Logs");
            System.out.println("[7] Exit");
            System.out.print("Select: ");

            String menuChoice = input.nextLine();

            switch (menuChoice) {
                case "1":
                    addDrink(false);
                    break;
                case "2":
                    removeOrder();
                    break;
                case "3":
                    updateQuantity();
                    break;
                case "4":
                    displayDrinks();
                    break;
                case "5":
                    if (drinkN.isEmpty()) {
                        System.out.println("You have no orders. Please add drinks first.");
                    } else {
                        payment();
                    }
                    break;
                case "6":
                    DispTrans();
                    break;
                case "7":
                    System.out.println("Thank you for visiting Lour's Coffee Shop. Goodbye!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
