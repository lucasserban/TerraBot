package simulation.entities.waters;

import fileio.WaterInput;
import simulation.Pair;
import simulation.Robot;
import simulation.Territory;
import simulation.entities.Entity;
import simulation.entities.airs.Air;
import simulation.entities.animals.Animal;
import simulation.entities.plants.Plant;
import simulation.entities.soils.Soil;

public abstract class Water extends Entity {
    private static final double GROW_VALUE = 0.1;
    private double purity;
    private double salinity;
    private double turbidity;
    private double contaminantIndex;
    private double pH;
    private boolean isFrozen;
    private int timestampesToWait = 2;
    protected double waterQuality;
    protected boolean isScanned = false;

    public Water(final WaterInput water, final Pair position) {
        super(water.getName(), water.getMass(), position);
        this.purity = water.getPurity();
        this.salinity = water.getSalinity();
        this.turbidity = water.getTurbidity();
        this.contaminantIndex = water.getContaminantIndex();
        this.pH = water.getPH();
        this.isFrozen = false;
    }

    /**
     *
     * @return the purity of the water
     */
    public double getPurity() {
        return purity;
    }

    /**
     *
     * @return the salinity of the water
     */
    public double getSalinity() {
        return salinity;
    }

    /**
     *
     * @return the turbidity of the water
     */
    public double getTurbidity() {
        return turbidity;
    }

    /**
     *
     * @return the condaminantIndex of the water
     */
    public double getContaminantIndex() {
        return contaminantIndex;
    }

    /**
     *
     * @return the pH of the water
     */
    public double getpH() {
        return pH;
    }

    /**
     *
     * @return if the water is Frozen
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     *
     * @return the water quality
     */
    public double getWaterQuality() {
        return waterQuality;
    }

    /**
     * sets the water as scanned
     */
    public void markAsScanned() {
        isScanned = true;
    }

    /**
     *
     * @return if the water is scanned or not
     */
    public boolean getIsScanned() {
        return isScanned;
    }

    /**
     *
     * @return the type of the water
     */
    public abstract String getType();

    /**
     * It initializez the type of water
     * @param water
     * @param position
     * @return the type of water
     */
    public static Water createWater(final WaterInput water, final Pair position) {
        switch (water.getType()) {
            case "lake":
                return new Lake(water, position);
            case "river":
                return new River(water, position);
            case "pond":
                return new Pond(water, position);
            default:
                return null;
        }
    }

    @Override
    public final void interaction(final Soil soil, final Water water, final Air air,
                            final Plant plant, final Animal animal, final Territory map,
                            final Pair position, final Robot robot) {
        timestampesToWait--;
        if (timestampesToWait <= 0) {
            air.growHumidity(GROW_VALUE);
            soil.growWaterRetention(GROW_VALUE);
            air.calculateAirQuality();
            soil.calculateSoilQuality();
            timestampesToWait = 2;
        }
    }
}
