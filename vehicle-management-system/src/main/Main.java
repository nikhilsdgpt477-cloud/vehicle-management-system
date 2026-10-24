package main;

import vehicle.Car;
import vehicle.LuxuryCar;
import vehicle.Vehicle;
import vehicle.VehicleFeatures;

import java.io.*;
import java.nio.file.*;
import java.time.Year;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final Path DATA_FILE = Paths.get("data", "vehicles.csv");
    private static final List<Vehicle> vehicles = new ArrayList<>();

    public static void main(String[] args) {
        loadVehicles();
        if (args.length > 0 && args[0].equalsIgnoreCase("--deadlock-demo")) {
            runDeadlockDemo();
            return;
        }

        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Choose an option: ");
                String choice = sc.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> addVehicle(sc);
                        case "2" -> listVehicles();
                        case "3" -> searchVehicle(sc);
                        case "4" -> removeVehicle(sc);
                        case "5" -> startVehicle(sc);
                        case "6" -> showFeatures(sc);
                        case "7" -> showConceptDemo();
                        case "0" -> running = false;
                        default -> System.out.println("Please choose a valid menu option.");
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                if (running) {
                    System.out.println();
                }
            }
            saveVehicles();
            System.out.println("Vehicle data saved. Goodbye!");
        }
    }

    private static void printMenu() {
        System.out.println("==================================");
        System.out.println("     VEHICLE MANAGEMENT SYSTEM");
        System.out.println("==================================");
        System.out.println("1. Add a vehicle");
        System.out.println("2. List all vehicles");
        System.out.println("3. Search by vehicle number");
        System.out.println("4. Remove a vehicle");
        System.out.println("5. Start/stop a vehicle");
        System.out.println("6. Show vehicle features");
        System.out.println("7. Java OOP and thread demonstration");
        System.out.println("0. Save and exit");
        System.out.println("----------------------------------");
    }

    private static void addVehicle(Scanner sc) {
        System.out.print("Vehicle name: ");
        String name = nonEmpty(sc.nextLine(), "Vehicle name cannot be empty.");
        System.out.print("Vehicle number (unique): ");
        String number = nonEmpty(sc.nextLine(), "Vehicle number cannot be empty.").toUpperCase(Locale.ROOT);
        if (findByNumber(number) != null) {
            throw new IllegalArgumentException("A vehicle with that number already exists.");
        }
        System.out.print("Manufacturing year: ");
        int year;
        try {
            year = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Year must be a number.");
        }
        if (year < 1886 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Enter a year from 1886 to " + Year.now().getValue() + ".");
        }
        System.out.print("Is this a luxury car? (y/n): ");
        String luxury = sc.nextLine().trim();
        Vehicle vehicle;
        if (luxury.equalsIgnoreCase("y")) {
            System.out.print("Luxury feature (e.g. sunroof, massage seats): ");
            String feature = nonEmpty(sc.nextLine(), "Luxury feature cannot be empty.");
            vehicle = new LuxuryCar(name, number, year, feature);
        } else {
            vehicle = new Car(name, number, year);
        }
        vehicles.add(vehicle);
        saveVehicles();
        System.out.println("Vehicle added successfully.");
    }

    private static void listVehicles() {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found. Add a vehicle first.");
            return;
        }
        System.out.println("----- Registered Vehicles (" + vehicles.size() + ") -----");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            System.out.printf("%d. %s | %s | %d | %s%n",
                    i + 1, v.getName(), v.getNumber(), v.getYear(),
                    v instanceof LuxuryCar ? "Luxury Car" : "Car");
        }
    }

    private static void searchVehicle(Scanner sc) {
        System.out.print("Enter vehicle number: ");
        String number = sc.nextLine().trim();
        Vehicle v = findByNumber(number);
        if (v == null) {
            System.out.println("No vehicle found with number " + number + ".");
        } else {
            v.displayVehicleInfo();
        }
    }

    private static void removeVehicle(Scanner sc) {
        System.out.print("Enter vehicle number to remove: ");
        String number = sc.nextLine().trim();
        Vehicle v = findByNumber(number);
        if (v == null) {
            System.out.println("Vehicle not found.");
            return;
        }
        vehicles.remove(v);
        saveVehicles();
        System.out.println("Vehicle removed successfully.");
    }

    private static void startVehicle(Scanner sc) {
        System.out.print("Enter vehicle number: ");
        Vehicle v = findByNumber(sc.nextLine().trim());
        if (v == null) {
            System.out.println("Vehicle not found.");
            return;
        }
        if (!(v instanceof Car car)) {
            System.out.println("This vehicle type cannot be controlled by this demo.");
            return;
        }
        if (car.isRunning()) {
            car.stopVehicle();
        } else {
            car.startVehicle();
        }
        saveVehicles();
    }

    private static void showFeatures(Scanner sc) {
        System.out.print("Enter vehicle number: ");
        Vehicle v = findByNumber(sc.nextLine().trim());
        if (v == null) {
            System.out.println("Vehicle not found.");
        } else if (v instanceof VehicleFeatures features) {
            features.showFeatures();
        }
    }

    private static void showConceptDemo() {
        if (vehicles.isEmpty()) {
            System.out.println("Add a vehicle first to demonstrate inheritance and polymorphism.");
            return;
        }
        Vehicle v = vehicles.get(0);
        System.out.println("Abstract class / overridden method:");
        v.displayVehicleInfo();
        System.out.println("\nOverloaded method:");
        v.displayVehicleInfo(v.getName(), v.getNumber());
        System.out.println("\nInterface method running on a separate thread:");
        Thread featureThread = new Thread(() -> {
            if (v instanceof VehicleFeatures features) features.showFeatures();
        }, "FeatureThread");
        Thread startThread = new Thread(() -> {
            if (v instanceof VehicleFeatures features) features.startVehicle();
        }, "VehicleStartThread");
        startThread.start();
        featureThread.start();
        try {
            startThread.join();
            featureThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Demo interrupted.");
        }
        System.out.println("\nWait/notify demonstration:");
        Signal signal = new Signal();
        Thread waiter = new Thread(() -> {
            try { signal.awaitSignal(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "WaitingThread");
        waiter.start();
        try {
            signal.waitUntilReady();
            signal.release();
            waiter.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static Vehicle findByNumber(String number) {
        for (Vehicle v : vehicles) {
            if (v.getNumber().equalsIgnoreCase(number.trim())) return v;
        }
        return null;
    }

    private static String nonEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(message);
        return value.trim();
    }

    private static void loadVehicles() {
        if (!Files.exists(DATA_FILE)) return;
        try {
            Files.createDirectories(DATA_FILE.getParent());
            for (String line : Files.readAllLines(DATA_FILE)) {
                String[] p = line.split(",", -1);
                if (p.length != 5) continue;
                try {
                    String name = decode(p[0]), number = decode(p[1]);
                    int year = Integer.parseInt(p[2]);
                    if (p[3].equals("L")) vehicles.add(new LuxuryCar(name, number, year, decode(p[4])));
                    else vehicles.add(new Car(name, number, year));
                } catch (RuntimeException ignored) {
                    // Skip malformed data lines instead of preventing the app from starting.
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load saved vehicles: " + e.getMessage());
        }
    }

    private static void saveVehicles() {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            List<String> lines = new ArrayList<>();
            for (Vehicle v : vehicles) {
                boolean luxury = v instanceof LuxuryCar;
                String feature = luxury ? ((LuxuryCar) v).getLuxuryFeature() : "";
                lines.add(encode(v.getName()) + "," + encode(v.getNumber()) + "," + v.getYear()
                        + "," + (luxury ? "L" : "C") + "," + encode(feature));
            }
            Files.write(DATA_FILE, lines);
        } catch (IOException e) {
            System.out.println("Warning: Could not save vehicle data: " + e.getMessage());
        }
    }

    private static String encode(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
    private static String decode(String s) {
        return new String(Base64.getDecoder().decode(s), java.nio.charset.StandardCharsets.UTF_8);
    }

    private static void runDeadlockDemo() {
        System.out.println("Deadlock demo starts two threads that intentionally block each other.");
        Object lock1 = new Object(), lock2 = new Object();
        CountDownLatch ready = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1 acquired Lock 1");
                ready.countDown();
                await(ready);
                System.out.println("Thread 1 waiting for Lock 2");
                synchronized (lock2) { System.out.println("Thread 1 acquired Lock 2"); }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2 acquired Lock 2");
                ready.countDown();
                await(ready);
                System.out.println("Thread 2 waiting for Lock 1");
                synchronized (lock1) { System.out.println("Thread 2 acquired Lock 1"); }
            }
        });
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        System.out.println("Demo launched. Threads are daemon threads so the application can exit.");
    }

    private static void await(CountDownLatch latch) {
        try { latch.await(); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static class Signal {
        private boolean waiting;
        private boolean released;
        synchronized void awaitSignal() throws InterruptedException {
            waiting = true;
            System.out.println(Thread.currentThread().getName() + " is waiting.");
            notifyAll();
            while (!released) wait();
            System.out.println(Thread.currentThread().getName() + " was notified.");
        }
        synchronized void waitUntilReady() throws InterruptedException {
            while (!waiting) wait();
        }
        synchronized void release() {
            released = true;
            System.out.println("notifyAll() called.");
            notifyAll();
        }
    }
}
