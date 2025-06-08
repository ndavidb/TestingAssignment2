public class StrategyOptimiser {

    private static final double TYRE_WEAR_THRESHOLD = 80.0; // Pit when tyres are 80% worn
    private static final double PIT_STOP_TIME_SECONDS = 25.0;

    public RaceStrategy generateOptimalStrategy(Car car, Track track, int totalLaps, double fuelTankCapacity, Tyres.WeatherCondition weather) {

        // Input validation for edge cases
        if (totalLaps <= 0 || fuelTankCapacity <= 0) {
            return new RaceStrategy("Error: Laps and Fuel Capacity must be positive.", false);
        }

        // Incompatibility check
        if (weather == Tyres.WeatherCondition.DRY && car.getTyres().getOptimalCondition() == Tyres.WeatherCondition.WET) {
            return new RaceStrategy("Error: Wet tyres selected for a dry race. This is not a valid strategy.", false);
        }
        if (weather == Tyres.WeatherCondition.WET && car.getTyres().getOptimalCondition() == Tyres.WeatherCondition.DRY) {
            return new RaceStrategy("Error: Dry tyres selected for a wet race. This is not a valid strategy.", false);
        }

        StringBuilder log = new StringBuilder();
        log.append("--- Race Strategy Simulation ---\n");
        log.append(String.format("Track: %s | Laps: %d | Weather: %s\n", track.getName(), totalLaps, weather));
        log.append(String.format("Car: %s Engine, %s Tyres, %s Aero\n\n", car.getEngine().getName(), car.getTyres().getName(), car.getAeroKit().getName()));

        double currentFuel = fuelTankCapacity;
        double currentTyreWear = 0.0;
        double totalRaceTime = 0.0;
        int pitStops = 0;

        Tyres currentTyres = car.getTyres();

        for (int lap = 1; lap <= totalLaps; lap++) {
            // 1. Calculate performance for this lap
            double handling = car.getCalculatedHandling();
            double speed = car.getCalculatedTopSpeed();

            // Adjust performance based on weather and tyre choice
            double weatherMismatchPenalty = 1.0;
            if (weather != currentTyres.getOptimalCondition() && currentTyres.getOptimalCondition() != Tyres.WeatherCondition.ANY) {
                weatherMismatchPenalty = 1.25; // 25% slower if wrong tyres for weather
            }

            // Lap time calculation (simplified model)
            // Time = (Distance / Speed) + TurnPenalty
            double turnPenalty = track.getNumberOfTurns() * track.getTurnSeverity() * (25.0 / handling); // More turns/handling -> more/less penalty
            double lapTimeSeconds = ((track.getLengthKm() / speed) * 3600) + turnPenalty;
            lapTimeSeconds *= weatherMismatchPenalty;

            // Fuel consumption calculation
            double fuelConsumedThisLap = car.getCalculatedFuelConsumption();

            // Tyre wear calculation
            double wearThisLap = currentTyres.getWearRate() * track.getTurnSeverity();
            if (weather != currentTyres.getOptimalCondition() && currentTyres.getOptimalCondition() != Tyres.WeatherCondition.ANY) {
                wearThisLap *= 3.0; // Wrong tyres wear extremely fast
            }


            // 2. Check for pit stop
            boolean needsPitStop = false;
            String pitReason = "";
            if (currentFuel < fuelConsumedThisLap) {
                needsPitStop = true;
                pitReason = "Low Fuel";
            } else if (currentTyreWear + wearThisLap > TYRE_WEAR_THRESHOLD) {
                needsPitStop = true;
                pitReason = "High Tyre Wear";
            }

            if (needsPitStop) {
                pitStops++;
                totalRaceTime += PIT_STOP_TIME_SECONDS;
                currentFuel = fuelTankCapacity;
                currentTyreWear = 0.0;
                log.append(String.format("Lap %d: PIT STOP (%s). Refuel and change tyres. Pit time: %.1fs\n", lap, pitReason, PIT_STOP_TIME_SECONDS));
            }

            // 3. Update state for next lap
            totalRaceTime += lapTimeSeconds;
            currentFuel -= fuelConsumedThisLap;
            currentTyreWear += wearThisLap;

            log.append(String.format("Lap %d/%d: Lap Time: %.2fs | Fuel: %.1fL | Tyre Wear: %.1f%%\n",
                    lap, totalLaps, lapTimeSeconds, currentFuel, currentTyreWear));
        }

        log.append("\n--- Strategy Summary ---\n");
        log.append(String.format("Total Pit Stops: %d\n", pitStops));
        log.append(String.format("Estimated Total Race Time: %s\n", formatTime(totalRaceTime)));

        return new RaceStrategy(log.toString(), true);
    }

    private String formatTime(double totalSeconds) {
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        double seconds = totalSeconds % 60;
        return String.format("%d:%02d:%05.2f", hours, minutes, seconds);
    }
}
