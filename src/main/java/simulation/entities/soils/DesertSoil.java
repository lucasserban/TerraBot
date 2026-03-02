package simulation.entities.soils;

import fileio.SoilInput;
import simulation.Pair;

public class DesertSoil extends Soil {
    private static final double ROUND_VALUE = 100.0;
    private static final double NITROGEN_MULT = 0.5;
    private static final double WATERRETENTION_MULT = 0.3;
    private static final double SALINITY_MULT = 2;
    private double salinity;
    private double soilQuality;

    DesertSoil(final SoilInput soil, final Pair position) {
        super(soil, position);
        this.salinity = soil.getSalinity();
        calculateSoilQuality();
    }

    @Override
    public final String getType() {
        return "DesertSoil";
    }

    @Override
    public final double getCustomField() {
        return this.salinity;
    }

    @Override
    public final String getCustomFieldName() {
        return "salinity";
    }

    @Override
    public final double getSoilQuality() {
        return this.soilQuality;
    }

    @Override
    public final void calculateSoilQuality() {
        soilQuality = (nitrogen * NITROGEN_MULT)
                + (waterRetention * WATERRETENTION_MULT)
                - (salinity * SALINITY_MULT);
    }

    @Override
    public final double posibilityToGetStuck() {
        double result = (ROUND_VALUE - waterRetention + salinity) / ROUND_VALUE * ROUND_VALUE;
        return normalizeValue(result);
    }
}
