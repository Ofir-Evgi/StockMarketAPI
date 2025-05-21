package com.example.stockmarketproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.models.Stock;
import com.example.stockmarketsdk.models.StockPrice;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<Stock> stockList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Stock stock);
    }

    public StockAdapter(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setStocks(List<Stock> stocks) {
        this.stockList = stocks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock stock = stockList.get(position);
        holder.title.setText(stock.getCompany_name());
        holder.subtitle.setText(stock.getSymbol());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(stock);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockList != null ? stockList.size() : 0;
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            subtitle = itemView.findViewById(android.R.id.text2);
        }
    }
}
