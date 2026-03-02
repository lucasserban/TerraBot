package simulation;

//this class represents a square of the map, and I made
//the fields public because I want to access them faster in
//Territory class, even if I could have make the class empty
//and still work as I would have like, I think it makes
//the implementation more clear

import simulation.entities.airs.Air;
import simulation.entities.animals.Animal;
import simulation.entities.plants.Plant;
import simulation.entities.soils.Soil;
import simulation.entities.waters.Water;

//this class represents a square of the map
public class TerritorySquare {
    private Plant plant;
    private Animal animal;
    private Air air;
    private Soil soil;
    private Water water;

    TerritorySquare() {
        plant = null;
        animal = null;
        air = null;
        soil = null;
        water = null;
    }

    /**
     *
     * @return the plant
     */
    public Plant getPlant() {
        return plant;
    }

    /**
     * sets the plant
     * @param plant
     */
    public void setPlant(final Plant plant) {
        this.plant = plant;
    }

    /**
     *
     * @return the animal
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * sets the animal
     * @param animal
     */
    public void setAnimal(final Animal animal) {
        this.animal = animal;
    }

    /**
     *
     * @return the air
     */
    public Air getAir() {
        return air;
    }

    /**
     * sets the air
     * @param air
     */
    public void setAir(final Air air) {
        this.air = air;
    }

    /**
     *
     * @return the soil
     */
    public Soil getSoil() {
        return soil;
    }

    /**
     * sets the soil
     * @param soil
     */
    public void setSoil(final Soil soil) {
        this.soil = soil;
    }

    /**
     *
     * @return the water
     */
    public Water getWater() {
        return water;
    }

    /**
     * sets the water
     * @param water
     */
    public void setWater(final Water water) {
        this.water = water;
    }

    /**
     *
     * @return the number of entities non-null
     */
    public int getNumberOfEntities() {
        int nr = 0;
        if (plant != null) {
            nr++;
        }
        if (animal != null) {
            nr++;
        }
        if (water != null) {
            nr++;
        }
        return nr;
    }

    /**
     * it s a helper method to calculate the quality score of the square
     * @return number of entities
     */
    private int getNumberOfQualityEntities() {
        int nr = 0;
        if (plant != null) {
            nr++;
        }
        if (animal != null) {
            nr++;
        }
        if (soil != null) {
            nr++;
        }
        if (air != null) {
            nr++;
        }
        return nr;
    }

    /**
     *
     * @return the score quality
     */
    public int squareQuality() {
        double score = (plant == null) ? 0 : plant.posibilityToGetStuck();
        score += (animal == null) ? 0 : animal.posibilityToAtack();
        score += (soil == null) ? 0 : soil.posibilityToGetStuck();
        score += (air == null) ? 0 : air.calculateAirToxicity();
        score = Math.abs(score / getNumberOfQualityEntities());
        return (int) Math.round(score);
    }

    /**
     * calculate the best neighbor so that the robot will move that way
     * @param position current position of robot
     * @param map
     * @param robot
     * @return the coordinates of the best neighbor
     */
    public int getBestNeighbor(final Pair position, final Territory map, final Robot robot) {
        int x = position.getX();
        int y = position.getY();

        Pair limits = map.getDims();
        int maxX = limits.getX();
        int maxY = limits.getY();

        Pair result = new Pair(position);
        int bestQualityScore = Integer.MAX_VALUE;

        if (y + 1 < maxY) {
            int currQualityScore = map.getInfo(x, y + 1).squareQuality();
            if (currQualityScore < bestQualityScore) {
                if (robot.canUseEnergy(currQualityScore)) {
                bestQualityScore = map.getInfo(x, y + 1).squareQuality();
                result.setX(x);
                result.setY(y + 1);
                }
            }
        }

        if (x + 1 < maxX) {
            int currQualityScore = map.getInfo(x + 1, y).squareQuality();
            if (currQualityScore < bestQualityScore) {
                if (robot.canUseEnergy(currQualityScore)) {
                    bestQualityScore = map.getInfo(x + 1, y).squareQuality();
                    result.setX(x + 1);
                    result.setY(y);
                }
            }
        }

        if (y - 1 >= 0) {
            int currQualityScore = map.getInfo(x, y - 1).squareQuality();
            if (currQualityScore < bestQualityScore) {
                if (robot.canUseEnergy(currQualityScore)) {
                    bestQualityScore = map.getInfo(x, y - 1).squareQuality();
                    result.setX(x);
                    result.setY(y - 1);
                }
            }
        }

        if (x - 1 >= 0) {
            int currQualityScore = map.getInfo(x - 1, y).squareQuality();
            if (currQualityScore < bestQualityScore) {
                if (robot.canUseEnergy(currQualityScore)) {
                    bestQualityScore = map.getInfo(x - 1, y).squareQuality();
                    result.setX(x - 1);
                    result.setY(y);
                }
            }
        }

        if (bestQualityScore ==  Integer.MAX_VALUE) {
            return 0;
        }
        robot.useEnergy(bestQualityScore);
        robot.setPosition(result);
        return 1;
    }

    /**
     * calculates if there is plant and water in the square
     * @param robot
     * @return will return 3 if it has plant and water, 2 just for plant, 1 just for water, -1 if neither
     */
    public int doesItHavePlantAndWater(final Robot robot) {
        if (robot.containsEntity(plant) && robot.containsEntity(water)) {
            return 1;
        } else if (robot.containsEntity(plant) && !robot.containsEntity(water)) {
            return 2;
        } else  if (!robot.containsEntity(plant) && robot.containsEntity(water)) {
            return 3;
        } else {
            return -1;
        }
    }

}
