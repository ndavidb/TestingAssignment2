package CarRace;
public class Tyres {
    private final String name;
    private final double wearRate; // Higher is worse
    private final double handlingModifier; // Multiplier for cornering ability
    private final WeatherCondition optimalCondition;

    public enum WeatherCondition {
        DRY, WET, ANY
    }

    public Tyres(String name, double wearRate, double handlingModifier, WeatherCondition optimalCondition) {
        this.name = name;
        this.wearRate = wearRate;
        this.handlingModifier = handlingModifier;
        this.optimalCondition = optimalCondition;
    }

    public String getName() {
        return name;
    }

    public double getWearRate() {
        return wearRate;
    }

    public double getHandlingModifier() {
        return handlingModifier;
    }

    public WeatherCondition getOptimalCondition() {
        return optimalCondition;
    }

    @Override
    public String toString() {
        return name;
    }
}