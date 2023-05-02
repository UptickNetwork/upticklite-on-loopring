package org.uptick.market.denum;

/**
 * @author hb.nie
 */
public enum FilterStatus {
    CREAT(1, "CREAT") //
    , UPTICK(2, "UPTICK") //
    , LOOPRING(3, "LOOPRING") //
    ;//

    private final int value;

    FilterStatus(int status, String title) {
        this.value = status;
    }

    public static FilterStatus parseValue(Integer value) {
        for (FilterStatus saleStatus : FilterStatus.values()) {
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

