package com.wanderland.app.Details.PackageDetailsAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.wanderland.app.Details.Modal.PackageDetailsModal;
import com.wanderland.app.Details.PackageDetailsActivity;
import com.wanderland.app.R;

import java.util.List;

public class PackageDetailsAdapter extends SliderViewAdapter<SliderViewHolder> {
    Context context;
    List<PackageDetailsModal> packageDetailsModalList= PackageDetailsActivity.packageDetailsModals;

    public PackageDetailsAdapter(Context context) {
        this.context = context;
    }
    public void renewItems(List<PackageDetailsModal> packageDetailsModalList) {
        this.packageDetailsModalList = packageDetailsModalList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.packageDetailsModalList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(PackageDetailsModal packageDetailsModalList) {
        this.packageDetailsModalList.add(packageDetailsModalList);
        notifyDataSetChanged();
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout,null);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        PackageDetailsModal packageDetailsModal=packageDetailsModalList.get(position);
        Glide.with(context).load(packageDetailsModal.getUrl())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.sliderImageView);

    }

    @Override
    public int getCount() {
        return packageDetailsModalList.size();
    }
}
class SliderViewHolder extends SliderViewAdapter.ViewHolder {
    ImageView sliderImageView;
    public SliderViewHolder(View itemView) {
        super(itemView);
        sliderImageView=itemView.findViewById(R.id.image_slider_imageView);

    }
}

