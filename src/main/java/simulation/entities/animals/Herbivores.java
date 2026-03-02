package simulation.entities.animals;

import fileio.AnimalInput;
import simulation.Pair;

public class Herbivores extends Animal {
    private static final double POSSIBILITY_ATACK = 1.5;

    Herbivores(final AnimalInput animal, final Pair position) {
        super(animal,  position);
    }

    @Override
    public final String getType() {
        return "Herbivores";
    }

    @Override
    public final double posibilityToAtack() {
        return normalizeValue(POSSIBILITY_ATACK);
    }
}
