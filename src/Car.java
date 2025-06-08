public class Car {
    private Engine engine;
    private Tyres tyres;
    private AeroKit aeroKit;

    // Base values for a car before modifications
    private static final double BASE_TOP_SPEED = 200; // km/h
    private static final double BASE_FUEL_CONSUMPTION = 2.0; // Liters per lap on a standard track
    private static final double BASE_HANDLING = 5.0; // Base cornering rating

    public Car(Engine engine, Tyres tyres, AeroKit aeroKit) {
        this.engine = engine;
        this.tyres = tyres;
        this.aeroKit = aeroKit;
    }

    public double getCalculatedTopSpeed() {
        return (BASE_TOP_SPEED + aeroKit.getTopSpeedEffect()) * engine.getSpeedModifier();
    }

    public double getCalculatedFuelConsumption() {
        return BASE_FUEL_CONSUMPTION * engine.getFuelConsumptionModifier() / aeroKit.getFuelEfficiencyModifier();
    }

    public double getCalculatedHandling() {
        return (BASE_HANDLING + aeroKit.getCorneringAbility()) * tyres.getHandlingModifier();
    }

    public Engine getEngine() { return engine; }
    public Tyres getTyres() { return tyres; }
    public AeroKit getAeroKit() { return aeroKit; }

    public void setEngine(Engine engine) { this.engine = engine; }
    public void setTyres(Tyres tyres) { this.tyres = tyres; }
    public void setAeroKit(AeroKit aeroKit) { this.aeroKit = aeroKit; }
}