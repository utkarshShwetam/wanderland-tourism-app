package com.wanderland.app.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.wanderland.app.DashBoard.DataModals.GetPackagesModal;
import com.wanderland.app.DashBoard.DataModals.ViewAllPlacesModal;
import com.wanderland.app.DashBoard.DashBoardViewsAdapter.ViewAllPlacesAdapter;
import com.wanderland.app.Details.PackageDetailsActivity;
import com.wanderland.app.R;

import java.util.ArrayList;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;

public class ViewAllPlaces extends AppCompatActivity implements ViewAllPlacesAdapter.OnViewAllPlacesListener {
    RecyclerView viewAllPlacesRecycler;
    RecyclerView.Adapter viewAllPlacesAdapter;
    ImageView BackImageView;
    List<GetPackagesModal> allPackage=MainDashboard.getPackagesModalList;
    ShimmerFrameLayout shimmerFrameLayoutViewAllPlaces;
    ArrayList<ViewAllPlacesModal> viewAllPlacesModals = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_places);
        customType(ViewAllPlaces.this, "bottom-to-up");
        viewAllPlacesRecycler = findViewById(R.id.view_all_places_recycler);
        viewAllPlacesRecycler.setNestedScrollingEnabled(false);
        BackImageView = findViewById(R.id.view_all_places_back_image);
        shimmerFrameLayoutViewAllPlaces=findViewById(R.id.shimmer_view_container_all_places);
        viewAllPlacesRecycler.setVisibility(View.GONE);
        shimmerFrameLayoutViewAllPlaces.setVisibility(View.VISIBLE);
        shimmerFrameLayoutViewAllPlaces.startShimmer();
        viewAllPlacesRecycler();
        //To move back to dashboard
        BackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.push_left_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.finish();
        overridePendingTransition(0,R.anim.push_left_out);
        super.onBackPressed();
    }

    //************************************View All Places Recycler*******************************************
    private void viewAllPlacesRecycler() {
        viewAllPlacesRecycler.setHasFixedSize(true);
        viewAllPlacesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //Log.e("Size", String.valueOf(allPackage.size()));
        for (int i=0;i<allPackage.size();i++) {
            viewAllPlacesModals.add(new ViewAllPlacesModal(allPackage.get(i).getImages(), allPackage.get(i).getTitle(), allPackage.get(i).getLocations(), "₹"+allPackage.get(i).getPrice()+"/person", ""));
        }
        viewAllPlacesRecycler.setVisibility(View.VISIBLE);
        shimmerFrameLayoutViewAllPlaces.stopShimmer();
        shimmerFrameLayoutViewAllPlaces.setVisibility(View.GONE);
        /*viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "Spain", "Barcelona", "€55.00", ""));
        viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "France", "Paris", "€62.00", ""));
        viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "Australia", "Sydney", "€62.00", ""));
        viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "USA", "Los Angeles", "€42.00", ""));
        viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "Spain", "Barcelona", "€55.00", ""));
        viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "France", "Paris", "€62.00", ""));
        viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "Australia", "Sydney", "€62.00", ""));
        viewAllPlacesAdapters.add(new ViewAllPlacesAdapter("", "USA", "Los Angeles", "€42.00", ""));*/

        viewAllPlacesAdapter = new ViewAllPlacesAdapter(viewAllPlacesModals,getApplicationContext(),this);
        viewAllPlacesRecycler.setAdapter(viewAllPlacesAdapter);
    }

    @Override
    public void OnViewAllPlacesClick(int position) {
        startActivity(new Intent(getApplicationContext(), PackageDetailsActivity.class).putExtra("ViewAllDeals",allPackage.get(position)));
    }


}