package simulation.entities.airs;

import fileio.AirInput;
import fileio.CommandInput;
import simulation.Pair;

public class PolarAir extends Air {
    private static final double ROUND_VALUE = 100.0;
    private static final double TOXICITY_VALUE = 142;
    private static final double WIND_MULT = 0.2;
    private static final double OXYGEN_MULT = 2;
    private static final double ICE_MULT = 0.05;
    private double iceCrystalConcentration;
    private double airQuality;

    public PolarAir(final AirInput air, final Pair position) {
        super(air, position);
        this.iceCrystalConcentration = air.getIceCrystalConcentration();
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
        return "PolarAir";
    }

    @Override
    public final double getCustomField() {
        return this.iceCrystalConcentration;
    }

    @Override
    public final String getCustomFieldName() {
        return "iceCrystalConcentration";
    }

    @Override
    public final void calculateAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_MULT)
                + (ROUND_VALUE - Math.abs(temperature))
                - (iceCrystalConcentration * ICE_MULT);
    }

    @Override
    public final double calculateAirToxicity() {
        double toxicityAQ = ROUND_VALUE * (1 - getAirQuality() / TOXICITY_VALUE);
        double result = Math.round(toxicityAQ * ROUND_VALUE) / ROUND_VALUE;
        return normalizeValue(result);
    }

    @Override
    public final void changeWeather(final CommandInput command) {
        double windSpeed = command.getWindSpeed();
        this.airQuality = calculateAirToxicity() - (windSpeed * WIND_MULT);
    }
}
