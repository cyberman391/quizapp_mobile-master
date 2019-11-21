package com.quizest.quizestapp.ActivityPackage.PurchaseHistory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizest.quizestapp.ModelPackage.Coin;
import com.quizest.quizestapp.R;

import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Coin> coinList;
    private int HEADER = 1;
    private int REGULAR = 2;
    Activity activity;

    public PurchaseHistoryAdapter(List<Coin> coinList, Activity activity) {
        this.coinList = coinList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new HeaderHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_purchase_history_header, parent, false)
            );
        } else if (viewType == REGULAR) {
            return new PurchaseViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_purchase_history, parent, false)
            );
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PurchaseViewHolder) {
            PurchaseViewHolder purchaseViewHolder = (PurchaseViewHolder) holder;
            purchaseViewHolder.coinName.setText(coinList.get(position)
                    .getCoinName());
            purchaseViewHolder.purchasedDate.setText(coinList.get(position)
                    .getDate());
            purchaseViewHolder.amount.setText(coinList.get(position)
                    .getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return REGULAR;
        }
    }


    class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView coinName, amount, purchasedDate;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            coinName = itemView.findViewById(R.id.tv_coin_name);
            amount = itemView.findViewById(R.id.tv_coin_amount);
            purchasedDate = itemView.findViewById(R.id.tv_purchased_date);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
