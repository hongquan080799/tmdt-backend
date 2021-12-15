package com.abc.response;

import java.util.List;

public class PaypalRefund {
    List<Transactions> transactions;

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }
}

