package CarRace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleRacesTest {

    private StrategyOptimiser optimiser;
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
    }

    @Test
    @DisplayName("TC30: Verify consistency of strategy output for same inputs across multiple runs")
    void testStrategyConsistencyOverMultipleRuns() {
        for (Track track : tracks) {
            for (int i = 0; i < engines.size(); i++) {
                Engine engine = engines.get(i);
                Tyres tyre = tyres.get(i % tyres.size());
                AeroKit aero = aeroKits.get(i % aeroKits.size());
                Car car = new Car(engine, tyre, aero);

                RaceStrategy firstRun = optimiser.generateOptimalStrategy(car, track, 10, 70.0, tyre.getOptimalCondition());
                RaceStrategy secondRun = optimiser.generateOptimalStrategy(car, track, 10, 70.0, tyre.getOptimalCondition());

                assertEquals(firstRun.getSummary(), secondRun.getSummary(),"Strategy should have the same summary");
            }
        }
    }

    @Test
    @DisplayName("TC31: Verify output for different customisations across tracks")
    void testStrategyAcrossDifferentCustomisations() {
        for (Engine engine : engines) {
            for (Tyres tyre : tyres) {
                for (AeroKit aeroKit : aeroKits) {
                    Car car = new Car(engine, tyre, aeroKit);
                    for (Track track : tracks) {
                        RaceStrategy strategy = optimiser.generateOptimalStrategy(
                                car, track, 3, 115.0, tyre.getOptimalCondition());

                        assertNotNull(strategy);
                        assertTrue(strategy.getSummary().contains("Lap"),
                                "Strategy output must contain lap information");
                        assertTrue(strategy.getSummary().contains("Strategy Summary"),
                                "Strategy output must contain a summary section");
                    }
                }
            }
        }
    }
}
