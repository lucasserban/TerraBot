package simulation.entities.animals;

import fileio.AnimalInput;
import simulation.Pair;

public class Carnivores extends Animal {
    private static final double POSSIBILITY_ATACK = 7.0;
    Carnivores(final AnimalInput animal, final Pair position) {
        super(animal,  position);
    }

    @Override
    public final String getType() {
        return "Carnivores";
    }

    @Override
    public final double posibilityToAtack() {
        return normalizeValue(POSSIBILITY_ATACK);
    }
}
