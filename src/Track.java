public class Track {
    private final String name;
    private final double lengthKm; // Length of one lap
    private final int numberOfTurns;
    private final double turnSeverity; // A factor from 1.0 (easy) to 2.0 (hard)

    public Track(String name, double lengthKm, int numberOfTurns, double turnSeverity) {
        this.name = name;
        this.lengthKm = lengthKm;
        this.numberOfTurns = numberOfTurns;
        this.turnSeverity = turnSeverity;
    }

    public String getName() {
        return name;
    }

    public double getLengthKm() {
        return lengthKm;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public double getTurnSeverity() {
        return turnSeverity;
    }

    @Override
    public String toString() {
        return name;
    }
}