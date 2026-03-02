package simulation.entities.airs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.AirInput;
import fileio.CommandInput;
import simulation.Pair;
import simulation.Simulation;
import simulation.entities.Entity;
public abstract class Air extends Entity {
    private static final double NORMALIZE_VALUE = 100.0;
    private static final double POOR_VALUE = 40;
    private static final double MEDIUM_VALUE = 70;
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;

    public Air(final AirInput air, final Pair position) {
        super(air.getName(), air.getMass(), position);
        this.humidity = air.getHumidity();
        this.temperature = air.getTemperature();
        this.oxygenLevel = air.getOxygenLevel();
    }

    /**
     *
     * @return humidity of air
     */
    public double getHumidity() {
        return normalizeValue(humidity);
    }

    /**
     *
     * @return temperature of air
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     *
     * @return oxygen level of air
     */
    public double getOxygenLevel() {
        return normalizeValue(oxygenLevel);
    }

    /**
     * It grows the oxygenLevel of the entity
     * @param oxygenLevelAdded
     */
    public void growOxygenLevel(final double oxygenLevelAdded) {
        oxygenLevel += oxygenLevelAdded;
    }

    /**
     * It grows humidity of the entity
     * @param humidityAdded
     */
    public void growHumidity(final double humidityAdded) {
        humidity += humidityAdded;
    }

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract String getType();

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract double getCustomField();

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract String getCustomFieldName();

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract double getAirQuality();

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract void calculateAirQuality();

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract double calculateAirToxicity();

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract void changeWeather(CommandInput command);

    /**
     * It prints the parameters of the ait entity, needed in printEnvCondition command
     * @param out
     * @param simulation
     * @return it returns the output of the method
     */
    public ObjectNode printDetails(final ObjectNode out, final Simulation simulation) {
        ObjectNode result = out;
        result = super.printDetails(result, simulation);
        result.put("type", getType());
        result.put("name", getName());
        result.put("mass", getMass());
        result.put("humidity", getHumidity());
        result.put("temperature", getTemperature());
        result.put("oxygenLevel", getOxygenLevel());
        result.put("airQuality", normalizeAirQuality(getAirQuality()));
        if (getType().equals("DesertAir")) {
            result.put(getCustomFieldName(), simulation.getIsChangeWeather());
        } else {
            result.put(getCustomFieldName(), getCustomField());
        }

        return result;
    }

    /**
     * Normalizez the airQuality by the given formula
     * @param airQuality
     * @return the normalized air quality
     */
    public double normalizeAirQuality(final double airQuality) {
        double res = Math.max(0, Math.min(NORMALIZE_VALUE, airQuality));
        res = Math.round(res * NORMALIZE_VALUE) / NORMALIZE_VALUE;
        return res;
    }

    /**
     *
     * @return air quality in text
     */
    public String getAirQualityResult() {
        double quality = normalizeAirQuality(getAirQuality());
        if  (quality < POOR_VALUE) {
            return "poor";
        } else if (quality < MEDIUM_VALUE) {
            return "moderate";
        } else {
            return "good";
        }
    }

    /**
     * It creates a subclass of the Air class
     * @param air
     * @param position on the map
     * @return the entity needed
     */
    public static Air createAir(final AirInput air, final Pair position) {
        switch (air.getType()) {
            case "DesertAir": //
                return new DesertAir(air, position);
            case "MountainAir":
                return new MountainAir(air, position);
            case "PolarAir":
                return new PolarAir(air, position);
            case "TemperateAir":
                return new TemperateAir(air, position);
            case "TropicalAir":
                return new TropicalAir(air, position);
            default:
                return null;
        }
    }
}
