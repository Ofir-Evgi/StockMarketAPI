package com.example.stockmarketsdk.views;

import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockPrice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockViewHolder> {

    public interface OnStockClickListener {
        void onStockClick(Stock stock);
    }

    private final List<Stock> stockList;
    private final OnStockClickListener listener;

    public StockListAdapter(List<Stock> stockList, OnStockClickListener listener) {
        this.stockList = stockList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StockCardView stockCardView = new StockCardView(parent.getContext());
        stockCardView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new StockViewHolder(stockCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock stock = stockList.get(position);
        List<StockPrice> prices = stock.getPrices();

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        double priceToShow = findPriceForTime(prices, currentTime);

        holder.setStockData(stock.getCompany_name(), stock.getSymbol(), priceToShow);
        holder.setHistory(prices);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStockClick(stock);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    private double findPriceForTime(List<StockPrice> prices, String timeNow) {
        if (prices == null || prices.isEmpty()) return 0.0;

        for (StockPrice price : prices) {
            if (timeNow.equals(price.getTime())) {
                return price.getPrice();
            }
        }

        StockPrice fallback = null;
        for (StockPrice price : prices) {
            if (price.getTime().compareTo(timeNow) <= 0) {
                fallback = price;
            } else {
                break;
            }
        }

        return fallback != null ? fallback.getPrice() : prices.get(0).getPrice();
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {
        private final StockCardView stockCardView;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stockCardView = (StockCardView) itemView;
        }

        public void setStockData(String name, String symbol, Double price) {
            stockCardView.setStockData(name, symbol, price);
        }

        public void setHistory(List<StockPrice> history) {
            stockCardView.setHistory(history);
        }
    }
}
