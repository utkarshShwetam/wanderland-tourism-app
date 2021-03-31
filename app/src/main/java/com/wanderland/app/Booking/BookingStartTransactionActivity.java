package com.wanderland.app.Booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.DashBoard.DataModals.GetPackagesModal;
import com.wanderland.app.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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

public class BookingStartTransactionActivity extends AppCompatActivity {


    GetPackagesModal getPackagesModal;
    String noOfPerson, cardHolderName, cardNo, exp, cardType;
    public static String restoredText;
    private OkHttpClient client;
    private Response response;
    boolean checkBack = true, cardSave;

    String URLStartTransaction = ConstantValues.API +"/user/start-transaction";
    String URLSaveCard = ConstantValues.API+"/user/save-card";

    @Override
    public void onBackPressed() {
        if (!checkBack) {
            super.onBackPressed();
            finish();
            overridePendingTransition(0, R.anim.right_to_left);
        }
        Log.e("Transaction", "onBackPressed:" + checkBack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_booking_start_transaction);

        if (getIntent().hasExtra("Package")) {
            getPackagesModal = getIntent().getParcelableExtra("Package");
            Log.e("Booking", "onCreate:" + getPackagesModal.getTitle());
        }
        if (getIntent().hasExtra("noOfPerson")) {
            noOfPerson = getIntent().getStringExtra("noOfPerson");
            Log.e("Booking", "onCreate:" + noOfPerson);
        }
        if (getIntent().hasExtra("CardName")) {
            cardHolderName = getIntent().getStringExtra("CardName");
            Log.e("Booking", "onCreate:" + cardHolderName);
        }
        if (getIntent().hasExtra("CardNumber")) {
            cardNo = getIntent().getStringExtra("CardNumber");
            Log.e("Booking", "onCreate:" + cardNo);
        }
        if (getIntent().hasExtra("CardType")) {
            cardType = getIntent().getStringExtra("CardType");
            Log.e("Booking", "onCreate:" + cardType);
        }
        if (getIntent().hasExtra("CardExp")) {
            exp = getIntent().getStringExtra("CardExp");
            Log.e("Booking", "onCreate:" + exp);
        }
        if (getIntent().hasExtra("SaveCard")) {
            cardSave = getIntent().getBooleanExtra("SaveCard", false);
            Log.e("Booking", "onCreate:" + cardSave);
        }

        //Id from Shared pref
        SharedPreferences prefs = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        restoredText = prefs.getString("KEY", null);
        if (restoredText != null) {
            //Log.e("KEY", restoredText);
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText = "";
        }
        init();
    }

    private void init() {
        TransactionProgressIndicatorFragment transactionProgressIndicatorFragment = new TransactionProgressIndicatorFragment();
        doFragmentTransaction(transactionProgressIndicatorFragment, getString(R.string.fragment_progress_indicator), false, "");
        checkBack = false;
        /*TransactionFailed transactionFailed=new TransactionFailed();
        doFragmentTransaction(transactionFailed,getString(R.string.transaction_failed),false,"");*/
        /*TransactionSuccess transactionSuccess=new TransactionSuccess();
        doFragmentTransaction(transactionSuccess,getString(R.string.transaction_success),false,"");*/
        if (cardSave)
            SaveCard();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.e("Booking", "run:Delayed");
                StartTransaction();
            }
        }, 5000);

    }

    private void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack, String msg) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_transaction_container_view, fragment, tag);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(msg);
        }
        fragmentTransaction.commit();

    }



    private void SaveCard() {
        //***************************************Transaction*******************************************
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", cardHolderName);
                    jsonObject.put("number", cardNo);
                    jsonObject.put("expiration", exp);
                    jsonObject.put("type", cardType);
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
                client.connectTimeoutMillis();
                client.readTimeoutMillis();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url(URLSaveCard)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        Log.e("Booking Activity", "run:Card Save failed");
                    } else {
                        //Log.e("Card", "run: "+response.toString());
                        Log.e("Card", "run: "+response.body().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.close();
            }

        });
    }
    //**************************************Transaction Start**********************************************************
    private void StartTransaction() {
        //***************************************Transaction*******************************************
        Log.e("pack", "StartTransaction: "+getPackagesModal.getId());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("package_id", getPackagesModal.getId());
                    jsonObject.put("people", noOfPerson);
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
                client.connectTimeoutMillis();
                client.readTimeoutMillis();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url(URLStartTransaction)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        //move screen to failed screen
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TransactionFailedFragment transactionFailed = new TransactionFailedFragment();
                                doFragmentTransaction(transactionFailed, getString(R.string.transaction_failed), false, "");
                                Log.e("Booking Activity", "run:Transaction failed");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AfterBookingFailedFragment afterBookingFailedFragment=new AfterBookingFailedFragment();
                                        doFragmentTransaction(afterBookingFailedFragment,"Booking Failed",false,"");
                                    }
                                },4000);
                            }
                        });

                    } else {
                        //Move screen to success screen
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TransactionSuccessFragment transactionSuccess = new TransactionSuccessFragment();
                                doFragmentTransaction(transactionSuccess, getString(R.string.transaction_success), false, "");
                                Log.e("Booking Activity", "run:Transaction Successful");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AfterBookingFragment afterBookingFragment=new AfterBookingFragment();
                                        doFragmentTransaction(afterBookingFragment,"After Booking",false,"");
                                    }
                                },4000);
                            }
                        });

                    }
                    checkBack = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.close();
            }

        });
    }



    public static Cookie createNonPersistentCookie() {

        return new Cookie.Builder()
                .domain(ConstantValues.DOMAIN)
                .path("/user/start-transaction")
                .name("JSESSIONID")
                .value(restoredText)
                .httpOnly()
                .secure()
                .build();
    }
}