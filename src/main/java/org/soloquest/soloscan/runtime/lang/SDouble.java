package org.soloquest.soloscan.runtime.lang;

public class SDouble extends SNumber {


    public SDouble(double d) {
        super(d);
    }

    public static SDouble valueOf(double value) {
        return new SDouble(value);
    }

    public static SDouble valueOf(Double value) {
        return valueOf(value.doubleValue());
    }


    @Override
    public SObjectType getSObjectType() {
        return SObjectType.Double;
    }

}
