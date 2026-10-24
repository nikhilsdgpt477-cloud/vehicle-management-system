package vehicle;

public class Car extends Vehicle implements VehicleFeatures {
    public Car(String name, String number, int year) {
        super(name, number, "Car", year);
    }

    @Override
    public void displayVehicleInfo() {
        System.out.println("----- Car Information -----");
        System.out.println("Name   : " + getName());
        System.out.println("Number : " + getNumber());
        System.out.println("Type   : " + getType());
        System.out.println("Year   : " + getYear());
        System.out.println("Status : " + (isRunning() ? "Running" : "Stopped"));
    }

    @Override
    public void startVehicle() {
        setRunning(true);
        System.out.println(getName() + " started successfully.");
    }

    public void stopVehicle() {
        setRunning(false);
        System.out.println(getName() + " stopped.");
    }

    @Override
    public void showFeatures() {
        System.out.println("Standard features: safety, comfort and performance.");
    }
}
