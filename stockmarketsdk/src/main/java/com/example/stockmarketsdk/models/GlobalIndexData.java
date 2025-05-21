package com.example.stockmarketsdk.models;

import java.util.List;

public class GlobalIndexData {
    private String symbol;
    private List<GlobalIndexPrice> prices;

    public String getSymbol() { return symbol; }
    public List<GlobalIndexPrice> getPrices() { return prices; }
}
