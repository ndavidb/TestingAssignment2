package CarRace;

public class AeroKit {
    private final String name;
    private final double dragCoefficient;
    private final int downforce;
    private final int topSpeedEffect; // Direct effect on top speed in km/h
    private final double fuelEfficiencyModifier; // Multiplier
    private final int corneringAbility; // Rating out of 10

    public AeroKit(String name, double dragCoefficient, int downforce, int topSpeedEffect, double fuelEfficiencyModifier, int corneringAbility) {
        this.name = name;
        this.dragCoefficient = dragCoefficient;
        this.downforce = downforce;
        this.topSpeedEffect = topSpeedEffect;
        this.fuelEfficiencyModifier = fuelEfficiencyModifier;
        this.corneringAbility = corneringAbility;
    }

    public String getName() {
        return name;
    }

    public int getTopSpeedEffect() {
        return topSpeedEffect;
    }

    public double getFuelEfficiencyModifier() {
        return fuelEfficiencyModifier;
    }

    public int getCorneringAbility() {
        return corneringAbility;
    }

    @Override
    public String toString() {
        return name;
    }
}