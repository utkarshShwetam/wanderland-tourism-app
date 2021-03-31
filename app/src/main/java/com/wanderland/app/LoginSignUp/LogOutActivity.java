package com.wanderland.app.LoginSignUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.DashBoard.MainDashboard;
import com.wanderland.app.R;

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

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;
import static maes.tech.intentanim.CustomIntent.customType;

public class LogOutActivity extends AppCompatActivity {

    MaterialButton signOutContinue, signOutCancel;


    public static String restoredText;
    //OkHttp
    OkHttpClient client;
    Response response;
    private final String URLLogOutCheckStatus = ConstantValues.API +"/logout";

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
        signOutContinue = findViewById(R.id.sign_out_continue_button);
        signOutCancel = findViewById(R.id.sign_out_cancel_button);
        customType(LogOutActivity.this, "left-to-right");
        SharedPreferences prefs = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        restoredText = prefs.getString("KEY", null);
        if (restoredText != null) {
            Log.e("KEY", "Present");
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText = "";
        }
        PushDownAnim.setPushDownAnimTo(signOutContinue)
                .setScale(MODE_SCALE, 0.89f);
        PushDownAnim.setPushDownAnimTo(signOutCancel)
                .setScale(MODE_SCALE, 0.89f);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
        customType(LogOutActivity.this, "right-to-left");
        finish();
    }

    //***************************ON cancel click**************************************
    public void OnCancelClickLogOut(View v) {
        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }


    public void OnContinueClickLogOut(View view) {
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
                        .url(URLLogOutCheckStatus)
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        Log.e("ERROR:", "Server down");
                    } else {
                        Log.e("Response", response.toString());
                        String jsonData = response.body().string();
                        final JSONObject jObject = new JSONObject(jsonData);
                        Log.e("JSON", jObject.get("status").toString());
                        Log.e("JSON", jObject.get("body").toString());
                        if (jObject.get("body").toString().equals("logout successful") && jObject.get("status").toString().equals("200")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    getApplicationContext().getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", 0).edit().clear().apply();
                                    finish();
                                }
                            });
                        }

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
                .path("/logout")
                .name("JSESSIONID")
                .value(LogOutActivity.restoredText)
                .httpOnly()
                .secure()
                .build();
    }
}