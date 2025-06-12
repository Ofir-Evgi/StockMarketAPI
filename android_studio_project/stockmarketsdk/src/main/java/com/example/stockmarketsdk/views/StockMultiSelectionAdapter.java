package com.example.stockmarketsdk.views;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockmarketsdk.R;
import com.example.stockmarketsdk.models.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockMultiSelectionAdapter extends RecyclerView.Adapter<StockMultiSelectionAdapter.StockViewHolder> {

    public interface OnSelectionChangedListener {
        void onSelectionChanged(List<Stock> selectedStocks);
    }

    private final List<Stock> allStocks = new ArrayList<>();
    private final List<Stock> selectedStocks = new ArrayList<>();
    private final OnSelectionChangedListener listener;

    public StockMultiSelectionAdapter(OnSelectionChangedListener listener) {
        this.listener = listener;
    }

    public void setStocks(List<Stock> stocks) {
        allStocks.clear();
        allStocks.addAll(stocks);
        selectedStocks.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_checkbox, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock stock = allStocks.get(position);
        holder.name.setText(stock.getCompany_name());
        holder.symbol.setText(stock.getSymbol());
        holder.checkBox.setChecked(selectedStocks.contains(stock));

        if (stock.getLogoImageBase64() != null && !stock.getLogoImageBase64().isEmpty()) {
            try {
                byte[] decoded = Base64.decode(stock.getLogoImageBase64(), Base64.DEFAULT);
                holder.logo.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.length));
            } catch (Exception ignored) {}
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectedStocks.contains(stock)) {
                selectedStocks.remove(stock);
            } else {
                selectedStocks.add(stock);
            }
            notifyItemChanged(position);
            listener.onSelectionChanged(new ArrayList<>(selectedStocks));
        });
    }

    @Override
    public int getItemCount() {
        return allStocks.size();
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView name, symbol;
        ImageView logo;
        CheckBox checkBox;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.stockName);
            symbol = itemView.findViewById(R.id.stockSymbol);
            logo = itemView.findViewById(R.id.stockLogo);
            checkBox = itemView.findViewById(R.id.stockCheckBox);
        }
    }
}
