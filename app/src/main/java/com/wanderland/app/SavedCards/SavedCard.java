package com.wanderland.app.SavedCards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cooltechworks.creditcarddesign.CreditCardView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.DashBoard.MainDashboard;
import com.wanderland.app.LoginSignUp.LogOutActivity;
import com.wanderland.app.R;
import com.wanderland.app.SavedCards.DataModals.SavedCardModal;

import org.jetbrains.annotations.NotNull;
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

public class SavedCard extends AppCompatActivity {
    private static String restoredText;
    ShimmerFrameLayout shimmerFrameLayoutSavedCard;
    CreditCardView creditCardView;
    ImageView backPress;
    TextView noCard;
    FloatingActionButton deleteCard;
    SavedCardModal savedCardModal = new SavedCardModal();

    //OkHttp
    OkHttpClient client;
    Response response;
    String URLGetCard = ConstantValues.API +"/user/get-card";
    String URLDeleteCard = ConstantValues.API+"/user/delete-card";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_cards);
        //animation
        customType(SavedCard.this, "left-to-right");
        shimmerFrameLayoutSavedCard = findViewById(R.id.shimmer_view_container_saved_card);
        creditCardView = findViewById(R.id.card);
        noCard = findViewById(R.id.no_card_saved);
        noCard.setVisibility(View.GONE);
        backPress = findViewById(R.id.back_press_saved_card);
        deleteCard=findViewById(R.id.floating_action_delete_button);
        deleteCard.setVisibility(View.GONE);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                customType(SavedCard.this, "right-to-left");*/
                finish();
            }
        });


        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creditCardView.setVisibility(View.GONE);
                shimmerFrameLayoutSavedCard.setVisibility(View.VISIBLE);
                shimmerFrameLayoutSavedCard.startShimmer();
                deleteMyCard();
            }
        });

        SharedPreferences prefs = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        restoredText = prefs.getString("KEY", null);
        if (restoredText != null) {
            Log.e("KEY", "Present");
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText = "";
        }

        creditCardView.setVisibility(View.GONE);
        shimmerFrameLayoutSavedCard.setVisibility(View.VISIBLE);
        shimmerFrameLayoutSavedCard.startShimmer();


        //************GET CARD************************
        getCard();
    }
    //**********************************Delete Card**********************************************
    private void deleteMyCard() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        .url(URLDeleteCard)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    if(!response.isSuccessful()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SavedCard.this, "Server Error! Card not deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        String jsonData = response.body().string();
                        final JSONObject jObject = new JSONObject(jsonData);
                        if(jObject.get("body").toString().equals("card deleted") && jObject.get("status").toString().equals("200")){
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Toast.makeText(SavedCard.this, "Card Deleted", Toast.LENGTH_SHORT).show();
                                   shimmerFrameLayoutSavedCard.setVisibility(View.GONE);
                                   shimmerFrameLayoutSavedCard.stopShimmer();
                                   noCard.setVisibility(View.VISIBLE);
                                   deleteCard.setVisibility(View.GONE);
                               }
                           });
                        }

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }
    //**********************************Getting user Saved Card************************************************
    private void getCard() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        .url(URLGetCard)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        Log.e("ERROR:", "Server down");
                    } else {
                        String jsonData = response.body().string();
                        final JSONObject jObject = new JSONObject(jsonData);
                        JSONObject Object = new JSONObject(jObject.get("body").toString());
                        if (Object.get("name").toString().isEmpty() || Object.get("number").toString().isEmpty()) {
                            savedCardModal.setCardHolderName("");
                            savedCardModal.setNumber("");
                            savedCardModal.setExpiration("");
                            savedCardModal.setType("");
                        } else {
                            savedCardModal.setCardHolderName(Object.get("name").toString());
                            savedCardModal.setNumber(Object.get("number").toString());
                            savedCardModal.setExpiration(Object.get("expiration").toString());
                            savedCardModal.setType(Object.get("type").toString());
                        }
                    }
                    response.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if (savedCardModal.getCardHolderName().isEmpty() || savedCardModal.getNumber().isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            noCard.setVisibility(View.VISIBLE);
                            deleteCard.setVisibility(View.GONE);
                            shimmerFrameLayoutSavedCard.setVisibility(View.GONE);
                            shimmerFrameLayoutSavedCard.stopShimmer();

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            creditCardView.setCardHolderName(savedCardModal.getCardHolderName());
                            creditCardView.setCardExpiry(savedCardModal.getExpiration());
                            creditCardView.setCardNumber(savedCardModal.getNumber());

                            creditCardView.setVisibility(View.VISIBLE);
                            deleteCard.setVisibility(View.VISIBLE);
                            shimmerFrameLayoutSavedCard.setVisibility(View.GONE);
                            shimmerFrameLayoutSavedCard.stopShimmer();
                        }
                    });
                }
            }
        });

    }


    public static Cookie createNonPersistentCookie() {

        return new Cookie.Builder()
                .domain(ConstantValues.DOMAIN)
                .path("/user/delete-card")
                .name("JSESSIONID")
                .value(restoredText)
                .httpOnly()
                .secure()
                .build();
    }

}