package vehicle;

public class LuxuryCar extends Car {
    private final String luxuryFeature;

    public LuxuryCar(String name, String number, int year, String luxuryFeature) {
        super(name, number, year);
        this.luxuryFeature = luxuryFeature;
    }

    public String getLuxuryFeature() { return luxuryFeature; }

    @Override
    public void displayVehicleInfo() {
        super.displayVehicleInfo();
        System.out.println("Luxury Feature: " + luxuryFeature);
    }

    @Override
    public void showFeatures() {
        super.showFeatures();
        System.out.println("Luxury feature: " + luxuryFeature);
    }
}
