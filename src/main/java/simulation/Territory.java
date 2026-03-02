package simulation;

import fileio.AnimalInput;
import fileio.PlantInput;
import fileio.SimulationInput;
import fileio.WaterInput;
import fileio.AirInput;
import fileio.SoilInput;
import simulation.entities.airs.Air;
import simulation.entities.animals.Animal;
import simulation.entities.plants.Plant;
import simulation.entities.soils.Soil;
import simulation.entities.waters.Water;

import java.util.ArrayList;
import java.util.Iterator;

public class Territory {
    private static final int BOTH_ENTITIES = 3;
    private String dim;
    //I stocked the territory info into a matrix of TerritorySquare objects
    private TerritorySquare[][] info;

    /**
     * In this constructor I processed the input info of the map
     * @param simulationInput is the input info of the territory
     */
    public Territory(final SimulationInput simulationInput) {
        this.dim = simulationInput.getTerritoryDim();

        String[] dims = this.dim.split("x");
        int x = Integer.parseInt(dims[0]);
        int y = Integer.parseInt(dims[1]);
        this.info = new TerritorySquare[x][y];

        //here I declare the matrix
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                this.info[i][j] = new TerritorySquare();
            }
        }

        //There I process the plants from the input
        for (PlantInput plant : simulationInput.getTerritorySectionParams().getPlants()) {
            for (int i = 0; i < plant.getSections().size(); i++) {
                int m = plant.getSections().get(i).getX();
                int n = plant.getSections().get(i).getY();
                Pair position = new Pair(m, n);
                info[m][n].setPlant(Plant.createPlant(plant, position));
            }
        }

        //Here I process the animals from the input
        for (AnimalInput animal : simulationInput.getTerritorySectionParams().getAnimals()) {
            for (int i = 0; i < animal.getSections().size(); i++) {
                int m = animal.getSections().get(i).getX();
                int n = animal.getSections().get(i).getY();
                Pair position = new Pair(m, n);
                info[m][n].setAnimal(Animal.createAnimal(animal, position));
            }
        }

        //Here I process the water from the input
        for (WaterInput water : simulationInput.getTerritorySectionParams().getWater()) {
            for (int i = 0; i < water.getSections().size(); i++) {
                int m = water.getSections().get(i).getX();
                int n = water.getSections().get(i).getY();
                Pair position = new Pair(m, n);
                info[m][n].setWater(Water.createWater(water, position));
            }
        }

        //Here I process the air from the input
        for (AirInput air : simulationInput.getTerritorySectionParams().getAir()) {
            for (int i = 0; i < air.getSections().size(); i++) {
                int m = air.getSections().get(i).getX();
                int n = air.getSections().get(i).getY();
                Pair position = new Pair(m, n);
                info[m][n].setAir(Air.createAir(air, position));
            }
        }

        //Here I process the soil from the input
        for (SoilInput soil : simulationInput.getTerritorySectionParams().getSoil()) {
            for (int i = 0; i < soil.getSections().size(); i++) {
                int m = soil.getSections().get(i).getX();
                int n = soil.getSections().get(i).getY();
                Pair position = new Pair(m, n);
                info[m][n].setSoil(Soil.createSoil(soil, position));
            }
        }
    }

    /**
     *
     * @return the territory dimensions
     */
    public String getTerritoryDim() {
        return this.dim;
    }

    /**
     *
     * @param x
     * @param y
     * @return the square at the (x, y) coordinates
     */
    public TerritorySquare getInfo(final int x, final int y) {
        return this.info[x][y];
    }

    /**
     *
     * @return the dimensions of the map
     */
    public Pair getDims() {
        String[] dims = this.dim.split("x");
        int x = Integer.parseInt(dims[0]);
        int y = Integer.parseInt(dims[1]);
        return new Pair(x, y);
    }

    /**
     * it calculates which position should the scanned animal go
     * @param position
     * @param animalType
     * @param robot
     * @return the square in which the animal will move
     */
    public TerritorySquare animalBestNeighbor(final Pair position, final String animalType,
                                              final Robot robot) {

        int x = position.getX();
        int y = position.getY();
        TerritorySquare up = null;
        TerritorySquare down = null;
        TerritorySquare left = null;
        TerritorySquare right = null;
        Pair limits = getDims();
        int xLimit = limits.getX();
        int yLimit = limits.getY();
        boolean isPredator = false;
        if (animalType.equals("Carnivores") || animalType.equals("Parasites")) {
            isPredator = true;
        }

        if (x - 1 >= 0) {
            up = getInfo(x - 1, y);
        }
        if (x + 1 < xLimit) {
            down = getInfo(x + 1, y);
        }
        if (y - 1 >= 0) {
            left = getInfo(x, y - 1);
        }
        if (y + 1 < yLimit) {
            right = getInfo(x, y + 1);
        }

        ArrayList<TerritorySquare> neighbors = new ArrayList<>();

        if (up != null) {
            neighbors.add(up);
        }

        if (right != null) {
            neighbors.add(right);
        }
        if (down != null) {
            neighbors.add(down);
        }
        if (left != null) {
            neighbors.add(left);
        }

        Iterator<TerritorySquare> it3 = neighbors.iterator();
        if (!isPredator) {
            while (it3.hasNext()) {
                TerritorySquare neighbor = it3.next();
                if (neighbor.getAnimal() != null) {
                    it3.remove();
                }
            }
        }

        int max = neighbors.get(0).doesItHavePlantAndWater(robot);
        for  (TerritorySquare neighbor : neighbors) {
            if (neighbor.doesItHavePlantAndWater(robot) > max) {
                max = neighbor.doesItHavePlantAndWater(robot);
            }
        }

        //if there is at least a neighbor with plant and water
        if (max == BOTH_ENTITIES) {
            Iterator<TerritorySquare> it2 = neighbors.iterator();
            while (it2.hasNext()) {
                TerritorySquare neighbor = it2.next();
                if (neighbor.doesItHavePlantAndWater(robot) != BOTH_ENTITIES) {
                    it2.remove();
                }
            }

            double bestScore = Integer.MIN_VALUE;
            for (TerritorySquare neighbor : neighbors) {
                if (neighbor.getWater().getWaterQuality() > bestScore) {
                    bestScore = neighbor.getWater().getWaterQuality();
                }
            }

            for (TerritorySquare neighbor : neighbors) {
                if (neighbor.getWater().getWaterQuality() == bestScore) {
                    return neighbor;
                }
            }
        } else if (max == 2) {
            return neighbors.get(0);
        } else if (max == 1) {
            double bestScore = Integer.MIN_VALUE;
            for (TerritorySquare neighbor : neighbors) {
                if (neighbor.doesItHavePlantAndWater(robot) == 1
                        && neighbor.getWater().getWaterQuality() > bestScore) {
                    bestScore = neighbor.getWater().getWaterQuality();
                }
            }

            for (TerritorySquare neighbor : neighbors) {
                if (neighbor.doesItHavePlantAndWater(robot) == 1
                        && neighbor.getWater().getWaterQuality() == bestScore) {
                    return neighbor;
                }
            }
        }
        return neighbors.get(0);
    }

    /**
     * it calculates the coordinates of a TerritorySquare object
     * @param square
     * @return the coordinates of the square
     */
    public Pair locationOfSquare(final TerritorySquare square) {
        Pair location = getDims();
        for (int i = 0; i < location.getX(); i++) {
            for (int j = 0; j < location.getY(); j++) {
                if (getInfo(i, j) == square) {
                    return new Pair(i, j);
                }
            }
        }
        return null;
    }

    /**
     * checks if there is a type of entity anywhere on the map
     * @param type
     * @return true if exists, false if not
     */
    public boolean doesTypeOfEntityExist(final String type) {
        boolean result = false;
        for (TerritorySquare[] square : info) {
            for (TerritorySquare square1 : square) {
                if (square1.getAir().getType().equals(type)) {
                    result = true;
                    break;
                }
            }
            if (result) {
                break;
            }
        }
        return result;
    }

}
