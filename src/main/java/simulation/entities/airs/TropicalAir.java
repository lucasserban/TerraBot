package simulation.entities.airs;

import fileio.AirInput;
import fileio.CommandInput;
import simulation.Pair;

public class TropicalAir extends Air {
    private static final double ROUND_VALUE = 100.0;
    private static final double TOXICITY_VALUE = 82;
    private static final double RAINFALL_VALUE = 0.3;
    private static final double OXYGEN_MULT = 2;
    private static final double HUMIDITY_MULT = 0.5;
    private static final double CO2LEVEL_MULT = 0.01;
    private double co2Level;
    private double airQuality;

    TropicalAir(final AirInput air, final Pair position) {
        super(air, position);
        this.co2Level = Math.round(air.getCo2Level() * ROUND_VALUE) / ROUND_VALUE;
        calculateAirQuality();
    }

    /**
     * sets air qualitu
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
        return "TropicalAir";
    }

    @Override
    public final double getCustomField() {
        return this.co2Level;
    }

    @Override
    public final String getCustomFieldName() {
        return "co2Level";
    }

    @Override
    public final void calculateAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_MULT)
                + (humidity * HUMIDITY_MULT)
                - (co2Level * CO2LEVEL_MULT);

    }

    @Override
    public final double calculateAirToxicity() {
        double toxicityAQ = ROUND_VALUE * (1 - getAirQuality() / TOXICITY_VALUE);
        double result = Math.round(toxicityAQ * ROUND_VALUE) / ROUND_VALUE;
        return normalizeValue(result);
    }

    @Override
    public final void changeWeather(final CommandInput command) {
        double rainfall = command.getRainfall();
        this.airQuality = calculateAirToxicity() - (rainfall * RAINFALL_VALUE);
    }
}
