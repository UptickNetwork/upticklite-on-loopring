package org.uptick.market.denum;


public enum MarkeType {

    SALE(1, "General goods") //
    , AUCTION(2, "Auction goods") //
    ;


    private final int value;

    MarkeType(int type, String title) {
        this.value = type;
    }

    public static MarkeType parseValue(Integer value) {
        for (MarkeType saleStatus : MarkeType.values()) {
            if (saleStatus.intValue() == value) {
                return saleStatus;
            }
        }
        return null;
    }

    public Integer intValue() {
        return value;
    }
}

