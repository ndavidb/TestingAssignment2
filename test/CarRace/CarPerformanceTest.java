package CarRace;

import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public class CarPerformanceTest {
    private Engine standardEngine;
    private Engine turboEngine;
    private Engine hybridEngine;

    private Tyres mediumTyres;
    private Tyres softTyres;
    private Tyres hardTyres;
    private Tyres wetTyres;

    private AeroKit standardAeroKit;
    private AeroKit downforceAeroKit;
    private AeroKit lowDragAeroKit;
    private AeroKit wetWeatherAeroKit;
    private AeroKit extremeAeroKit;

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


}
