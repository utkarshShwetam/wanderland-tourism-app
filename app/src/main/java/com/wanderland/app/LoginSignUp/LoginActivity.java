package com.wanderland.app.LoginSignUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wanderland.app.Constants.ConstantValues;
import com.wanderland.app.DashBoard.MainDashboard;
import com.wanderland.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;
import static maes.tech.intentanim.CustomIntent.customType;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText passwordDetails;
    TextInputEditText usernameDetails;
    TextInputLayout forErrorDisplayPassword;
    TextInputLayout forErrorDisplayUsername;
    TextView signUP;
    MaterialButton materialButton;


    //Login parameters OkHttp
    private OkHttpClient client;
    private Response response;
    private String[] JSESSIONID_INFO;
    final String URLLogin = ConstantValues.API + "/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //animation
        customType(LoginActivity.this, "left-to-right");

        usernameDetails = findViewById(R.id.usernameTextField);
        passwordDetails = findViewById(R.id.passwordTextField);
        signUP = findViewById(R.id.link_logIn);
        materialButton = findViewById(R.id.login_button);

        forErrorDisplayPassword = findViewById(R.id.passwordTextInputLayout);
        forErrorDisplayUsername = findViewById(R.id.usernameTextInputLayout);

        //Button Animation
        PushDownAnim.setPushDownAnimTo(materialButton)
                .setScale(MODE_SCALE, 0.89f);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //***************To close Keyboard****************
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    //*******************************Navigate to SignUp if not registered***************************
    public void navigationToRegister(View view) {
        startActivity(new Intent(this, RegisterUserActivity.class));
    }

    //*******************************To check if string is NULL*************************************
    private boolean isStringNull(String string) {
        return string.equals("");
    }


    private void textOnChangeListenerUsername() {
        usernameDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                forErrorDisplayUsername.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerPassword() {
        passwordDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                forErrorDisplayPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //*******************************Getting details from Login form**************************************
    public void OnLogInClick(View view) {
        final String username = usernameDetails.getText().toString();
        final String password = passwordDetails.getText().toString();
        if (isStringNull(username) || isStringNull(password)) {
            if (isStringNull(username)) {
                forErrorDisplayUsername.setError("Email Required");
                textOnChangeListenerUsername();
            }
            if (isStringNull(password)) {
                forErrorDisplayPassword.setError("Password Required");
                textOnChangeListenerPassword();
            }
        } else {
            //*******************************Sign in procedure check****************************************
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", username);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    client = new OkHttpClient();
                    client.connectTimeoutMillis();
                    client.readTimeoutMillis();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                    Request request = new Request.Builder()
                            .url(URLLogin)
                            .post(body)
                            .build();

                    try {
                        response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    forErrorDisplayPassword.setError("Wrong Password");
                                    textOnChangeListenerPassword();
                                }
                            });
                        } else {
                            Log.e("Response", response.toString());
                            String a = response.headers("Set-Cookie").toString();
                            String[] cookiesInfo = a.split(";");
                            JSESSIONID_INFO = cookiesInfo[0].split("=");
                            try {
                                //Log.e("J_SESSION_ID", JSESSIONID_INFO[1]);
                                try {
                                    SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("KEY", JSESSIONID_INFO[1]);
                                    editor.apply();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (ArrayIndexOutOfBoundsException e) {
                                Log.e("Error", "No sessions id found");
                            }
                            String jsonData = response.body().string();
                            final JSONObject jObject = new JSONObject(jsonData);
                            Log.e("JSON", jObject.get("status").toString());
                            Log.e("JSON", jObject.get("body").toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (!jObject.get("body").toString().equals("login successful")) {
                                            forErrorDisplayUsername.setError(jObject.get("body").toString());
                                            textOnChangeListenerUsername();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "WELCOME", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainDashboard.class));
                                            customType(LoginActivity.this, "left-to-right");
                                            finish();

                                        }
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
    }
}