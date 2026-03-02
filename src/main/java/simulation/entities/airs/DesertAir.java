package simulation.entities.airs;

import fileio.AirInput;
import fileio.CommandInput;
import simulation.Pair;

public class DesertAir extends Air {
    private static final double OXYGEN_MULT = 2;
    private static final double DUST_MULT = 0.2;
    private static final double TEMPERATURE_MULT = 0.3;
    private static final double ROUND_VALUE = 100.0;
    private static final double TOXICITY_VALUE = 65;
    private static final double STORM_VALUE = 30;
    private double dustParticles;
    private double airQuality;
    private boolean desertStorm;

    public DesertAir(final AirInput air, final Pair position) {
        super(air, position);
        this.dustParticles = air.getDustParticles();
        calculateAirQuality();
    }

    /**
     * It sets the airQuality
     * @param airQuality
     */
    public void setAirQuality(final double airQuality) {
        this.airQuality = airQuality;
    }

    /**
     * checks if is a desert storm
     * @return
     */
    public boolean isDesertStorm() {
        return desertStorm;
    }

    @Override
    public final double getAirQuality() {
        return this.airQuality;
    }

    @Override
    public final String getType() {
        return "DesertAir";
    }

    @Override
    public final double getCustomField() {
        return this.dustParticles;
    }

    @Override
    public final String getCustomFieldName() {
        return "desertStorm";
    }

    @Override
    public final void calculateAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_MULT)
                - (dustParticles * DUST_MULT)
                - (temperature * TEMPERATURE_MULT);
        desertStorm = false;
    }

    @Override
    public final double calculateAirToxicity() {
        double toxicityAQ = ROUND_VALUE * (1 - getAirQuality() / TOXICITY_VALUE);
        double result = Math.round(toxicityAQ * ROUND_VALUE) / ROUND_VALUE;
        return normalizeValue(result);
    }

    @Override
    public final void changeWeather(final CommandInput command) {
        boolean isDesertStorm = command.isDesertStorm();
        this.airQuality = getAirQuality() - (isDesertStorm ? STORM_VALUE : 0);
        desertStorm = true;
    }
}
