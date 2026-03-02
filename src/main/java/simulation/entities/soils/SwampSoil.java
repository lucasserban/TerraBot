package simulation.entities.soils;

import fileio.SoilInput;
import simulation.Pair;

public class SwampSoil extends Soil {
    private static final double NITROGEN_MULT = 1.1;
    private static final double ORGANICMATTER_MULT = 2.2;
    private static final double WATERLOGGING_MULT = 5;
    private static final double POSIBILITY_MULT = 10;
    private double waterLogging;
    private double soilQuality;

    SwampSoil(final SoilInput soil, final Pair position) {
        super(soil, position);
        waterLogging = soil.getWaterLogging();
        calculateSoilQuality();
    }

    @Override
    public final String getType() {
        return "SwampSoil";
    }

    @Override
    public final double getCustomField() {
        return this.waterLogging;
    }

    @Override
    public final String getCustomFieldName() {
        return "waterLogging";
    }

    @Override
    public final double getSoilQuality() {
        return this.soilQuality;
    }


    @Override
    public final void calculateSoilQuality() {
        soilQuality = (nitrogen * NITROGEN_MULT)
                + (organicMatter * ORGANICMATTER_MULT)
                - (waterLogging * WATERLOGGING_MULT);
    }

    @Override
    public final double posibilityToGetStuck() {
        double result = waterLogging * POSIBILITY_MULT;
        return normalizeValue(result);
    }
}
