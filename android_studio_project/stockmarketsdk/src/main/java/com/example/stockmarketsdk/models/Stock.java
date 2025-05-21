package com.example.stockmarketsdk.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Stock implements Serializable {

    private String symbol;

    @SerializedName("company_name")
    private String company_name;

    private List<StockPrice> prices;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public List<StockPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<StockPrice> prices) {
        this.prices = prices;
    }
}
