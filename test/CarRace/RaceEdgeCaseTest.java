package CarRace;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RaceEdgeCaseTest {

    private StrategyOptimiser optimiser;
    private Car testCar1, testCar2;
    private List<Track> tracks;
    private List<Engine> engines;
    private List<Tyres> tyres;
    private List<AeroKit> aeroKits;

    @BeforeEach
    void setUp() {
        optimiser = new StrategyOptimiser();
        tracks = CarComponentFactory.getAvailableTracks();
        engines = CarComponentFactory.getAvailableEngines();
        tyres = CarComponentFactory.getAvailableTyres();
        aeroKits = CarComponentFactory.getAvailableAeroKits();

        // testing cars
        testCar1 = new Car(engines.getFirst(), tyres.get(1), aeroKits.getFirst());
        testCar2 = new Car(engines.get(2), tyres.get(2), aeroKits.get(2));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100, Integer.MIN_VALUE})
    @DisplayName("TC20: Negative laps should return error strategy")
    void testNegativeLaps(int laps) {
        Track monza = tracks.getFirst();
        double fuelTankCapacity = 110.0;

        RaceStrategy strategy = optimiser.generateOptimalStrategy(testCar1, monza, laps, fuelTankCapacity, Tyres.WeatherCondition.DRY);
        assertFalse(strategy.isValid(), "Strategy should be invalid.");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -5, -100, 0 })
    @DisplayName("TC21: Negative laps should return error strategy")
    void testNegativeFuelCapacity(double fuelCapacity) {
        Track monza = tracks.getFirst();
        int totalLaps = 50;

        RaceStrategy strategy = optimiser.generateOptimalStrategy(testCar1, monza, totalLaps, fuelCapacity, Tyres.WeatherCondition.DRY);
        assertFalse(strategy.isValid(), "Strategy should be invalid.");
    }

    @ParameterizedTest
    @ValueSource(doubles = {135.0, 140.0, 150.0, 200.0, Double.POSITIVE_INFINITY})
    @DisplayName("TC22: Value greater than 130 litre should return error")
    void testHighValueForFuelTankCapacity(double fuelTank) {
        Track monza = tracks.getFirst();
        int totalLaps = 50;

        RaceStrategy strategy = optimiser.generateOptimalStrategy(testCar1, monza, totalLaps, fuelTank, Tyres.WeatherCondition.DRY);
        assertFalse(strategy.isValid(), "Strategy should be invalid since the fuelTankCapacity is greater than 130.0");
    }

    @ParameterizedTest
    @ValueSource(ints = {80, 100, 120})
    @DisplayName("TC23: Value greater than 78 laps should return error strategy")
    void testHighValueForNumberOfLaps(int laps) {
        Track monza = tracks.getFirst();
        double fuelTankCapacity = 110.0;

        RaceStrategy strategy = optimiser.generateOptimalStrategy(testCar1, monza, laps, fuelTankCapacity, Tyres.WeatherCondition.DRY);
        assertFalse(strategy.isValid(), "Strategy should be invalid since the totalLaps is greater than 78");
    }

    @Test
    @DisplayName("TC24: Wrong tyres wear multiplier when weather=ANY and optimal!=ANY")
    void testWearMultiplierBranch() {
        // Find a tyre with a specific optimal condition (not ANY)
        Tyres mismatchTyre = tyres.stream()
                .filter(t -> t.getOptimalCondition() != Tyres.WeatherCondition.ANY)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No non-ANY tyre defined"));

        Car car = new Car(engines.getFirst(), mismatchTyre, aeroKits.getFirst());
        Track track = tracks.getFirst();
        // Use weather=ANY to avoid early incompatibility return
        RaceStrategy raceStrategy = optimiser.generateOptimalStrategy(car, track, 1, 100.0, Tyres.WeatherCondition.ANY);
        assertTrue(raceStrategy.isValid(), "Strategy should succeed with ANY weather");

        // Extract tyre wear value from log
        String firstLapLine = raceStrategy.getSummary().lines()
                .filter(line -> line.startsWith("Lap 1/1:"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Lap log not found"));

        // Parse wear percentage
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("Tyre Wear: ([0-9]+\\.?[0-9]*)%")
                .matcher(firstLapLine);
        assertTrue(m.find(), "Wear percentage should be logged");
        double reportedWear = Double.parseDouble(m.group(1));

        double expectedWear = mismatchTyre.getWearRate() * track.getTurnSeverity() * 3;
        assertEquals(expectedWear, reportedWear, 0.1, "Wear should be tripled for wrong tyres");
    }


}