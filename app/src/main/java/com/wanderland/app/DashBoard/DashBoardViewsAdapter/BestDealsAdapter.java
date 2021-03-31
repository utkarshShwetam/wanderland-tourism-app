package com.wanderland.app.DashBoard.DashBoardViewsAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wanderland.app.DashBoard.DataModals.BestDealsModal;
import com.wanderland.app.R;

import java.util.ArrayList;

public class BestDealsAdapter extends RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder> {
    ArrayList<BestDealsModal> featuredPlaces;
    Context context;
    OnBestDealsListener mBestDealsListener;


    public BestDealsAdapter(ArrayList<BestDealsModal> featuredPlaces, Context context, OnBestDealsListener onBestDealsListener) {
        this.featuredPlaces = featuredPlaces;
        this.context=context;
        this.mBestDealsListener=onBestDealsListener;
    }

    @NonNull
    @Override
    public BestDealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.best_offer_card_layout,parent,false);
        return new BestDealsViewHolder(view,mBestDealsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BestDealsViewHolder holder, int position) {
        BestDealsModal bestDealsModal = featuredPlaces.get(position);
        Glide.with(context).load(bestDealsModal.getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        //holder.imageView.setImageResource(bestDealsAdapter.getImage());
        holder.place_country_textView.setText(bestDealsModal.getPlace_country());
        holder.place_name_textView.setText(bestDealsModal.getPlace_name());
        holder.place_price_textView.setText(bestDealsModal.getPlace_price());
        holder.place_discountPrice_textView.setText(bestDealsModal.getPlace_discountPrice());
    }

    @Override
    public int getItemCount() {
        return featuredPlaces.size();
    }

    public static class BestDealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView place_country_textView,place_name_textView,place_price_textView,place_discountPrice_textView;
        OnBestDealsListener onBestDealsListener;
        public BestDealsViewHolder(@NonNull View itemView,OnBestDealsListener onBestDealsListener) {
            super(itemView);

            //Getting ids
            imageView=itemView.findViewById(R.id.placeToVisit_imageView);
            place_country_textView=itemView.findViewById(R.id.place_country_TextView);
            place_name_textView=itemView.findViewById(R.id.place_name_TextView);
            place_price_textView=itemView.findViewById(R.id.place_price_textView);
            place_discountPrice_textView=itemView.findViewById(R.id.place_discountPrice_textView);
            this.onBestDealsListener=onBestDealsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBestDealsListener.OnBestDealsClick(getAdapterPosition());
        }
    }
    public interface OnBestDealsListener{
        void OnBestDealsClick(int position);
    }
}
