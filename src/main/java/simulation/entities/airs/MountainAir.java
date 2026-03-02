package simulation.entities.airs;

import fileio.AirInput;
import fileio.CommandInput;
import simulation.Pair;

public class MountainAir extends Air {
    private static final double ROUND_VALUE = 100.0;
    private static final double TOXICITY_VALUE = 78;
    private static final double ALTITUDE_VALUE = 1000;
    private static final double ALTITUDE_VALUE_2 = 0.5;
    private static final double OXYGEN_MULT = 2;
    private static final double HUMIDITY_MULT = 0.6;
    private static final double HICKERS_MULT = 0.1;
    private double altitude;
    private double airQuality;

    public MountainAir(final AirInput air, final Pair position) {
        super(air, position);
        this.altitude = air.getAltitude();
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
        return "MountainAir";
    }

    @Override
    public final double getCustomField() {
        return this.altitude;
    }

    @Override
    public final String getCustomFieldName() {
        return "altitude";
    }

    @Override
    public final void calculateAirQuality() {
        double oxygenFactor = oxygenLevel - (altitude / ALTITUDE_VALUE * ALTITUDE_VALUE_2);
        this.airQuality = (oxygenFactor * OXYGEN_MULT) + (humidity * HUMIDITY_MULT);
    }

    @Override
    public final double calculateAirToxicity() {
        double toxicityAQ = ROUND_VALUE * (1 - getAirQuality() / TOXICITY_VALUE);
        double result = Math.round(toxicityAQ * ROUND_VALUE) / ROUND_VALUE;
        return normalizeValue(result);
    }

    @Override
    public final void changeWeather(final CommandInput command) {
        int numberOfHikers = command.getNumberOfHikers();
        this.airQuality = calculateAirToxicity() - (numberOfHikers * HICKERS_MULT);
    }
}
