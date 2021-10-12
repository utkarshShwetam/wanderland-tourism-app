package com.wanderland.app.TransactionHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.DashBoard.MainDashboard;
import com.wanderland.app.LoginSignUp.LogOutActivity;
import com.wanderland.app.R;
import com.wanderland.app.TransactionHistory.DataModals.TransactionHistoryModal;
import com.wanderland.app.TransactionHistory.ViewAdapter.TransactionHistoryViewAdapter;

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

public class TransactionHistoryActivity extends AppCompatActivity {

    RecyclerView transactionRecyclerView;
    RecyclerView.Adapter<TransactionHistoryViewAdapter.TransactionHistoryViewHolder> transactionAdapter;
    ShimmerFrameLayout transactionHistoryShimmer;
    ArrayList<TransactionHistoryModal> transactionHistoryModalArrayList = new ArrayList<>();
    ImageView imageView;
    TextView noTransactionFound;

    //OkHttp
    OkHttpClient client;
    Response response;
    private final String URLTransactionHistory = ConstantValues.API +"/user/get-transaction-history";

    static String restoredText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_layout);
        customType(TransactionHistoryActivity.this, "left-to-right");
        transactionHistoryShimmer = findViewById(R.id.shimmer_view_container_transaction_history);
        transactionRecyclerView = findViewById(R.id.transaction_recycler);
        transactionRecyclerView.setNestedScrollingEnabled(false);
        imageView = findViewById(R.id.back_press_transaction_history);
        noTransactionFound=findViewById(R.id.noTransaction);
        noTransactionFound.setVisibility(View.GONE);

        transactionRecyclerView.setVisibility(View.GONE);
        transactionHistoryShimmer.setVisibility(View.VISIBLE);
        transactionHistoryShimmer.startShimmer();

        SharedPreferences prefs = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        restoredText = prefs.getString("KEY", null);
        if (restoredText != null) {
            Log.e("KEY", "Present");
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText = "";
        }

        getTransactionHistory();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(getApplicationContext(), MainDashboard.class));*/
                overridePendingTransition(0, R.anim.right_to_left);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*startActivity(new Intent(getApplicationContext(), MainDashboard.class));
        customType(TransactionHistoryActivity.this, "right-to-left");*/
        finish();
    }

    ///************************Transaction History Recycler***********************************
    private void transactionHistoryRecycler() {
        transactionRecyclerView.setHasFixedSize(true);
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<TransactionHistoryModal> transactionHistoryList = new ArrayList<>();
        for (int i = transactionHistoryModalArrayList.size()-1; i >=0; i--) {
            transactionHistoryList.add(new TransactionHistoryModal(
                    "ID: " + transactionHistoryModalArrayList.get(i).getTransaction_id(),
                    transactionHistoryModalArrayList.get(i).getPackage_id(),
                    transactionHistoryModalArrayList.get(i).getUser_id(),
                    "Payment Mode: " + transactionHistoryModalArrayList.get(i).getPayment_method(),
                    "₹ " + transactionHistoryModalArrayList.get(i).getAmount(),
                    transactionHistoryModalArrayList.get(i).getDescription(),
                    "Status: " + transactionHistoryModalArrayList.get(i).getStatus(),
                    transactionHistoryModalArrayList.get(i).getCreate_time()));
        }


        transactionRecyclerView.setVisibility(View.VISIBLE);
        transactionHistoryShimmer.setVisibility(View.GONE);
        transactionHistoryShimmer.stopShimmer();

        transactionAdapter = new TransactionHistoryViewAdapter(transactionHistoryList, this);
        transactionRecyclerView.setAdapter(transactionAdapter);
    }
    //*****************************************To get user transaction history********************************************
    private void getTransactionHistory() {
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
                        .url(URLTransactionHistory)
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
                        Log.e("Length", String.valueOf(jsonArray.length()));
                        if(jsonArray.length()==0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    noTransactionFound.setVisibility(View.VISIBLE);
                                    transactionHistoryShimmer.setVisibility(View.GONE);
                                    transactionHistoryShimmer.stopShimmer();
                                }
                            });

                        }
                        else {
                            Log.e("JSON", jObject.get("status").toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                TransactionHistoryModal transactionDataHistoryModal = new TransactionHistoryModal();
                                transactionDataHistoryModal.setTransaction_id(object.get("id").toString());
                                transactionDataHistoryModal.setPackage_id(object.get("package_id").toString());
                                transactionDataHistoryModal.setUser_id(object.get("user_id").toString());
                                transactionDataHistoryModal.setPayment_method(object.get("payment_method").toString());
                                transactionDataHistoryModal.setAmount(object.get("amount").toString());
                                transactionDataHistoryModal.setDescription(object.get("description").toString());
                                transactionDataHistoryModal.setStatus(object.get("status").toString());
                                String date = object.get("create_time").toString();
                                String[] s = date.split("T");
                                transactionDataHistoryModal.setCreate_time(s[0]);
                                transactionHistoryModalArrayList.add(transactionDataHistoryModal);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transactionHistoryRecycler();
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
                .path("/user/get-transaction-history")
                .name("JSESSIONID")
                .value(restoredText)
                .httpOnly()
                .secure()
                .build();
    }

}