package simulation.entities.plants;

import fileio.PlantInput;
import simulation.Pair;
import simulation.Robot;
import simulation.Territory;
import simulation.entities.Entity;
import simulation.entities.airs.Air;
import simulation.entities.animals.Animal;
import simulation.entities.soils.Soil;
import simulation.entities.waters.Water;

public abstract class Plant extends Entity {
    private static final double GROWTH_YOUNG = 0.2;
    private static final double GROWTH_MATURE = 0.7;
    private static final double GROWTH_OLD = 0.4;
    private static final double GROWTH_PLANT = 0.2;
    protected String maturity = "Young";
    protected double nextLevel;
    protected double maturityGrowth;
    protected boolean isScanned = false;

    public Plant() {
        super();
    }

    public Plant(final PlantInput plant, final Pair position) {
        super(plant.getName(), plant.getMass(), position);
    }

    /**
     * the method it's passed to plant's subclasses
     * @return the type of the entity
     */
    public abstract String getType();

    /**
     * the method it's passed to plant's subclasses
     * @return the posibility of a plant to get stuck
     */
    public abstract double posibilityToGetStuck();

    /**
     * this method initializez a plant entity
     * @param plant
     * @param position
     * @return the type of plant
     */
    public static Plant createPlant(final PlantInput plant, final Pair position) {
        switch (plant.getType()) {
            case "Algae": //
                return new Algae(plant, position);
            case "Ferns":
                return new Ferns(plant, position);
            case "FloweringPlants":
                return new FloweringPlants(plant, position);
            case "GymnospermsPlants":
                return new Gymnosperms(plant, position);
            case "Mosses":
                return new Mosses(plant, position);
            default:
                return null;
        }
    }

    /**
     * sets the plant as scanned
     */
    public void markAsScanned() {
        isScanned = true;
    }

    /**
     *
     * @return if the plant is scanned
     */
    public boolean getIsScanned() {
        return isScanned;
    }

    /**
     * Updates the status of this
     */
    public void setMaturityGrowth() {
        if (maturity.equals("Young")) {
            maturityGrowth = GROWTH_YOUNG;
        } else if (maturity.equals("Mature")) {
            maturityGrowth = GROWTH_MATURE;
        } else if (maturity.equals("Old")) {
            maturityGrowth = GROWTH_OLD;
        }
    }

    /**
     * It manages the interaction beetween plant and other entities
     * @param soil
     * @param water
     * @param air
     * @param plant
     * @param animal
     * @param map
     * @param position
     * @param robot
     */
    @Override
    public void interaction(final Soil soil, final Water water, final Air air,
                                  final Plant plant, final Animal animal, final Territory map,
                                  final Pair position, final Robot robot) {
        if (soil != null) {
            nextLevel += GROWTH_PLANT;
        }

        if (water != null) {
            nextLevel += GROWTH_PLANT;
        }

        if (nextLevel >= 1) {
            if (maturity.equals("Young")) {
                maturity = "Mature";
            } else if (maturity.equals("Mature")) {
                maturity = "Old";
            } else if (maturity.equals("Old")) {
                maturity = "Dead";
                int x = position.getX();
                int y = position.getY();
                map.getInfo(x, y).setPlant(null);
            }

            nextLevel = 0;
        }
        setMaturityGrowth();
    }

}
