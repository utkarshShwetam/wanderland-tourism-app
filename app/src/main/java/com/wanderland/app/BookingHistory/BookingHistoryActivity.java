package com.wanderland.app.BookingHistory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.wanderland.app.BookingHistory.Modal.BookingHistoryModal;
import com.wanderland.app.BookingHistory.ViewAdapter.BookingHistoryViewAdapter;
import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.R;

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

import static maes.tech.intentanim.CustomIntent.customType;

public class BookingHistoryActivity extends AppCompatActivity {
    private RecyclerView bookingHistoryRecycler;
    private RecyclerView.Adapter<BookingHistoryViewAdapter.BookingHistoryViewHolder> bookingHistoryAdapter;
    private ShimmerFrameLayout bookingHistoryShimmer;
    private ArrayList<BookingHistoryModal> bookingHistoryModalArrayList = new ArrayList<>();
    private ImageView imageView;
    private TextView noBookingHistory;

    static String restoredText;
    //OkHttp
    OkHttpClient client;
    Response response;
    final String URLBookingHistory = ConstantValues.API + "/user/get-booking-history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        customType(BookingHistoryActivity.this, "left-to-right");
        bookingHistoryRecycler = findViewById(R.id.booking_recycler);
        noBookingHistory = findViewById(R.id.noBookingHistory);
        bookingHistoryShimmer = findViewById(R.id.shimmer_view_container_booking_history);
        imageView = findViewById(R.id.back_press_booking_history);
        bookingHistoryRecycler.setVisibility(View.INVISIBLE);
        bookingHistoryShimmer.setVisibility(View.VISIBLE);
        bookingHistoryShimmer.startShimmer();

        SharedPreferences prefs = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        restoredText = prefs.getString("KEY", null);
        if (restoredText != null) {
            Log.e("KEY", "Present");
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText = "";
        }
        getHistory();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, R.anim.right_to_left);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void bookingHistoryRecycler() {
        bookingHistoryRecycler.setHasFixedSize(true);
        bookingHistoryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<BookingHistoryModal> bookingHistoryModals = new ArrayList<>();
        for (int i = bookingHistoryModalArrayList.size()-1; i >= 0; i--) {
            bookingHistoryModals.add(new BookingHistoryModal(
                    "ID: " + bookingHistoryModalArrayList.get(i).getTransaction_id(),
                    bookingHistoryModalArrayList.get(i).getPackage_id(),
                    "Days: " + bookingHistoryModalArrayList.get(i).getDays(),
                    "Nights: " + bookingHistoryModalArrayList.get(i).getNights(),
                    "People: " + bookingHistoryModalArrayList.get(i).getPeople(),
                    "₹ " + bookingHistoryModalArrayList.get(i).getAmount(),
                    bookingHistoryModalArrayList.get(i).getTransaction_id(),
                    bookingHistoryModalArrayList.get(i).getUser_id(),
                    bookingHistoryModalArrayList.get(i).getCreate_time(),
                    "Locations: " + bookingHistoryModalArrayList.get(i).getLocations(),
                    bookingHistoryModalArrayList.get(i).getLocationsArray()));
            //Log.e("Recycler", "bookingHistoryRecycler: "+bookingHistoryModalArrayList.get(i).getLocations());
        }

        bookingHistoryRecycler.setVisibility(View.VISIBLE);
        bookingHistoryShimmer.setVisibility(View.GONE);
        bookingHistoryShimmer.stopShimmer();

        bookingHistoryAdapter = new BookingHistoryViewAdapter(bookingHistoryModals, this);
        bookingHistoryRecycler.setAdapter(bookingHistoryAdapter);

    }

    //***********************************GET booking history**************************************************
    private void getHistory() {

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
                        .url(URLBookingHistory)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        Log.e("ERROR:", "Server down");
                    } else {
                        Log.e("Response", response.toString());
                        String serverResponse = response.body().string();
                        JSONObject jObject = new JSONObject(serverResponse);
                        JSONArray jsonArray = jObject.getJSONArray("body");
                        if (jsonArray.length() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    noBookingHistory.setVisibility(View.VISIBLE);
                                    bookingHistoryShimmer.setVisibility(View.GONE);
                                    bookingHistoryShimmer.stopShimmer();
                                }
                            });

                        } else {
                            Log.e("JSON", jObject.get("status").toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                BookingHistoryModal transactionDataHistoryModal = new BookingHistoryModal();
                                transactionDataHistoryModal.setTransaction_id(object.get("transaction_id").toString());
                                transactionDataHistoryModal.setPackage_id(object.get("package_id").toString());
                                transactionDataHistoryModal.setUser_id(object.get("user_id").toString());
                                transactionDataHistoryModal.setDays(object.get("days").toString());
                                transactionDataHistoryModal.setNights(object.get("nights").toString());
                                transactionDataHistoryModal.setPeople(object.get("people").toString());
                                transactionDataHistoryModal.setAmount(object.get("amount").toString());
                                String date = object.get("create_time").toString();
                                String[] s = date.split("T");
                                transactionDataHistoryModal.setCreate_time(s[0]);
                                String loc = "";
                                String[] locations = new String[object.getJSONArray("locations").length()];
                                for (int k = 0; k < object.getJSONArray("locations").length(); k++) {
                                    locations[k] = object.getJSONArray("locations").get(k).toString();
                                    if (k != object.getJSONArray("locations").length() - 1)
                                        loc = loc.concat(locations[k] + " ,");
                                    else
                                        loc = loc.concat(locations[k]);
                                }
                                transactionDataHistoryModal.setLocationsArray(locations);
                                transactionDataHistoryModal.setLocations(loc);
                                bookingHistoryModalArrayList.add(transactionDataHistoryModal);
                                //Log.e("SIZE", String.valueOf(bookingHistoryModalArrayList.size()));
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bookingHistoryRecycler();
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
                .path("/user/get-booking-history")
                .name("JSESSIONID")
                .value(restoredText)
                .httpOnly()
                .secure()
                .build();
    }
}