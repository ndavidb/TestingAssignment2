package CarRace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarPerformanceTest {
    private Engine standardEngine;
    private Engine turboEngine;
    private Engine hybridEngine;

    private Tyres mediumTyres;
    private Tyres softTyres;
    private Tyres hardTyres;
    private Tyres wetTyres; // It was defined but not used in tests

    private AeroKit standardAeroKit;
    private AeroKit downforceAeroKit;
    private AeroKit lowDragAeroKit;
    private AeroKit wetWeatherAeroKit;  // It was defined but not used in tests
    private AeroKit extremeAeroKit;  // It was defined but not used in tests

    @BeforeEach
    void setUp() {

        //Purpose: create a common set of components
        List<Engine> engines = CarComponentFactory.getAvailableEngines();
        standardEngine = engines.get(0); // Standard V8 engine
        turboEngine = engines.get(1); // Turbocharged V6 engine
        hybridEngine = engines.get(2); // High-Efficiency Hybrid engine

        List<Tyres> tyres = CarComponentFactory.getAvailableTyres();
        mediumTyres = tyres.get(0); // Soft Compound tyres
        softTyres = tyres.get(1); // Medium Compound tyres
        hardTyres = tyres.get(2); // Hard Compound tyres
        wetTyres = tyres.get(3); // Wet Weather tyres


        List<AeroKit> aeroKits = CarComponentFactory.getAvailableAeroKits();
        standardAeroKit = aeroKits.get(0); // Standard Aero Kit
        downforceAeroKit = aeroKits.get(1); // Downforce-Focused Aero Kit
        lowDragAeroKit = aeroKits.get(2); // Low-Drag Aero Kit
        wetWeatherAeroKit = aeroKits.get(3); // Wet Weather Aero Kit
        extremeAeroKit = aeroKits.get(4); // Extreme Aero Kit
    }

    @Test
    @DisplayName("TC1: Validate Base Car Creation")
    void testValidCarCreation() {
        // Purpose: To confirm a Car object is created with the correct components.
        Car car = new Car(standardEngine, mediumTyres, standardAeroKit);

        assertNotNull(car, "Car object should not be null.");
        assertEquals(standardEngine, car.getEngine(), "Engine should be correctly set.");
        assertEquals(mediumTyres, car.getTyres(), "Tyres should be correctly set.");
        assertEquals(standardAeroKit, car.getAeroKit(), "AeroKit should be correctly set.");
    }

    @Test
    @DisplayName("TC2: Verify Top Speed Calculation")
    void testTopSpeedCalculation() {
        // Purpose: To ensure top speed correctly combines engine and aero effects.
        // Test Data: Two cars with different speed-focused components.
        Car speedCar = new Car(turboEngine, mediumTyres, lowDragAeroKit);
        Car downforceCar = new Car(standardEngine, mediumTyres, downforceAeroKit);

        assertTrue(speedCar.getCalculatedTopSpeed() > downforceCar.getCalculatedTopSpeed(),
                "Speed car's top speed should be greater than the downforce car's.");
    }

    @Test
    @DisplayName("TC3: Verify Fuel Consumption Calculation")
    void testFuelConsumptionCalculation() {
        // Purpose: To ensure fuel consumption correctly applies modifiers.
        // Test Data: Two cars with different efficiency-focused components.
        Car efficientCar = new Car(hybridEngine, mediumTyres, lowDragAeroKit);
        Car thirstyCar = new Car(turboEngine, mediumTyres, downforceAeroKit);

        assertTrue(efficientCar.getCalculatedFuelConsumption() < thirstyCar.getCalculatedFuelConsumption(),
                "Efficient car's fuel consumption should be less than the thirsty car's.");
    }

    @Test
    @DisplayName("TC4: Verify Handling Calculation")
    void testHandlingCalculation() {
        // Purpose: To ensure handling correctly combines tyre and aero effects.
        // Test Data: Two cars with different grip-focused components.
        Car highGripCar = new Car(standardEngine, softTyres, downforceAeroKit);
        Car lowGripCar = new Car(standardEngine, hardTyres, lowDragAeroKit);

        assertTrue(highGripCar.getCalculatedHandling() > lowGripCar.getCalculatedHandling(),
                "High-grip car's handling should be greater than the low-grip car's.");
    }

    @Test
    @DisplayName("TC5: Validate Component Swapping Updates Stats")
    void testComponentSwapping() {
        // Purpose: To test that car stats update after changing components via setters.
        Car car = new Car(standardEngine, mediumTyres, standardAeroKit);
        double initialSpeed = car.getCalculatedTopSpeed();

        car.setEngine(turboEngine);

        assertTrue(car.getCalculatedTopSpeed() > initialSpeed,
                "Top speed should increase after swapping to a better engine.");
    }
}
