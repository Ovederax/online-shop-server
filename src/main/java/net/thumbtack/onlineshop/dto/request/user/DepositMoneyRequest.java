package net.thumbtack.onlineshop.dto.request.user;

public class DepositMoneyRequest {
    private String deposit;

    public DepositMoneyRequest(String deposit) {
        this.deposit = deposit;
    }

    public DepositMoneyRequest() {
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
}
