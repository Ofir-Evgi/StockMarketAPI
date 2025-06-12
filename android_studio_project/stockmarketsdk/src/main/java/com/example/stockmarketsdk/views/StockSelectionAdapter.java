package com.example.stockmarketsdk.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockSelectionAdapter extends RecyclerView.Adapter<StockSelectionAdapter.StockViewHolder> {

    public interface OnStockClickListener {
        void onStockClick(Stock stock);
    }

    private List<Stock> stocks = new ArrayList<>();
    private final OnStockClickListener listener;

    public StockSelectionAdapter(OnStockClickListener listener) {
        this.listener = listener;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock stock = stocks.get(position);
        holder.name.setText(stock.getCompany_name());
        holder.symbol.setText(stock.getSymbol());

        if (stock.getLogoImageBase64() != null && !stock.getLogoImageBase64().isEmpty()) {
            Bitmap logoBitmap = decodeBase64(stock.getLogoImageBase64());
            if (logoBitmap != null) {
                holder.logo.setImageBitmap(logoBitmap);
            } else {
                holder.logo.setImageResource(R.drawable.stock_placeholder_logo);
            }
        } else {
            holder.logo.setImageResource(R.drawable.stock_placeholder_logo);
        }

        holder.itemView.setOnClickListener(v -> listener.onStockClick(stock));
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView name, symbol;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.stockLogo);
            name = itemView.findViewById(R.id.stockName);
            symbol = itemView.findViewById(R.id.stockSymbol);
        }
    }

    private Bitmap decodeBase64(String base64) {
        try {
            byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } catch (Exception e) {
            return null;
        }
    }
}
