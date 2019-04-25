package net.thumbtack.onlineshop.dto.request.user;

import net.thumbtack.onlineshop.model.exeptions.enums.ValidationError;
import net.thumbtack.onlineshop.validation.DepositFormat;

import javax.validation.constraints.NotNull;

public class DepositMoneyRequest {
    @NotNull(message = ValidationError.DEPOSIT_CANNOT_BE_NULL)
    @DepositFormat
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
