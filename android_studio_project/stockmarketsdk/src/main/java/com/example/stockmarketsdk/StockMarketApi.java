package com.example.stockmarketsdk;

import com.example.stockmarketsdk.models.BaseResponse;
import com.example.stockmarketsdk.models.FullMarketResponse;
import com.example.stockmarketsdk.models.GlobalIndexData;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockHistory;

import java.util.List;

import retrofit2.Call;

public class StockMarketApi {

    private static final StockApiService apiService = ApiClient.getClient().create(StockApiService.class);

    public static Call<BaseResponse<List<Stock>>> searchStocks(String query) {
        return apiService.searchStocks(query);
    }

    public static Call<BaseResponse<Stock>> getStock(String symbol) {
        return apiService.getStock(symbol);
    }

    public static Call<BaseResponse<List<StockHistory>>> getStockHistory(String symbol, String range) {
        return apiService.getStockHistory(symbol, range);
    }

    public static Call<BaseResponse<List<StockHistory>>> getStockHistory(String symbol) {
        return getStockHistory(symbol, "7d");
    }

    public static Call<List<Stock>> getAllStocks() {
        return apiService.getAllStocks();
    }

    public static Call<Stock> getStockBySymbol(String symbol) {
        return apiService.getStockBySymbol(symbol);
    }

    public static Call<List<GlobalIndexData>> getGlobalIndices() {
        return apiService.getGlobalIndices();
    }

    public static Call<FullMarketResponse> getFullMarketData() {
        return apiService.getFullMarketData();
    }
}
