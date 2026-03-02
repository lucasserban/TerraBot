package simulation.entities.soils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import simulation.Pair;
import simulation.Simulation;
import simulation.entities.Entity;

public abstract class Soil extends Entity {
    private static final double ROUND_VALUE = 100.0;
    private static final double POORQUALITY_VALUE = 40;
    private static final double MODERATEQUALITY_VALUE = 70;
    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;

    Soil() {
        super();
        this.nitrogen = 0;
        this.waterRetention = 0;
        this.soilpH = 0;
        this.organicMatter = 0;
    }

    public Soil(final SoilInput soil, final Pair position) {
        super(soil.getName(), soil.getMass(), position);
        this.nitrogen = soil.getNitrogen();
        this.waterRetention = soil.getWaterRetention();
        this.soilpH = soil.getSoilpH();
        this.organicMatter = soil.getOrganicMatter();
    }

    /**
     *
     * @return the nitrogen of the soil
     */
    public double getNitrogen() {
        return normalizeValue(nitrogen);
    }

    /**
     *
     * @return the waterRetention of the soil
     */
    public double getWaterRetention() {
        return normalizeValue(waterRetention);
    }

    /**
     *
     * @return the soilPH of the soil
     */
    public double getSoilpH() {
        return normalizeValue(soilpH);
    }

    /**
     *
     * @return the organicMatter
     */
    public double getOrganicMatter() {
        return normalizeValue(organicMatter);
    }

    /**
     * sets the organicMatter
     * @param organicMatter
     */
    public void setOrganicMatter(final double organicMatter) {
        this.organicMatter = normalizeValue(organicMatter);
    }

    /**
     * grows the organicMatter
     * @param organicMatterAdded
     */
    public void growOrganicMatter(final double organicMatterAdded) {
        organicMatter += organicMatterAdded;
    }

    /**
     *
     * @return the type of the soil
     */
    public abstract String getType();

    /**
     *
     * @return the custom field of the soil type
     */
    public abstract double getCustomField();

    /**
     *
     * @return the custom fields name of the soil type
     */
    public abstract String getCustomFieldName();

    /**
     * calculates the soil quality
     */
    public abstract void calculateSoilQuality();

    /**
     *
     * @return the posibility for a robot to get stuck in the soil
     */
    public abstract double posibilityToGetStuck();

    /**
     *
     * @return the soil Quality
     */
    public abstract double getSoilQuality();

    /**
     * it adds to the waterRetention field
     * @param waterRetentionAdded adds up to the field
     */
    public void growWaterRetention(final double waterRetentionAdded) {
        waterRetention += waterRetentionAdded;
    }

    /**
     * it prints the details of the soil
     * @param out where the details will be written
     * @param simulation
     * @return the output
     */
    public ObjectNode printDetails(final ObjectNode out, final Simulation simulation) {
        ObjectNode result = out;
        result = super.printDetails(result, simulation);
        result.put("nitrogen", getNitrogen());
        result.put("waterRetention",  getWaterRetention());
        result.put("soilpH", getSoilpH());
        result.put("organicMatter", getOrganicMatter());
        result.put("soilQuality", normalizeSoilQuality(getSoilQuality()));
        result.put(getCustomFieldName(), getCustomField());
        return result;
    }

    /**
     * It normalized soilQuality value
     * @param soilQuality the value to be normalized
     * @return the normalized value
     */
    public double normalizeSoilQuality(final double soilQuality) {
        double res = Math.max(0, Math.min(ROUND_VALUE, soilQuality));
        res = Math.round(res * ROUND_VALUE) / ROUND_VALUE;
        return res;
    }

    /**
     *
     * @return the soil quality in String mode
     */
    public String getSoilQualityResult() {
        double quality = normalizeSoilQuality(getSoilQuality());
        if  (quality < POORQUALITY_VALUE) {
            return "poor";
        } else if (quality < MODERATEQUALITY_VALUE) {
            return "moderate";
        } else {
            return "good";
        }
    }

    /**
     * It creates the type of soil needed
     * @param soil
     * @param position
     * @return the type of soil
     */
    public static Soil createSoil(final SoilInput soil, final Pair position) {
        switch (soil.getType()) {
            case "DesertSoil": //
                return new DesertSoil(soil, position);
            case "ForestSoil":
                return new ForestSoil(soil, position);
            case "GrasslandSoil":
                return new GrasslandSoil(soil, position);
            case "SwampSoil":
                return new SwampSoil(soil, position);
            case "TundraSoil":
                return new TundraSoil(soil, position);
            default:
                return null;
        }
    }


}
