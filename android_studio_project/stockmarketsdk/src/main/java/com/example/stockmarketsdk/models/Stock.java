package com.example.stockmarketsdk.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Stock implements Serializable {

    private String symbol;

    @SerializedName("company_name")
    private String company_name;

    private List<StockPrice> prices;

    @SerializedName("logo_image_base64")
    private String logoImageBase64;

    @SerializedName("bg_image_base64")
    private String bgImageBase64;

    @SerializedName("description")
    private String description;

    @SerializedName("news")
    private List<String> news;

    // --- Getters & Setters ---

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

    public String getLogoImageBase64() {
        return logoImageBase64;
    }

    public void setLogoImageBase64(String logoImageBase64) {
        this.logoImageBase64 = logoImageBase64;
    }

    public String getBgImageBase64() {
        return bgImageBase64;
    }

    public void setBgImageBase64(String bgImageBase64) {
        this.bgImageBase64 = bgImageBase64;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getNews() {
        return news;
    }

    public void setNews(List<String> news) {
        this.news = news;
    }
}
