package com.wanderland.app.Details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wanderland.app.Booking.BookingActivity;
import com.wanderland.app.DashBoard.DataModals.GetPackagesModal;
import com.wanderland.app.Details.Modal.PackageDetailsModal;
import com.wanderland.app.Details.PackageDetailsAdapter.PackageDetailsAdapter;
import com.wanderland.app.R;

import java.util.ArrayList;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;
import static maes.tech.intentanim.CustomIntent.customType;

public class PackageDetailsActivity extends AppCompatActivity {
    SliderView sliderView;
    public static ArrayList<PackageDetailsModal> packageDetailsModals;
    GetPackagesModal getPackagesModal;
    MaterialButton BookMaterialButton;
    TextView description, noOfDays, noOfNights, price, locations;


    String[] imgURL;
    String[] getLocations;
    StringBuilder loc = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        customType(PackageDetailsActivity.this, "left-to-right");
        Log.e("Profile", "onCreate:Opened ");

        packageDetailsModals = new ArrayList<>();
        //Setting view
        sliderView = findViewById(R.id.imageSliderView);
        description = findViewById(R.id.package_description_textView);
        noOfDays = findViewById(R.id.noOfDays_textView);
        noOfNights = findViewById(R.id.noOfNights_textView);
        price = findViewById(R.id.price_textView);
        locations = findViewById(R.id.locations_textView);
        BookMaterialButton = findViewById(R.id.book_button);
        PushDownAnim.setPushDownAnimTo(BookMaterialButton)
                .setScale(MODE_SCALE, 0.89f);

        //***********************Custom object getting*************************************
        if (getIntent().hasExtra("BestOfferDeals")) {
            getPackagesModal = getIntent().getParcelableExtra("BestOfferDeals");
            assert getPackagesModal != null;
            Log.e("BestOfferDeals", "onCreate:" + getPackagesModal.getTitle());
            init();
        }
        if (getIntent().hasExtra("PopularPlacesDeals")) {
            getPackagesModal = getIntent().getParcelableExtra("PopularPlacesDeals");
            assert getPackagesModal != null;
            Log.e("PopularPlacesDeals", "onCreate:" + getPackagesModal.getTitle());
            init();
        }
        if (getIntent().hasExtra("ViewAllDeals")) {
            getPackagesModal = getIntent().getParcelableExtra("ViewAllDeals");
            assert getPackagesModal != null;
            Log.e("ViewAllDeals", "onCreate:" + getPackagesModal.getTitle());
            init();
        }

        sliderView.setSliderAdapter(new PackageDetailsAdapter(this));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();

        BookMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookingActivity.class).putExtra("Checkout Package", getPackagesModal));
            }
        });
    }

    //*************on back press button**********************************
    @Override
    public void onBackPressed() {
        super.finish();
        overridePendingTransition(0, R.anim
                .right_to_left);
        super.onBackPressed();
    }

    //************Setting value to layout****************************************
    private void init() {
        imgURL = getPackagesModal.getImagesArray();
        String pricePer=getPackagesModal.getPrice()+"/person";
        getLocations = getPackagesModal.getLocationsArray();

        for (int i = 0; i < getPackagesModal.getImagesArray().length; i++) {
            PackageDetailsModal valueAdapter = new PackageDetailsModal();
            valueAdapter.setUrl(imgURL[i]);
            packageDetailsModals.add(valueAdapter);

        }
        for (int i = 0; i < getPackagesModal.getLocationsArray().length; i++)
            if (i == getPackagesModal.getLocationsArray().length - 1)
                loc.append(getLocations[i]);
            else
                loc.append(getLocations[i]).append(",");
        locations.setText(loc);
        description.setText(getPackagesModal.getDescription());
        price.setText(pricePer);
        noOfNights.setText(getPackagesModal.getNights());
        noOfDays.setText(getPackagesModal.getDays());
    }
}