package com.example.stockmarketsdk.models;

import java.util.List;

public class StockResponse {
    private List<Stock> stocks;

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
