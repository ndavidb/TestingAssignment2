package CarRace;
public class Engine {
    private final String name;
    private final double speedModifier; // Multiplier for top speed
    private final double fuelConsumptionModifier; // Multiplier for fuel usage

    public Engine(String name, double speedModifier, double fuelConsumptionModifier) {
        this.name = name;
        this.speedModifier = speedModifier;
        this.fuelConsumptionModifier = fuelConsumptionModifier;
    }

    public String getName() {
        return name;
    }

    public double getSpeedModifier() {
        return speedModifier;
    }

    public double getFuelConsumptionModifier() {
        return fuelConsumptionModifier;
    }

    @Override
    public String toString() {
        return name;
    }
}