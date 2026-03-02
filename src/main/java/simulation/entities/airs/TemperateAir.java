package simulation.entities.airs;

import fileio.AirInput;
import fileio.CommandInput;
import simulation.Pair;

public class TemperateAir extends Air {
    private static final double ROUND_VALUE = 100.0;
    private static final double OXYGEN_MULT = 2;
    private static final double TOXICITY_VALUE = 84;
    private static final double HUMIDITY_VALUE = 0.7;
    private static final double POLLEN_VALUE = 0.1;
    private static final double SPRING_VALUE = 15;
    private double pollenLevel;
    private double airQuality;

    public TemperateAir(final AirInput air, final Pair position) {
        super(air, position);
        this.pollenLevel = air.getPollenLevel();
        calculateAirQuality();
    }

    /**
     * sets the air quality
     * @param airQuality
     */
    public void setAirQuality(final double airQuality) {
        this.airQuality = airQuality;
    }

    @Override
    public final double getAirQuality() {
        return this.airQuality;
    }

    @Override
    public final String getType() {
        return "TemperateAir";
    }

    @Override
    public final double getCustomField() {
        return this.pollenLevel;
    }

    @Override
    public final String getCustomFieldName() {
        return "pollenLevel";
    }

    @Override
    public final void calculateAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_MULT)
                + (humidity * HUMIDITY_VALUE)
                - (pollenLevel * POLLEN_VALUE);
    }

    @Override
    public final double calculateAirToxicity() {
        double toxicityAQ = ROUND_VALUE * (1 - getAirQuality() / TOXICITY_VALUE);
        double result = Math.round(toxicityAQ * ROUND_VALUE) / ROUND_VALUE;
        return normalizeValue(result);
    }

    @Override
    public final void changeWeather(final CommandInput command) {
        String season = command.getSeason();
        this.airQuality = calculateAirToxicity()
                - (season.equalsIgnoreCase("Spring") ? SPRING_VALUE : 0);
    }
}
