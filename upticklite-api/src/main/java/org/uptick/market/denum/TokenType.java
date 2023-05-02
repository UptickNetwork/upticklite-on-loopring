package org.uptick.market.denum;

public enum TokenType {

    USDT, USDC, ETH, LRC, DAI;


    public static TokenType parseValue(String type) {
        TokenType[] values = TokenType.values();
        for (TokenType t : values) {
            if (t.name().equals(type)) {
                return t;
            }
        }
        return null;
    }
}
