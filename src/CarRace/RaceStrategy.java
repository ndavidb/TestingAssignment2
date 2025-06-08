package CarRace;
public class RaceStrategy {
    private final String summary;
    private final boolean isValid;

    public RaceStrategy(String summary, boolean isValid) {
        this.summary = summary;
        this.isValid = isValid;
    }

    public String getSummary() {
        return summary;
    }

    public boolean isValid() {
        return isValid;
    }
}