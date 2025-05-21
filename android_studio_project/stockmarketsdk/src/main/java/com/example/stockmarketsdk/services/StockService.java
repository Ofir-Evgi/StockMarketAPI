package com.example.stockmarketsdk.services;

import com.example.stockmarketsdk.ApiClient;
import com.example.stockmarketsdk.StockApiService;
import com.example.stockmarketsdk.models.Stock;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockService {

    private static final StockApiService apiService =
            ApiClient.getClient().create(StockApiService.class);

    public static void getStocks(OnStocksLoadedListener listener) {
        apiService.getAllStocks().enqueue(new Callback<List<Stock>>() {
            @Override
            public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Server error: Empty or invalid response");
                }
            }

            @Override
            public void onFailure(Call<List<Stock>> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }
}
