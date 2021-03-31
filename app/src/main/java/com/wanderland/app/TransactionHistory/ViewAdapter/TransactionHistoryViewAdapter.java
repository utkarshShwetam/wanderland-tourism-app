package com.wanderland.app.TransactionHistory.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wanderland.app.R;
import com.wanderland.app.TransactionHistory.DataModals.TransactionHistoryModal;

import java.util.ArrayList;

public class TransactionHistoryViewAdapter extends RecyclerView.Adapter<TransactionHistoryViewAdapter.TransactionHistoryViewHolder> {
    ArrayList<TransactionHistoryModal> getTransactionHistory;
    Context context;


    public TransactionHistoryViewAdapter(ArrayList<TransactionHistoryModal> getTransactionHistories, Context context) {
        this.getTransactionHistory = getTransactionHistories;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_history_card_layout, parent, false);
        return new TransactionHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryViewHolder holder, int position) {
        TransactionHistoryModal getTransaction = getTransactionHistory.get(position);
        holder.transaction_amount.setText(getTransaction.getAmount());
        holder.transaction_date.setText(getTransaction.getCreate_time());
        holder.transaction_id.setText(getTransaction.getTransaction_id());
        holder.transaction_status.setText(getTransaction.getStatus());
        holder.transaction_payment_mode.setText(getTransaction.getPayment_method());
    }

    @Override
    public int getItemCount() {
        return getTransactionHistory.size();
    }

    public static class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView transaction_amount, transaction_date, transaction_id, transaction_status, transaction_payment_mode;

        public TransactionHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            //Getting ids
            transaction_amount = itemView.findViewById(R.id.transaction_amount_TextView);
            transaction_date = itemView.findViewById(R.id.date_TextView);
            transaction_id = itemView.findViewById(R.id.transaction_id_textView);
            transaction_status = itemView.findViewById(R.id.status_textView);
            transaction_payment_mode = itemView.findViewById(R.id.payment_mode_textView);
        }
    }

}
