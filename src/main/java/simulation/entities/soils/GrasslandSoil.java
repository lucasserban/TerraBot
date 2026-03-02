package simulation.entities.soils;

import fileio.SoilInput;
import simulation.Pair;

public class GrasslandSoil extends Soil {
    private static final double ROUND_VALUE = 100.0;
    private static final double NITROGEN_MULT = 1.3;
    private static final double ORGANICMATTER_MULT = 1.5;
    private static final double ROOTDENSITY_MULT = 0.8;
    private static final double WATERRETENTION_MULT = 0.5;
    private static final double POSIBILITY_VALUE1 = 50;
    private static final double POSIBILITY_VALUE2 = 75;
    private double rootDensity;
    private double soilQuality;

    GrasslandSoil(final SoilInput soil, final Pair position) {
        super(soil, position);
        this.rootDensity = soil.getRootDensity();
        calculateSoilQuality();
    }

    @Override
    public final String getType() {
        return "GrasslandSoil";
    }

    @Override
    public final double getCustomField() {
        return this.rootDensity;
    }

    @Override
    public final String getCustomFieldName() {
        return "rootDensity";
    }

    @Override
    public final double getSoilQuality() {
        return this.soilQuality;
    }

    @Override
    public final void calculateSoilQuality() {
        soilQuality = (nitrogen * NITROGEN_MULT)
                + (organicMatter * ORGANICMATTER_MULT)
                + (rootDensity * ROOTDENSITY_MULT);
    }

    @Override
    public final double posibilityToGetStuck() {
        double result = ((POSIBILITY_VALUE1 - rootDensity) + waterRetention * WATERRETENTION_MULT)
                / POSIBILITY_VALUE2 * ROUND_VALUE;
        return normalizeValue(result);
    }
}
