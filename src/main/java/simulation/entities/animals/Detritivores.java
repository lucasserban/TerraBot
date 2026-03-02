package simulation.entities.animals;

import fileio.AnimalInput;
import simulation.Pair;

public class Detritivores extends Animal {
    private static final double POSSIBILITY_ATACK = 1.0;

    Detritivores(final AnimalInput animal, final Pair position) {
        super(animal, position);
    }

    @Override
    public final String getType() {
        return "Detritivores";
    }

    @Override
    public final double posibilityToAtack() {
        return normalizeValue(POSSIBILITY_ATACK);
    }
}
