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
import com.wanderland.app.DashBoard.DataModals.ViewAllPlacesModal;
import com.wanderland.app.R;

import java.util.ArrayList;

public class ViewAllPlacesAdapter extends RecyclerView.Adapter<ViewAllPlacesAdapter.ViewAllPlacesViewHolder> {
    ArrayList<ViewAllPlacesModal> featuredPlaces;
    Context context;
    OnViewAllPlacesListener mViewAllPlacesListener;

    public ViewAllPlacesAdapter(ArrayList<ViewAllPlacesModal> featuredPlaces, Context context, OnViewAllPlacesListener onViewAllPlacesListener) {
        this.featuredPlaces = featuredPlaces;
        this.context=context;
        this.mViewAllPlacesListener=onViewAllPlacesListener;
    }

    @NonNull
    @Override
    public ViewAllPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_places_card_layout, parent, false);
        ViewAllPlacesAdapter.ViewAllPlacesViewHolder viewAllPlacesViewHolder = new ViewAllPlacesViewHolder(view,mViewAllPlacesListener);
        return viewAllPlacesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllPlacesViewHolder holder, int position) {
        ViewAllPlacesModal viewAllPlacesModal = featuredPlaces.get(position);
        Glide.with(context).load(viewAllPlacesModal.getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.popularPlace_country_textView.setText(viewAllPlacesModal.getPlace_country());
        holder.popularPlace_name_textView.setText(viewAllPlacesModal.getPlace_name());
        holder.popularPlace_price_textView.setText(viewAllPlacesModal.getPlace_price());
        holder.popularPlace_discountPrice_textView.setText(viewAllPlacesModal.getPlace_discountPrice());
    }

    @Override
    public int getItemCount() {
        return featuredPlaces.size();
    }

    public static class ViewAllPlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView popularPlace_country_textView, popularPlace_name_textView, popularPlace_price_textView, popularPlace_discountPrice_textView;
        OnViewAllPlacesListener onViewAllPlacesListener;
        public ViewAllPlacesViewHolder(@NonNull View itemView,OnViewAllPlacesListener onViewAllPlacesListener) {
            super(itemView);

            //Getting ids
            imageView = itemView.findViewById(R.id.placeToVisit_imageView);
            popularPlace_country_textView = itemView.findViewById(R.id.popular_places_country_TextView);
            popularPlace_name_textView = itemView.findViewById(R.id.popular_places_name_TextView);
            popularPlace_price_textView = itemView.findViewById(R.id.popular_places_price_textView);
            popularPlace_discountPrice_textView = itemView.findViewById(R.id.popular_places_discountPrice_textView);
            this.onViewAllPlacesListener=onViewAllPlacesListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onViewAllPlacesListener.OnViewAllPlacesClick(getAdapterPosition());
        }
    }
    public interface OnViewAllPlacesListener{
        void OnViewAllPlacesClick(int position);
    }
}
