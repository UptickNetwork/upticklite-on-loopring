package org.uptick.market.denum;

public enum ContractAndNftStatus {
    normal(0),
    white(1),
    black(2),
    link(3),
    haveApplied(4);
    private Integer status;

    ContractAndNftStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}