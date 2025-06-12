package com.example.stockmarketsdk.views;

import android.view.ViewGroup;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stockmarketsdk.models.Stock;

import java.util.List;

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

        holder.stockCardView.setFullStockData(stock);

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

    static class StockViewHolder extends RecyclerView.ViewHolder {
        public final StockCardView stockCardView;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            this.stockCardView = (StockCardView) itemView;
        }
    }
}
