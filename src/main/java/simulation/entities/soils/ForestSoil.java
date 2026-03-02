package simulation.entities.soils;

import fileio.SoilInput;
import simulation.Pair;

public class ForestSoil extends Soil {
    private static final double NITROGEN_MULT = 1.2;
    private static final double ORGANICMATTER_MULT = 2;
    private static final double WATER_RETENTION_MULT = 1.5;
    private static final double LEAFLITTER_MULT = 0.3;
    private static final double ROUND_VALUE = 100.0;
    private static final double WATERRETENTION_MULT2 = 0.6;
    private static final double LEAFLITTER_MULT2 = 0.4;
    private static final double POSIBILITY_VALUE = 80.0;
    private double leafLitter;
    private double soilQuality;

    ForestSoil(final SoilInput soil, final Pair position) {
        super(soil, position);
        this.leafLitter = soil.getLeafLitter();
        calculateSoilQuality();
    }

    @Override
    public final String getType() {
        return "ForestSoil";
    }

    @Override
    public final double getCustomField() {
        return this.leafLitter;
    }

    @Override
    public final String getCustomFieldName() {
        return "leafLitter";
    }

    @Override
    public final double getSoilQuality() {
        return this.soilQuality;
    }

    @Override
    public final void calculateSoilQuality() {
        soilQuality = (nitrogen * NITROGEN_MULT)
                + (organicMatter * ORGANICMATTER_MULT)
                + (waterRetention * WATER_RETENTION_MULT)
                + (leafLitter * LEAFLITTER_MULT);
    }

    @Override
    public final double posibilityToGetStuck() {
        double result = (waterRetention * WATERRETENTION_MULT2 + leafLitter * LEAFLITTER_MULT2)
                / POSIBILITY_VALUE * ROUND_VALUE;
        return normalizeValue(result);
    }
}
