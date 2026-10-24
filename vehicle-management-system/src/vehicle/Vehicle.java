package vehicle;

public abstract class Vehicle {
    private final String name;
    private final String number;
    private final int year;
    private final String type;
    private boolean running;

    protected Vehicle(String name, String number, String type, int year) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.year = year;
    }

    public String getName() { return name; }
    public String getNumber() { return number; }
    public int getYear() { return year; }
    public String getType() { return type; }
    public boolean isRunning() { return running; }
    protected void setRunning(boolean running) { this.running = running; }

    public abstract void displayVehicleInfo();

    // Method overloading
    public void displayVehicleInfo(String name) {
        System.out.println("Vehicle Name: " + name);
    }

    public void displayVehicleInfo(String name, String number) {
        System.out.println("Vehicle Name: " + name);
        System.out.println("Vehicle Number: " + number);
    }

    public void showVehicleType() {
        System.out.println("Vehicle Type: " + type);
    }
}
