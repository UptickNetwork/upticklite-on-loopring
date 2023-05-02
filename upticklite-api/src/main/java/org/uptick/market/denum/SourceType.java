package org.uptick.market.denum;


public enum SourceType {

    FIRST_PUBLISH(1, "FIRST_PUBLISH") //
    , DETAIL_LINK(2, "DETAIL_LINK") //
    , ON_SALE(3, "ON_SALE") //
    , OFF_SALE(4, "OFF_SALE") //
    , OWNER(5, "OWNER_IS_ME") //
    ;

    private final int value;

    SourceType(int type, String title) {
        this.value = type;
    }

    public static SourceType parseValue(Integer value) {
        for (SourceType saleStatus : SourceType.values()) {
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

