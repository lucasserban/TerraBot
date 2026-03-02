package simulation.entities.plants;

import fileio.PlantInput;
import simulation.Pair;
import simulation.Robot;
import simulation.Territory;
import simulation.entities.airs.Air;
import simulation.entities.animals.Animal;
import simulation.entities.soils.Soil;
import simulation.entities.waters.Water;

public class Mosses extends Plant {
    private static final double STUCK_VALUE = 0.4;
    private static final double BASEGROWTH = 0.8;

    Mosses(final PlantInput plant, final Pair position) {
        super(plant, position);
    }

    @Override
    public final String getType() {
        return "Mosses";
    }

    @Override
    public final double posibilityToGetStuck() {
        return STUCK_VALUE;
    }

    @Override
    public final void interaction(final Soil soil, final Water water, final Air air,
                                  final Plant plant, final Animal animal, final Territory map,
                                  final Pair position, final Robot robot) {
        super.interaction(soil, water, air, plant, animal, map, position, robot);
        air.growOxygenLevel(BASEGROWTH + maturityGrowth);
        air.calculateAirQuality();
    }
}
