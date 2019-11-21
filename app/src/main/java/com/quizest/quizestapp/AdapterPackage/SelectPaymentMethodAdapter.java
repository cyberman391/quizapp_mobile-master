package com.quizest.quizestapp.AdapterPackage;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quizest.quizestapp.ActivityPackage.BuyCoin.BuyCoinActivity;
import com.quizest.quizestapp.ModelPackage.PaymentMethodOption;
import com.quizest.quizestapp.R;

import java.util.List;

public class SelectPaymentMethodAdapter extends RecyclerView.Adapter<SelectPaymentMethodAdapter.PaymentMethodHolder> {

    List<PaymentMethodOption> paymentMethodOptionList;
    Activity activity;
    int selectedPosition = 0;
    ConstraintLayout layoutCardInfo, layoutPaypalInfo;
    AppCompatButton btnBuyCoin;

    public SelectPaymentMethodAdapter(List<PaymentMethodOption> paymentMethodOptionList,
                                      Activity activity, ConstraintLayout layoutPaypalInfo, ConstraintLayout layoutCardInfo, AppCompatButton btnBuyCoin) {
        this.paymentMethodOptionList = paymentMethodOptionList;
        this.activity = activity;
        this.layoutPaypalInfo = layoutPaypalInfo;
        this.layoutCardInfo = layoutCardInfo;
        this.btnBuyCoin = btnBuyCoin;
    }

    @NonNull
    @Override
    public PaymentMethodHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_paymenth_method_row, viewGroup, false);
        return new PaymentMethodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodHolder viewHolder, int i) {
        if (selectedPosition == i) {
            viewHolder.imgOption.setImageResource(R.drawable.ic_option_selected);
        } else {
            viewHolder.imgOption.setImageResource(R.drawable.ic_option_unselected);
        }
        viewHolder.methodName.setText(paymentMethodOptionList.get(i)
                .getPaymentName()
        );
    }

    @Override
    public int getItemCount() {
        return paymentMethodOptionList.size();
    }

    class PaymentMethodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgOption;
        TextView methodName;

        public PaymentMethodHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgOption = itemView.findViewById(R.id.image_option);
            methodName = itemView.findViewById(R.id.tv_payment_method_name);
        }

        @Override
        public void onClick(View v) {
            if (paymentMethodOptionList.get(getAdapterPosition())
                    .getPaymentName().contains("Paypal")) {
                layoutCardInfo.setVisibility(View.GONE);
                layoutPaypalInfo.setVisibility(View.VISIBLE);
                btnBuyCoin.setVisibility(View.GONE);
            } else if (paymentMethodOptionList.get(getAdapterPosition())
                    .getPaymentName().contains("Card")) {
                layoutCardInfo.setVisibility(View.VISIBLE);
                layoutPaypalInfo.setVisibility(View.GONE);
                btnBuyCoin.setVisibility(View.VISIBLE);
            }
            BuyCoinActivity.paymentId = paymentMethodOptionList
                    .get(getAdapterPosition()).getId();
            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
        }
    }
}
