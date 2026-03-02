package simulation.entities.soils;

import fileio.SoilInput;
import simulation.Pair;

public class TundraSoil extends Soil {
    private static final double NITROGEN_MULT = 0.7;
    private static final double ORGANICMATTER_MULT = 0.5;
    private static final double PERMAFROST_MULT = 1.5;
    private static final double POSIBILITY_VALUE = 50;
    private static final double ROUND_VALUE = 100.0;
    private double permafrostDepth;
    private double soilQuality;

    TundraSoil(final SoilInput soil, final Pair position) {
        super(soil, position);
        this.permafrostDepth = soil.getPermafrostDepth();
        calculateSoilQuality();
    }

    @Override
    public final String getType() {
        return "TundraSoil";
    }

    @Override
    public final double getCustomField() {
        return this.permafrostDepth;
    }

    @Override
    public final String getCustomFieldName() {
        return "permafrostDepth";
    }

    @Override
    public final double getSoilQuality() {
        return this.soilQuality;
    }

    @Override
    public final void calculateSoilQuality() {
        soilQuality = (nitrogen * NITROGEN_MULT)
                + (organicMatter * ORGANICMATTER_MULT)
                - (permafrostDepth * PERMAFROST_MULT);
    }

    @Override
    public final double posibilityToGetStuck() {
        double result = (POSIBILITY_VALUE - permafrostDepth) / POSIBILITY_VALUE * ROUND_VALUE;
        return normalizeValue(result);
    }
}
