package com.example.stockmarketsdk;

import com.example.stockmarketsdk.models.BaseResponse;
import com.example.stockmarketsdk.models.FullMarketResponse;
import com.example.stockmarketsdk.models.GlobalIndexData;
import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockHistory;
import com.example.stockmarketsdk.models.StockResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StockApiService {

    @GET("stocks")
    Call<BaseResponse<List<Stock>>> searchStocks(@Query("query") String query);

    @GET("stock/{symbol}")
    Call<BaseResponse<Stock>> getStock(@Path("symbol") String symbol);

    @GET("stock/{symbol}/history")
    Call<BaseResponse<List<StockHistory>>> getStockHistory(@Path("symbol") String symbol, @Query("range") String range);

    @GET("stock_prices.json")
    Call<FullMarketResponse> getFullMarketData();

    @GET("stocks")
    Call<List<Stock>> getAllStocks();

    @GET("stocks/{symbol}")
    Call<Stock> getStockBySymbol(@Path("symbol") String symbol);

    @GET("analytics/stock-clicks")
    Call<List<Object>> getStockClicks();

    @GET("stocks")
    Call<List<Stock>> getStocks();

    @GET("global-indices")
    Call<List<GlobalIndexData>> getGlobalIndices();


}
