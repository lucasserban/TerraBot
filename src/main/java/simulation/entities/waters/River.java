package simulation.entities.waters;

import fileio.WaterInput;
import simulation.Pair;

public class River extends Water {

    public River(final WaterInput water, final Pair position) {
        super(water, position);
    }

    @Override
    public final String getType() {
        return "river";
    }
}
