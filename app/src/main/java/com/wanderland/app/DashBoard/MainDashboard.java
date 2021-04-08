package com.wanderland.app.DashBoard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.wanderland.app.Booking.BookingActivity;
import com.wanderland.app.BookingHistory.BookingHistoryActivity;
import com.wanderland.app.BookingHistory.Modal.BookingHistoryModal;
import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.DashBoard.DataModals.BestDealsModal;
import com.wanderland.app.DashBoard.DataModals.GetPackagesModal;
import com.wanderland.app.DashBoard.DataModals.PopularPlacesModal;
import com.wanderland.app.DashBoard.DashBoardViewsAdapter.BestDealsAdapter;
import com.wanderland.app.DashBoard.DashBoardViewsAdapter.PopularPlacesAdapter;
import com.wanderland.app.Details.PackageDetailsActivity;
import com.wanderland.app.LoginSignUp.LogOutActivity;
import com.wanderland.app.R;
import com.wanderland.app.SavedCards.SavedCard;
import com.wanderland.app.TransactionHistory.TransactionHistoryActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BestDealsAdapter.OnBestDealsListener, PopularPlacesAdapter.OnPopularPlacesListener {
    //*****************Variables*******************
    long SPLASH_TIMER = 220;
    static String restoredText;
    boolean singleBack = false;
    RecyclerView bestOfferRecycler, popularPlacesRecycler;
    RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder> bestOfferAdapter;
    RecyclerView.Adapter<PopularPlacesAdapter.PopularPlacesViewHolder> popularPlacesAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuOpenImageView;
    TextView ViewAllTextView, user_name, user_username;
    LinearLayout linearLayout;
    public static List<GetPackagesModal> getPackagesModalList;
    public static List<GetPackagesModal> getPackagesModalListBest;
    public static List<GetPackagesModal> getPackagesModalListPopular;
    ShimmerFrameLayout shimmerFrameLayoutBestOffer, shimmerFrameLayoutPopularPlaces;


    //Login parameters OkHttp
    OkHttpClient client;
    Response response;
    final String URLGetPackages = ConstantValues.API+"/user/get-packages";
    final String URLGETUser = ConstantValues.API+"/get-profile";

    //Adapters of recyclerview
    ArrayList<BestDealsModal> bestDealsModals = new ArrayList<>();
    ArrayList<PopularPlacesModal> popularPlacesModals = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        //textView
        ViewAllTextView = findViewById(R.id.view_all_dashboard);
        //Recycler setting values
        bestOfferRecycler = findViewById(R.id.best_offer_recycler);
        bestOfferRecycler.setNestedScrollingEnabled(false);
        popularPlacesRecycler = findViewById(R.id.popular_places_recycler);
        popularPlacesRecycler.setNestedScrollingEnabled(false);

        //Menu setting values
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        //Navigation Drawer
        menuOpenImageView = findViewById(R.id.nav_open_image);
        navigationView();

        linearLayout = findViewById(R.id.content);

        ViewAllTextView.setVisibility(View.GONE);

        //Shimmer layout
        shimmerFrameLayoutBestOffer = findViewById(R.id.shimmer_view_container_best_offers);
        shimmerFrameLayoutPopularPlaces = findViewById(R.id.shimmer_view_container_popular_places);

        //For best and popular card shimmer effect
        bestOfferRecycler.setVisibility(View.GONE);
        shimmerFrameLayoutBestOffer.setVisibility(View.VISIBLE);
        shimmerFrameLayoutBestOffer.startShimmer();

        popularPlacesRecycler.setVisibility(View.GONE);
        shimmerFrameLayoutPopularPlaces.setVisibility(View.VISIBLE);
        shimmerFrameLayoutPopularPlaces.startShimmer();
        //Shared Pref
        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        restoredText = prfs.getString("KEY", null);
        if (restoredText != null) {
            Log.e("KEY", "Key present");
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText = "";
        }
        //To get images from server
        getImage();


    }


    private void ViewAllPlacesCall() {
        //To view all places
        ViewAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewAllPlaces.class));
            }
        });
    }

    //**********************************Double back press closes application********************************************
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (singleBack) {
            super.onBackPressed();
            return;
        }

        this.singleBack = true;
        Toast.makeText(this, "Double Tap to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                singleBack = false;
            }
        }, 2000);

    }

    //*****************************On pause and resume********************************************************
    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayoutBestOffer.stopShimmer();
        shimmerFrameLayoutPopularPlaces.stopShimmer();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        shimmerFrameLayoutBestOffer.startShimmer();
        shimmerFrameLayoutPopularPlaces.startShimmer();
    }

    //*********************************Navigation between cases from drawer**************************************
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_menu_home:
                drawerLayout.closeDrawers();
                break;
            /*case R.id.nav_menu_search_bar:
                break;*/
            /*case R.id.nav_menu_profile_login:
                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intentLogin);
                    }
                }, SPLASH_TIMER);

                break;*/
            case R.id.nav_menu_profile_logout:
                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intentLogOut = new Intent(getApplicationContext(), LogOutActivity.class);
                        startActivity(intentLogOut);
                    }
                }, SPLASH_TIMER);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 230);
                break;
            /*case R.id.nav_menu_profile_profile:
                    break;*/
            case R.id.nav_menu_payments_savedCards:
                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), SavedCard.class);
                        startActivity(intent);
                    }
                }, SPLASH_TIMER);
                break;
          /*  case R.id.nav_menu_payments_addCard:
                    break;*/
            case R.id.nav_menu_booking_history:
                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), BookingHistoryActivity.class);
                        startActivity(intent);
                    }
                }, SPLASH_TIMER);
                break;
            case R.id.nav_menu_payments_transaction_history:
                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), TransactionHistoryActivity.class);
                        startActivity(intent);
                    }
                }, SPLASH_TIMER);
                break;
        }
        return true;
    }

    //******************************Best Deals Recycler to add into dashboard***********************************
    private void bestOfferRecycler() {
        bestOfferRecycler.setHasFixedSize(true);
        bestOfferRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getPackagesModalListBest = new ArrayList<>();
        for (int i = 0; i < getPackagesModalList.size(); i++) {
            if (getPackagesModalList.get(i).getTag().equals("BEST")) {
                getPackagesModalListBest.add(getPackagesModalList.get(i));
                bestDealsModals.add(new BestDealsModal(getPackagesModalList.get(i).getImages(), getPackagesModalList.get(i).getTitle(),
                        getPackagesModalList.get(i).getLocations(), "₹" + getPackagesModalList.get(i).getPrice() + "/person", ""));
            }
        }
        bestOfferRecycler.setVisibility(View.VISIBLE);
        shimmerFrameLayoutBestOffer.stopShimmer();
        shimmerFrameLayoutBestOffer.setVisibility(View.GONE);

        bestOfferAdapter = new BestDealsAdapter(bestDealsModals, getApplicationContext(), this);
        bestOfferRecycler.setAdapter(bestOfferAdapter);

    }

    //OnClick Listener for Best Deals Recycler
    @Override
    public void OnBestDealsClick(int position) {
        Log.e("Click", "OnBestDealsClick: Clicked");
        /*Intent intent=new Intent(this,ProfileActivity.class);
        intent.putExtra("Value",bestDealsAdapters.get(position));*/
        startActivity(new Intent(this, PackageDetailsActivity.class).putExtra("BestOfferDeals", getPackagesModalListBest.get(position)));
    }

    //******************************Popular places Recycler to add into dashboard*******************************
    private void popularPlacesRecycler() {
        popularPlacesRecycler.setHasFixedSize(true);
        popularPlacesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getPackagesModalListPopular = new ArrayList<>();
        for (int i = 0; i < getPackagesModalList.size(); i++) {
            if (getPackagesModalList.get(i).getTag().equals("POPULAR")) {
                getPackagesModalListPopular.add(getPackagesModalList.get(i));
                popularPlacesModals.add(new PopularPlacesModal(getPackagesModalList.get(i).getImages(), getPackagesModalList.get(i).getTitle(),
                        getPackagesModalList.get(i).getLocations(), "₹" + getPackagesModalList.get(i).getPrice() + "/person", ""));
            }
        }
        popularPlacesRecycler.setVisibility(View.VISIBLE);
        shimmerFrameLayoutPopularPlaces.stopShimmer();
        shimmerFrameLayoutPopularPlaces.setVisibility(View.GONE);

        popularPlacesAdapter = new PopularPlacesAdapter(popularPlacesModals, getApplicationContext(), this);
        popularPlacesRecycler.setAdapter(popularPlacesAdapter);
    }

    //OnClick Listener for Popular Deals Recycler
    @Override
    public void OnPopularPlacesClick(int position) {
        Log.e("Click", "OnPopularPlacesClick: Clicked");
        startActivity(new Intent(this, PackageDetailsActivity.class).putExtra("PopularPlacesDeals", getPackagesModalListPopular.get(position)));
    }

    //******************************Navigation Drawer Functions*************************************************
    private void navigationView() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_menu_home);
        View headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.user_name_textView);
        user_username = headerView.findViewById(R.id.user_username_textView);
        menuOpenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //To animate on open Navigation Drawer
        animateNavigationDrawer();
        getProfile();
    }

    //****Animation of Drawer*****
    private void animateNavigationDrawer() {
        //drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                float END_SCALE = 0.9f;
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                linearLayout.setScaleX(offsetScale);
                linearLayout.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = linearLayout.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                linearLayout.setTranslationX(xTranslation);
            }
        });
    }

    //*******************************Gettting details from server for recycler***********************************************
    private void getImage() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                client = new OkHttpClient();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url(URLGetPackages)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        Log.e("ERROR", "SERVER ERROR");
                    } else {
                        String serverResponse = response.body().string();
                        JSONObject jObject = new JSONObject(serverResponse);
                        JSONArray packageList = jObject.getJSONArray("body");
                        getPackagesModalList = new ArrayList<>();
                        for (int i = 0; i < packageList.length(); i++) {
                            JSONObject packageJson = (JSONObject) packageList.get(i);
                            GetPackagesModal getPackagesModal = new GetPackagesModal();
                            /*Log.e("JSON", packageJson.get("id").toString());
                            Log.e("JSON", packageJson.get("title").toString());
                            Log.e("JSON", packageJson.get("days").toString());
                            Log.e("JSON", packageJson.get("nights").toString());
                            Log.e("JSON", packageJson.get("price").toString());
                            Log.e("JSON",packageJson.get("description").toString());
                            Log.e("JSON", packageJson.get("create_time").toString());
                            Log.e("JSON", packageJson.getJSONArray("images").toString());
                            Log.e("JSON", packageJson.getJSONArray("locations").toString());*/
                            getPackagesModal.setId(packageJson.get("id").toString());
                            getPackagesModal.setTitle(packageJson.get("title").toString());
                            getPackagesModal.setImages(packageJson.getJSONArray("images").get(0).toString());
                            getPackagesModal.setLocations(packageJson.getJSONArray("locations").get(0).toString());
                            getPackagesModal.setDays(packageJson.get("days").toString());
                            getPackagesModal.setNights(packageJson.get("nights").toString());
                            getPackagesModal.setPrice(packageJson.get("price").toString());
                            getPackagesModal.setDescription(packageJson.get("description").toString());
                            getPackagesModal.setTag(packageJson.get("tag").toString());
                            getPackagesModal.setCreateTime(packageJson.get("create_time").toString());
                            String[] locations = new String[packageJson.getJSONArray("locations").length()];
                            String[] images = new String[packageJson.getJSONArray("images").length()];
                            for (int j = 0; j < packageJson.getJSONArray("images").length(); j++) {
                                images[j] = packageJson.getJSONArray("images").get(j).toString();
                                //Log.e("Array", packageJson.getJSONArray("images").get(j).toString());
                            }
                            for (int k = 0; k < packageJson.getJSONArray("locations").length(); k++) {
                                locations[k] = packageJson.getJSONArray("locations").get(k).toString();
                                //Log.e("Array", locations[k]);
                            }
                            getPackagesModal.setImagesArray(images);
                            getPackagesModal.setLocationsArray(locations);
                            getPackagesModalList.add(getPackagesModal);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bestOfferRecycler();
                                popularPlacesRecycler();
                                //To load view all places fragment
                                ViewAllTextView.setVisibility(View.VISIBLE);
                                ViewAllPlacesCall();
                            }
                        });

                    }
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //********************************To get User Profile***********************************************
    private void getProfile() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                client = new OkHttpClient().newBuilder()
                        .cookieJar(new CookieJar() {
                            @Override
                            public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {

                            }

                            @NotNull
                            @Override
                            public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                                final ArrayList<Cookie> oneCookie = new ArrayList<>(1);
                                oneCookie.add(createNonPersistentCookie());
                                //Log.e("Cookies",oneCookie.toString());
                                return oneCookie;

                            }
                        }).build();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url(URLGETUser)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();

                    if (!response.isSuccessful()) {
                        Log.e("ERROR:", "Server down");
                    } else {
                        String jsonData = response.body().string();
                        final JSONObject jObject = new JSONObject(jsonData);
                        //Log.e("JSON", jObject.get("body").toString());
                        //Log.e("JSON", jObject.getJSONObject("body").get("name").toString());
                        // Log.e("JSON", jObject.getJSONObject("body").get("username").toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    user_name.setText(jObject.getJSONObject("body").get("name").toString());
                                    user_username.setText(jObject.getJSONObject("body").get("username").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    response.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Cookie createNonPersistentCookie() {
        return new Cookie.Builder()
                .domain(ConstantValues.DOMAIN)
                .path("/api/logout")
                .name("JSESSIONID")
                .value(restoredText)
                .httpOnly()
                .secure()
                .build();
    }
}

