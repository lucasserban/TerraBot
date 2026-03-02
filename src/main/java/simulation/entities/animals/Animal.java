package simulation.entities.animals;

import fileio.AnimalInput;
import simulation.Pair;
import simulation.Robot;
import simulation.Territory;
import simulation.TerritorySquare;
import simulation.entities.Entity;
import simulation.entities.airs.Air;
import simulation.entities.plants.Plant;
import simulation.entities.soils.Soil;
import simulation.entities.waters.Water;

public abstract class Animal extends Entity {
    private static final double WATER_MULT = 0.8;
    private static final double PARAM_ADDED = 0.5;
    private static final double MASS_MULT = 0.08;
    protected int timestampesToWait = 2;
    protected boolean isScanned = false;


    public Animal(final AnimalInput animal, final Pair position) {
        super(animal.getName(), animal.getMass(), position);
    }

    /**
     * Marks the animal as scaned
     */
    public void markAsScanned() {
        isScanned = true;
    }

    /**
     * It passes the method to it s sublasses
     * @return
     */
    public abstract String getType();

    /**
     * It passes the method to it's subclasses
     * @return
     */
    public abstract double posibilityToAtack();

    /**
     * This creates a specific type of animal
     * @param animal
     * @param position on the map
     * @return
     */
    public static Animal createAnimal(final AnimalInput animal, final Pair position) {
        switch (animal.getType()) {
            case "Carnivores": //
                return new Carnivores(animal, position);
            case "Herbivores":
                return new Herbivores(animal, position);
            case "Omnivores":
                return new Omnivores(animal, position);
            case "Detritivores":
                return new Detritivores(animal, position);
            case "Parasites":
                return new Parasites(animal, position);
            default:
                return null;
        }
    }

    @Override
    public final void interaction(final Soil soil, final Water water, final Air air,
                                  final Plant plant, final Animal animal, final Territory map,
                                  final Pair position, final Robot robot) {
        timestampesToWait--;
        TerritorySquare newLocation = map.getInfo(position.getX(), position.getY());

        Soil newSoil = newLocation.getSoil();
        Water newWater = newLocation.getWater();
        Plant newPlant = newLocation.getPlant();

        boolean isPredator = false;
        if (getType().equals("Carnivores") ||  getType().equals("Parasites")) {
            isPredator = true;
        }

            if (newWater != null && newPlant != null) {
                if (newPlant.getIsScanned() && newWater.getIsScanned()) {
                    double waterToDrink = Math.min(mass * MASS_MULT, newWater.getMass());
                    newWater.setMass(newWater.getMass() - waterToDrink);
                    if (newWater.getMass() <= 0) {
                        newLocation.setWater(null);
                    }
                    mass += waterToDrink + newPlant.getMass();
                    newSoil.setOrganicMatter(newSoil.getOrganicMatter() + WATER_MULT);
                    newLocation.setPlant(null);
                } else if (newWater.getIsScanned()) {
                    double waterToDrink = Math.min(mass * MASS_MULT, newWater.getMass());
                    newWater.setMass(newWater.getMass() - waterToDrink);
                    if (newWater.getMass() <= 0) {
                        newLocation.setWater(null);
                    }
                    mass += waterToDrink;
                    newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
                } else if (newPlant.getIsScanned()) {
                    mass += newPlant.getMass();
                    newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
                    newLocation.setPlant(null);
                }
            } else if (newWater != null && newPlant == null && newWater.getIsScanned()) {
                double waterToDrink = Math.min(mass * MASS_MULT, newWater.getMass());
                newWater.setMass(newWater.getMass() - waterToDrink);
                if (newWater.getMass() <= 0) {
                    newLocation.setWater(null);
                }
                mass += waterToDrink;
                newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
            } else if (newWater == null && newPlant != null && newPlant.getIsScanned()) {
                mass += newPlant.getMass();
                newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
                newLocation.setPlant(null);
            }

        if (timestampesToWait <= 0) {
            newLocation = map.animalBestNeighbor(position, getType(), robot);
            Pair newCoordinates = map.locationOfSquare(newLocation);
            map.getInfo(position.getX(), position.getY()).setAnimal(null);
            setPosition(newCoordinates);
            Soil newSoil2 = newLocation.getSoil();
            if (newLocation.getAnimal() != null && isPredator) {
                double prayMass = newLocation.getAnimal().getMass();
                setMass(this.mass + prayMass);
                newSoil2.setOrganicMatter(newSoil2.getOrganicMatter() + PARAM_ADDED);
            }
            if (!isPredator) {
                if (newWater != null && newPlant != null) {
                    if (newPlant.getIsScanned() && newWater.getIsScanned()) {
                        double waterToDrink = Math.min(mass * MASS_MULT, newWater.getMass());
                        newWater.setMass(newWater.getMass() - waterToDrink);
                        if (newWater.getMass() <= 0) {
                            newLocation.setWater(null);
                        }
                        mass += waterToDrink + newPlant.getMass();
                        newSoil.setOrganicMatter(newSoil.getOrganicMatter() + WATER_MULT);
                        newLocation.setPlant(null);
                    } else if (newWater.getIsScanned()) {
                        double waterToDrink = Math.min(mass * MASS_MULT, newWater.getMass());
                        newWater.setMass(newWater.getMass() - waterToDrink);
                        if (newWater.getMass() <= 0) {
                            newLocation.setWater(null);
                        }
                        mass += waterToDrink;
                        newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
                    } else if (newPlant.getIsScanned()) {
                        mass += newPlant.getMass();
                        newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
                        newLocation.setPlant(null);
                    }
                } else if (newWater != null && newPlant == null && newWater.getIsScanned()) {
                    double waterToDrink = Math.min(mass * MASS_MULT, newWater.getMass());
                    newWater.setMass(newWater.getMass() - waterToDrink);
                    if (newWater.getMass() <= 0) {
                        newLocation.setWater(null);
                    }
                    mass += waterToDrink;
                    newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
                } else if (newWater == null && newPlant != null && newPlant.getIsScanned()) {
                    mass += newPlant.getMass();
                    newSoil.setOrganicMatter(newSoil.getOrganicMatter() + PARAM_ADDED);
                    newLocation.setPlant(null);
                }
            }
            newLocation.setAnimal(this);
            timestampesToWait = 2;
        }

    }
}
