package CarRace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RaceStrategyTest {
    private StrategyOptimiser optimiser;
    private Car testCar;
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

        // A default, standard car for testing
        testCar = new Car(engines.getFirst(), tyres.get(1), aeroKits.getFirst());
    }

    @Test
    @DisplayName("TC_RACE_01: Short Race on a Power Track (Monza)")
    void testShortDryRaceStrategy() {
        // Purpose: To test a scenario where no pit stops are expected.
        // Test Data: 15 laps at Monza, 110L fuel tank, standard car.

        Track monza = tracks.getFirst();
        int totalLaps = 15;
        double fuelTankCapacity = 110.0;

        RaceStrategy strategy = optimiser.generateOptimalStrategy(testCar, monza, totalLaps, fuelTankCapacity, Tyres.WeatherCondition.DRY);

        assertTrue(strategy.isValid(), "Strategy should be valid.");
        assertTrue(strategy.getSummary().contains("Total Pit Stops: 0"),
                "A short race with a full tank should not require any pit stops.");
    }

    @Test
    @DisplayName("TC_RACE_02: Long Race Forcing Fuel Stops")
    void testLongRaceForcesFuelStop() {
        // Purpose: To ensure the strategy correctly identifies the need for fuel stops.
        // Test Data: 70 laps at Monza, high-consumption car.
        Track monza = tracks.getFirst();
        Car thirstyCar = new Car(engines.get(1), tyres.get(2), aeroKits.get(4));
        int totalLaps = 70;
        double fuelTankCapacity = 110.0;

        RaceStrategy strategy = optimiser.generateOptimalStrategy(thirstyCar, monza, totalLaps, fuelTankCapacity, Tyres.WeatherCondition.DRY);

        assertTrue(strategy.isValid(), "Strategy should be valid.");
        assertTrue(strategy.getSummary().contains("PIT STOP (Low Fuel)"),
                "A long race with a high-consumption car must have a pit stop for fuel.");
    }

    @Test
    @DisplayName("TC_RACE_03: Twisty Track Forcing Tyre Wear Stops")
    void testTwistyTrackForcesTyreWearStop() {
        // Purpose: To ensure the strategy identifies pit stops due to high tyre wear.
        // Test Data: 60 laps at Monaco with soft, high-wear tyres.
        Track monaco = tracks.get(1);
        Car softTyreCar = new Car(engines.getFirst(), tyres.getFirst(), aeroKits.get(1));
        int totalLaps = 60;
        double fuelTankCapacity = 110.0;

        RaceStrategy strategy = optimiser.generateOptimalStrategy(softTyreCar, monaco, totalLaps, fuelTankCapacity, Tyres.WeatherCondition.DRY);

        assertTrue(strategy.isValid(), "Strategy should be valid.");
        assertTrue(strategy.getSummary().contains("PIT STOP (High Tyre Wear)"),
                "A race on a twisty track with soft tyres must have a pit stop for wear.");
    }

    @Nested
    @DisplayName("Tyre and Weather Incompatibility Tests")
    class IncompatibilityTests {

        @Test
        @DisplayName("TC_RACE_04: Correct Tyre Strategy for Wet Weather")
        void testWetWeatherWithCorrectTyres() {
            // Purpose: To validate a successful strategy generation in wet conditions.
            // Test Data: Wet race at Spa with Wet Weather Tyres.
            Track spa = tracks.get(3);
            Car wetCar = new Car(engines.getFirst(), tyres.get(3), aeroKits.get(3)); // Wet Tyres, Wet Aero

            RaceStrategy strategy = optimiser.generateOptimalStrategy(wetCar, spa, 44, 110.0, Tyres.WeatherCondition.WET);

            assertTrue(strategy.isValid(), "A wet race with wet tyres should produce a valid strategy.");
        }

        @Test
        @DisplayName("TC_RACE_05: Invalid Tyre Strategy for Wet Weather")
        void testWetWeatherWithIncorrectTyres() {
            // Purpose: To test the incompatibility check for using dry tyres in rain.
            // Test Data: Wet race with Dry (Medium) tyres.
            Track spa = tracks.get(3);
            Car dryTyreCar = new Car(engines.getFirst(), tyres.get(1), aeroKits.get(3)); // Medium Tyres

            RaceStrategy strategy = optimiser.generateOptimalStrategy(dryTyreCar, spa, 44, 110.0, Tyres.WeatherCondition.WET);

            assertFalse(strategy.isValid(), "Strategy should be invalid when using dry tyres in the wet.");
            assertTrue(strategy.getSummary().contains("Error: Dry tyres selected for a wet race."), "Summary should contain the correct error message.");
        }

        @Test
        @DisplayName("TC_RACE_07: Invalid Tyre Strategy for Dry Weather")
        void testDryWeatherWithIncorrectTyres() {
            // Purpose: To test the incompatibility check for using wet tyres in the dry.
            // Test Data: Dry race with Wet tyres.
            Track monza = tracks.getFirst();
            Car wetTyreCar = new Car(engines.getFirst(), tyres.get(3), aeroKits.getFirst()); // Wet Tyres

            RaceStrategy strategy = optimiser.generateOptimalStrategy(wetTyreCar, monza, 50, 110.0, Tyres.WeatherCondition.DRY);

            assertFalse(strategy.isValid(), "Strategy should be invalid when using wet tyres in the dry.");
            assertTrue(strategy.getSummary().contains("Error: Wet tyres selected for a dry race."), "Summary should contain the correct error message.");
        }
    }

    @Test
    @DisplayName("TC_RACE_08: Strategy Consistency Check")
    void testStrategyIsDeterministic() {
        // Purpose: To ensure the simulation produces identical, consistent results.
        // Test Data: The setup from TC7, run twice.
        Track monza = tracks.getFirst();
        Car thirstyCar = new Car(engines.get(1), tyres.get(2), aeroKits.get(4));
        int totalLaps = 70;
        double fuelTankCapacity = 110.0;

        RaceStrategy strategy1 = optimiser.generateOptimalStrategy(thirstyCar, monza, totalLaps, fuelTankCapacity, Tyres.WeatherCondition.DRY);
        RaceStrategy strategy2 = optimiser.generateOptimalStrategy(thirstyCar, monza, totalLaps, fuelTankCapacity, Tyres.WeatherCondition.DRY);

        assertEquals(strategy1.getSummary(), strategy2.getSummary(), "Two identical simulations should produce identical strategy summaries.");
    }
}
