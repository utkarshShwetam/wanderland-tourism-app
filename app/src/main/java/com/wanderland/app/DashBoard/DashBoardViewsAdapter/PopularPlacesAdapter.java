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
import com.wanderland.app.DashBoard.DataModals.PopularPlacesModal;
import com.wanderland.app.R;

import java.util.ArrayList;

public class PopularPlacesAdapter extends RecyclerView.Adapter<PopularPlacesAdapter.PopularPlacesViewHolder> {
    ArrayList<PopularPlacesModal> featuredPlaces;
    Context context;
    OnPopularPlacesListener mPopularPlacesListener;


    public PopularPlacesAdapter(ArrayList<PopularPlacesModal> featuredPlaces, Context context, OnPopularPlacesListener onPopularPlacesListener) {
        this.featuredPlaces = featuredPlaces;
        this.context=context;
        this.mPopularPlacesListener=onPopularPlacesListener;
    }

    @NonNull
    @Override
    public PopularPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_places_card_layout,parent,false);
        return new PopularPlacesViewHolder(view,mPopularPlacesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularPlacesViewHolder holder, int position) {
        PopularPlacesModal popularPlacesModal = featuredPlaces.get(position);
        Glide.with(context).load(popularPlacesModal.getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        //holder.imageView.setImageResource(popularPlacesAdapter.getImage());
        holder.popularPlace_country_textView.setText(popularPlacesModal.getPlace_country());
        holder.popularPlace_name_textView.setText(popularPlacesModal.getPlace_name());
        holder.popularPlace_price_textView.setText(popularPlacesModal.getPlace_price());
        holder.popularPlace_discountPrice_textView.setText(popularPlacesModal.getPlace_discountPrice());
    }

    @Override
    public int getItemCount() {
        return featuredPlaces.size();
    }

    public static class PopularPlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //ProgressBar progressBar;
        ImageView imageView;
        TextView popularPlace_country_textView, popularPlace_name_textView, popularPlace_price_textView, popularPlace_discountPrice_textView;
        OnPopularPlacesListener onPopularPlacesListener;
        public PopularPlacesViewHolder(@NonNull View itemView,OnPopularPlacesListener onPopularPlacesListener) {
            super(itemView);

            //Getting ids
            //progressBar=itemView.findViewById(R.id.loadingPanel);
            imageView = itemView.findViewById(R.id.placeToVisit_imageView);
            popularPlace_country_textView = itemView.findViewById(R.id.popular_places_country_TextView);
            popularPlace_name_textView = itemView.findViewById(R.id.popular_places_name_TextView);
            popularPlace_price_textView = itemView.findViewById(R.id.popular_places_price_textView);
            popularPlace_discountPrice_textView = itemView.findViewById(R.id.popular_places_discountPrice_textView);
            this.onPopularPlacesListener=onPopularPlacesListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        onPopularPlacesListener.OnPopularPlacesClick(getAdapterPosition());
        }
    }
    public interface OnPopularPlacesListener{
        void OnPopularPlacesClick(int position);
    }
}
