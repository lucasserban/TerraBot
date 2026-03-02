package simulation.entities.animals;

import fileio.AnimalInput;
import simulation.Pair;

public class Omnivores extends Animal {
    private static final double POSSIBILITY_ATACK = 4.0;

    Omnivores(final AnimalInput animal, final Pair position) {
        super(animal, position);
    }

    @Override
    public final String getType() {
        return "Omnivores";
    }

    @Override
    public final double posibilityToAtack() {
        return normalizeValue(POSSIBILITY_ATACK);
    }
}
