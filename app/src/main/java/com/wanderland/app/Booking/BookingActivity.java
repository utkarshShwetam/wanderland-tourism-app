package com.wanderland.app.Booking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.thekhaeng.pushdownanim.PushDownAnim;
import com.wanderland.app.DashBoard.DataModals.GetPackagesModal;
import com.wanderland.app.R;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;
import static maes.tech.intentanim.CustomIntent.customType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingActivity extends AppCompatActivity {

    MaterialButton button;
    ImageView packageImageView, backButtonImageView;
    TextView placeTextView, locationsTextView, daysTextView, nightsTextView, priceTextView;
    TextInputLayout nameTextInputLayout, addressTextInputLayout, emailTextInputLayout,
            phoneTextInputLayout, zipTextInputLayout, stateTextInputLayout,
            noOfPersonsTextInputLayout;
    TextInputEditText nameTextInputEditText, addressTextInputEditText,
            emailTextInputEditText, phoneTextInputEditText, zipTextInputEditText,
            noOfPersonsTextInputEditText;
    AutoCompleteTextView stateAutoCompleteTextView, editTextFilledExposedDropdown;
    CardForm cardForm;

    GetPackagesModal getPackagesModal;
    String[] getLocations;
    StringBuilder loc = new StringBuilder();
    String name, phone, email, zip, noOfPerson, state, address;
    boolean valid, toSaveCard = false;
    String regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        customType(BookingActivity.this, "left-to-right");

        //Getting ids
        nameTextInputLayout = findViewById(R.id.name_textInput_layout);
        addressTextInputLayout = findViewById(R.id.address_textInput_layout);
        emailTextInputLayout = findViewById(R.id.email_textInput_layout);
        phoneTextInputLayout = findViewById(R.id.phone_number_textInput_layout);
        zipTextInputLayout = findViewById(R.id.zip_textInputLayout);
        noOfPersonsTextInputLayout = findViewById(R.id.noOfPerson_textInput_layout);
        nameTextInputEditText = findViewById(R.id.name_textInputEditText);
        addressTextInputEditText = findViewById(R.id.address_textInputEditText);
        emailTextInputEditText = findViewById(R.id.email_textInputEditText);
        phoneTextInputEditText = findViewById(R.id.phone_number_textInputEditText);
        zipTextInputEditText = findViewById(R.id.zip_textInputEditText);
        noOfPersonsTextInputEditText = findViewById(R.id.nofPersons_textInputEditText);
        stateAutoCompleteTextView = findViewById(R.id.State_exposed_dropdown);
        stateTextInputLayout = findViewById(R.id.State_TextInputLayout);
        button = findViewById(R.id.purchase_button);
        packageImageView = findViewById(R.id.checkout_package_selected_image);
        placeTextView = findViewById(R.id.package_destination_textView);
        locationsTextView = findViewById(R.id.all_places_display_textView);
        daysTextView = findViewById(R.id.days_set_textView);
        nightsTextView = findViewById(R.id.nights_set_textView);
        priceTextView = findViewById(R.id.price_set_textView);
        backButtonImageView = findViewById(R.id.booking_back_button);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim
                        .right_to_left);
            }
        });

        //Button Animation
        PushDownAnim.setPushDownAnimTo(button)
                .setScale(MODE_SCALE, 0.89f);

        //Id from Shared pref
        SharedPreferences prefs = getSharedPreferences("AUTHENTICATION_TOKEN_FILE_NAME", Context.MODE_PRIVATE);
        String restoredText = prefs.getString("KEY", null);
        if (restoredText != null) {
            //Log.e("KEY", restoredText);
        } else {
            Log.e("NO_KEY_IF", "No key found");
            restoredText = "";
        }

        //Payment Form
        cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .saveCardCheckBoxVisible(true)
                .setup(BookingActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        String[] type = new String[]{"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka",
                "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
                "Odisha", "Others", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
                "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, type);

        editTextFilledExposedDropdown = findViewById(R.id.State_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        //***********************Custom object getting*************************************
        if (getIntent().hasExtra("Checkout Package")) {
            getPackagesModal = getIntent().getParcelableExtra("Checkout Package");
            init();
        }
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

    private boolean isStringNull(String string) {
        return string.equals("");
    }

    //OnBack Press
    @Override
    public void onBackPressed() {
        super.finish();
        overridePendingTransition(0, R.anim
                .right_to_left);
        super.onBackPressed();
    }

    //*****************************Setting selected user package to the view***************************
    private void init() {
        String pricePer=getPackagesModal.getPrice()+"/person";
        getLocations = getPackagesModal.getLocationsArray();
        for (int i = 0; i < getPackagesModal.getLocationsArray().length; i++)
            if (i == getPackagesModal.getLocationsArray().length - 1)
                loc.append(getLocations[i]);
            else
                loc.append(getLocations[i]).append(",");
        placeTextView.setText(getPackagesModal.getTitle());
        locationsTextView.setText(loc);
        daysTextView.setText(getPackagesModal.getDays());
        nightsTextView.setText(getPackagesModal.getNights());
        priceTextView.setText(pricePer);
        Glide.with(getApplicationContext()).load(getPackagesModal.getImages())
                .thumbnail(0.5f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(packageImageView);
    }

    //*************************Checking form validity and not null cases***********************************
    boolean isFormValid(){
        boolean val=true;
        if (isStringNull(name)) {
            nameTextInputLayout.setError("Name Required");
            val = false;
            textOnChangeListenerName();
        }
        if (isStringNull(address)) {
            addressTextInputLayout.setError("Address Required");
            val = false;
            textOnChangeListenerAddress();
        }
        if (isStringNull(email)) {
            emailTextInputLayout.setError("Email Required");
            val = false;
            textOnChangeListenerEmail();
        }else{
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailTextInputLayout.setError("Invalid Email");
                val = false;
                textOnChangeListenerEmail();
            }
        }
        if (isStringNull(phone)) {
            phoneTextInputLayout.setError("Phone Required");
            val = false;
            textOnChangeListenerPhone();
        }else{
            if (phone.length()!=10) {
                phoneTextInputLayout.setError("Invalid Number");
                val = false;
                textOnChangeListenerPhone();
            }else if(!Patterns.PHONE.matcher(phone).matches()){
                phoneTextInputLayout.setError("Invalid Number");
                val = false;
                textOnChangeListenerEmail();
            }
        }
        if (isStringNull(zip)) {
            zipTextInputLayout.setError("Zip Required");
            val = false;
            textOnChangeListenerZip();
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(zip);
            if(!m.matches()) {
                zipTextInputLayout.setError("Invalid Zip");
                val = false;
                textOnChangeListenerZip();
            }
        }
        if (isStringNull(state)) {
            stateTextInputLayout.setError("State Required");
            val = false;
            textOnChangeListenerState();
        }
        if (noOfPerson.equals("0")) {
            noOfPersonsTextInputLayout.setError("Invalid");
            val = false;
            textOnChangeListenerNoOfPerson();
        }
        if (isStringNull(noOfPerson)) {
            noOfPersonsTextInputLayout.setError("Required");
            val = false;
            textOnChangeListenerNoOfPerson();
        }
        return val;
    }

    public void OnPurchaseClick(View view) {
        name = nameTextInputEditText.getText().toString();
        address = addressTextInputEditText.getText().toString();
        email = emailTextInputEditText.getText().toString();
        phone = phoneTextInputEditText.getText().toString();
        zip = zipTextInputEditText.getText().toString();
        state = stateAutoCompleteTextView.getText().toString();
        noOfPerson = noOfPersonsTextInputEditText.getText().toString();

        valid=isFormValid();
        /*Log.e("Check", "OnPurchaseClick: "+valid);
        Log.e("Check", "OnPurchaseClick: "+cardForm.isValid());*/
        if (cardForm.isValid() && valid) {
            String cardHolderName = cardForm.getCardholderName();
            String cardType = cardForm.getCardEditText().getCardType().toString();
            Log.e("Booking Activity", "OnPurchaseClick: " + cardType);
            String cardNumber = cardForm.getCardNumber();
            String expMonth = cardForm.getExpirationMonth();
            String expYear = cardForm.getExpirationYear();
            String exp = expMonth + "/" + expYear;
            /* String cvv = cardForm.getCvv();*/
            startActivity(new Intent(getApplicationContext(), BookingStartTransactionActivity.class)
                    .putExtra("noOfPerson", noOfPerson)
                    .putExtra("Package", getPackagesModal)
                    .putExtra("CardName", cardHolderName)
                    .putExtra("CardNumber", cardNumber)
                    .putExtra("CardType", cardType)
                    .putExtra("CardExp", exp)
                    .putExtra("SaveCard", cardForm.isSaveCardCheckBoxChecked()));

        } else if(!cardForm.isValid()){

            if(cardForm.getCardholderName().isEmpty())
                cardForm.setCardholderNameError("Name Required");
            if(cardForm.getCardNumber().isEmpty())
                cardForm.setCardNumberError("Card Number Required");
            if(cardForm.getExpirationMonth().isEmpty() || cardForm.getExpirationYear().isEmpty())
                cardForm.setExpirationError("Required");
            if(cardForm.getCvv().isEmpty())
                cardForm.setCvvError("Cvv Required");
        }else {
            Toast.makeText(this, "Form Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private void textOnChangeListenerNoOfPerson() {
        noOfPersonsTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noOfPersonsTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerState() {
        stateAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                stateTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerZip() {
        zipTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                zipTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerPhone() {
        phoneTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerEmail() {
        emailTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerAddress() {
        addressTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void textOnChangeListenerName() {
        nameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameTextInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}