package com.wanderland.app.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.DashBoard.MainDashboard;
import com.wanderland.app.LoginSignUp.LoginActivity;
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


public class SplashScreenActivity extends AppCompatActivity {

    //Variables
    ImageView imageView;
    TextView textView;

    Animation sideAnim, bottomAnim;
    SharedPreferences preferences;

    //Login parameters OkHttp
    OkHttpClient client;
    Response response;
    public static String restoredText;

    //SignIn check
    final String URLLoginCheckStatus = ConstantValues.API +"/login-status";

    //*****************************JNI KEY FETCH****************************************************
    static {
        System.loadLibrary("keys");
    }
    public static native String getNativeKeySplashScreen1();
    public static native String getNativeKeySplashScreen2();


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public SplashScreenActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Getting ids
        textView = findViewById(R.id.splash_text);
        imageView = findViewById(R.id.logo_imageView);
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);


        preferences = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        restoredText = preferences.getString("KEY", null);
        if (restoredText != null) {
            Log.e("KEY_Stored", restoredText);
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText="";
        }
        launchCheck();
    }


    private void launchCheck() {
        preferences=getSharedPreferences("WALK_THROUGH_SCREEN",Context.MODE_PRIVATE);
        boolean firstLaunch=preferences.getBoolean("FIRST_TIME",true);
        Log.e("FIRST_CHECK", String.valueOf(firstLaunch));
        if(firstLaunch){
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("FIRST_TIME",false);
            editor.apply();
            Log.e("FIRST_CHECK", String.valueOf(preferences.getBoolean("FIRST_TIME",true)));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),WalkThroughActivity.class));
                    finish();
                }
            },2000);

        }else {
            LoginCheck();
        }
    }

    private void LoginCheck() {
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
                        .url(URLLoginCheckStatus)
                        .post(body)
                        .build();
                try {
                    for (int i=0;i<10;i++){
                        response = client.newCall(request).execute();
                        if (response.isSuccessful())
                            break;
                    }
                    if (!response.isSuccessful()) {
                        Log.e("ERROR:", "Server down");
                        //Show an error page in the app
                    } else {
                        //Log.e("Response", response.toString());
                        String jsonData = response.body().string();
                        final JSONObject jObject = new JSONObject(jsonData);
                        Log.e("JSON", jObject.get("status").toString());
                        Log.e("JSON", jObject.get("body").toString());
                        if (jObject.get("body").toString().equals("false") && jObject.get("status").toString().equals("200")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //startActivity(new Intent(getApplicationContext(),WalkThroughActivity.class));
                                    startActivity(new Intent(getApplicationContext(), MainDashboard.class));
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
                .path("/login-status")
                .name("JSESSIONID")
                .value(SplashScreenActivity.restoredText)
                .httpOnly()
                .secure()
                .build();
    }
}