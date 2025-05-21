package com.example.stockmarketsdk.models;

import java.util.List;

public class StockHistoryResponse {
    private List<StockHistory> history;

    public List<StockHistory> getHistory() {
        return history;
    }

    public void setHistory(List<StockHistory> history) {
        this.history = history;
    }
}
