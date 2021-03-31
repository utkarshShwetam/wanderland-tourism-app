package com.wanderland.app.LoginSignUp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wanderland.app.Constants.ConstantValues;
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

public class RegisterUserActivity extends AppCompatActivity {
    //**************Variables****************
    TextInputEditText emailDetails;
    TextInputEditText nameDetails;
    TextInputEditText passwordDetails;
    TextInputLayout ForErrorDisplayEmail;
    TextInputLayout ForErrorDisplayUsername;
    TextInputLayout ForErrorDisplayPassword;
    MaterialButton materialButton;

    //Register params OkHttp
    OkHttpClient client;
    Response response;

    final String URLRegister = ConstantValues.API +"/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //animation
        customType(RegisterUserActivity.this, "left-to-right");
        //getting ids
        emailDetails = findViewById(R.id.emailSignUpTextField);
        nameDetails = findViewById(R.id.userNameSignUpTextField);
        passwordDetails = findViewById(R.id.passwordTextField);
        ForErrorDisplayEmail = findViewById(R.id.emailRegisterPageTextInputLayout);
        ForErrorDisplayUsername = findViewById(R.id.nameRegisterPageTextInputLayout);
        ForErrorDisplayPassword = findViewById(R.id.passwordRegisterPageTextInputLayout);
        materialButton=findViewById(R.id.register_button);
        PushDownAnim.setPushDownAnimTo(materialButton)
                .setScale(MODE_SCALE,0.89f);
        /*backImage=findViewById(R.id.backToLoginButton);*/

        //navigate to login on image click
        /*backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customType(RegisterUserActivity.this, "right-to-left");

            }
        });*/
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customType(RegisterUserActivity.this, "right-to-left");
        finish();
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

    //**************************To check if text changed************************************
    private void textOnChangeListenerPassword() {

        passwordDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ForErrorDisplayPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerUsername() {

        emailDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ForErrorDisplayEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerName() {

        nameDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ForErrorDisplayUsername.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //****************************************To check string is null or not*******************************************
    private boolean isStringNull(String string) {
        return string.equals("");
    }

    String name;

    //****************************************Getting details from the form view****************************************
    public void OnRegisterClick(View view) {
        final String email = emailDetails.getText().toString();
        name = nameDetails.getText().toString();
        final String password = passwordDetails.getText().toString();
        if (isStringNull(email) || isStringNull(password) || isStringNull(name)) {
            if (isStringNull(email)) {
                ForErrorDisplayEmail.setError("Email Required");
                textOnChangeListenerUsername();
            }
            if (isStringNull(password)) {
                ForErrorDisplayPassword.setError("Password Required");
                textOnChangeListenerPassword();
            }
            if (isStringNull(name)) {
                ForErrorDisplayUsername.setError("Name Required");
                textOnChangeListenerName();
            }
        } else {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("username", email);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    client= new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
                    Request request = new Request.Builder()
                            .url(URLRegister)
                            .post(body)
                            .build();

                    try {
                        response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterUserActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            String jsonData = response.body().string();
                            final JSONObject jObject = new JSONObject(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if(!jObject.get("body").toString().equals("register successful")){
                                            ForErrorDisplayEmail.setError(jObject.get("body").toString());
                                            textOnChangeListenerUsername();
                                        }else{
                                            Toast.makeText(RegisterUserActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(RegisterUserActivity.this, "Press back to login", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            response.close();
                        }
                        } catch(IOException | JSONException e){
                            e.printStackTrace();
                        }
                }
            });
        }

    }
}