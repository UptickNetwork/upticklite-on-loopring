package org.uptick.market.denum;


public enum ChannelType {

    UPTICK_LOOPRING("Uptick-Loopring", "General goods") //
    , LOOPRING("Loopring", "Auction goods") //
    ;


    private final String value;

    ChannelType(String type, String title) {
        this.value = type;
    }

    public static ChannelType parseValue(String value) {
        for (ChannelType saleStatus : ChannelType.values()) {
            if (saleStatus.intValue() == value) {
                return saleStatus;
            }
        }
        return null;
    }

    public String intValue() {
        return value;
    }
}

