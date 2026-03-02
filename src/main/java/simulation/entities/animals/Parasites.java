package simulation.entities.animals;

import fileio.AnimalInput;
import simulation.Pair;

public class Parasites extends Animal {
    private static final double POSSIBILITY_ATACK = 9.0;

    Parasites(final AnimalInput animal, final Pair position) {
        super(animal, position);
    }

    @Override
    public final String getType() {
        return "Parasites";
    }

    @Override
    public final double posibilityToAtack() {
        return normalizeValue(POSSIBILITY_ATACK);
    }
}
