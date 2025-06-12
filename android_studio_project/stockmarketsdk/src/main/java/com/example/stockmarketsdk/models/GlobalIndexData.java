package com.example.stockmarketsdk.models;

import java.util.List;

public class GlobalIndexData {
    private String symbol;
    private String company_name;
    private List<GlobalIndexPrice> prices;

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return company_name;
    }

    public List<GlobalIndexPrice> getPrices() {
        return prices;
    }
}
