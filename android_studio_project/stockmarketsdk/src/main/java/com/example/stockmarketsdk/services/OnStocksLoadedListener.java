package com.example.stockmarketsdk.services;

import com.example.stockmarketsdk.models.Stock;

import java.util.List;

public interface OnStocksLoadedListener {
    void onSuccess(List<Stock> stocks);
    void onError(String errorMessage);
}
