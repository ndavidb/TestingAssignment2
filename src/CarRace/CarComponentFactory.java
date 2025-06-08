package CarRace;
import java.util.Arrays;
import java.util.List;

public class CarComponentFactory {

    public static List<Engine> getAvailableEngines() {
        return Arrays.asList(
                new Engine("Standard V8", 1.0, 1.0),
                new Engine("Turbocharged V6", 1.15, 1.2),
                new Engine("High-Efficiency Hybrid", 0.95, 0.75)
        );
    }

    public static List<Tyres> getAvailableTyres() {
        return Arrays.asList(
                new Tyres("Soft Compound", 1.5, 1.2, Tyres.WeatherCondition.DRY),
                new Tyres("Medium Compound", 1.0, 1.0, Tyres.WeatherCondition.DRY),
                new Tyres("Hard Compound", 0.6, 0.9, Tyres.WeatherCondition.DRY),
                new Tyres("Wet Weather Tyres", 0.8, 1.1, Tyres.WeatherCondition.WET)
        );
    }

    public static List<AeroKit> getAvailableAeroKits() {
        return Arrays.asList(
                new AeroKit("Standard Kit", 0.30, 200, 0, 1.0, 6),
                new AeroKit("Downforce-Focused Kit", 0.35, 350, -30, 0.83, 9), // 10/12 = 0.83
                new AeroKit("Low-Drag Kit", 0.25, 150, 30, 1.16, 5),          // 14/12 = 1.16
                new AeroKit("Wet Weather Kit", 0.32, 220, -20, 0.91, 7),      // 11/12 = 0.91
                new AeroKit("Extreme Aero Kit", 0.40, 500, -50, 0.75, 10)     // 9/12 = 0.75
        );
    }

    public static List<Track> getAvailableTracks() {
        return Arrays.asList(
                new Track("Monza (Temple of Speed)", 5.793, 11, 1.2),
                new Track("Monaco (Twisty Streets)", 3.337, 19, 1.9),
                new Track("Silverstone (High Speed Mix)", 5.891, 18, 1.5),
                new Track("Spa-Francorchamps (Classic)", 7.004, 20, 1.6),
                new Track("Suzuka (Figure-Eight)", 5.807, 18, 1.8)
        );
    }
}
