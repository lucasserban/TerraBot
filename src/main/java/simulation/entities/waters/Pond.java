package simulation.entities.waters;

import fileio.WaterInput;
import simulation.Pair;

public class Pond extends Water {

    public Pond(final WaterInput water, final Pair position) {
        super(water, position);
    }

    @Override
    public final String getType() {
        return "pond";
    }
}
