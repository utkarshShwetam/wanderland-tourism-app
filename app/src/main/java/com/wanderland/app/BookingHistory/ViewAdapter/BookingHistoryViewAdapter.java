package com.wanderland.app.BookingHistory.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wanderland.app.BookingHistory.Modal.BookingHistoryModal;
import com.wanderland.app.R;

import java.util.ArrayList;

public class BookingHistoryViewAdapter extends RecyclerView.Adapter<BookingHistoryViewAdapter.BookingHistoryViewHolder> {
    ArrayList<BookingHistoryModal> bookingHistoryModals;
    Context context;

    public BookingHistoryViewAdapter(ArrayList<BookingHistoryModal> bookingHistoryModals, Context context) {
        this.bookingHistoryModals = bookingHistoryModals;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_card, parent, false);
        return new BookingHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryViewHolder holder, int position) {
        BookingHistoryModal getBookingHistory= bookingHistoryModals.get(position);
        holder.transaction_amount.setText(getBookingHistory.getAmount());
        holder.locations.setText(getBookingHistory.getLocations());
        holder.data.setText(getBookingHistory.getCreate_time());
        holder.booked_days.setText(getBookingHistory.getDays());
        holder.booked_nights.setText(getBookingHistory.getNights());
        holder.booked_people.setText(getBookingHistory.getPeople());

    }

    @Override
    public int getItemCount() {
        return bookingHistoryModals.size();
    }

    public static class BookingHistoryViewHolder extends RecyclerView.ViewHolder{
        TextView transaction_amount, data, locations,booked_days,booked_nights,booked_people;
        public BookingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            //Getting ids
            transaction_amount=itemView.findViewById(R.id.transaction_amount_booking_history_TextView);
            data=itemView.findViewById(R.id.date_booked_TextView);
            locations=itemView.findViewById(R.id.locations_booking_history_textView);
            booked_days=itemView.findViewById(R.id.booked_days_textView);
            booked_nights=itemView.findViewById(R.id.booked_nights_textView);
            booked_people=itemView.findViewById(R.id.booked_noOfPeople_textView);

        }
    }
}
