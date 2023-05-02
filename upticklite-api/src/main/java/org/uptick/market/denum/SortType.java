package org.uptick.market.denum;


public enum SortType {

    Created(1),
    Listed(2),
    Sold(3),
    EndingSoon(4),
    PriceLowToHigh(5),
    PriceHighToLow(6),
    MostViewed(7),
    MostLikes(8),
    Random(9);


    private final Integer value;

    SortType(Integer type) {
        this.value = type;
    }

    public static SortType parseValue(Integer value) {
        for (SortType saleStatus : SortType.values()) {
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

