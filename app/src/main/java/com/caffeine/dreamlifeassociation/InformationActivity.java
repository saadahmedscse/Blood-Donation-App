package com.caffeine.dreamlifeassociation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InformationActivity extends AppCompatActivity {

    private Button submit;
    private EditText name;
    private Spinner blood_group, district;
    private RadioGroup gender;
    private TextView dob, last_dd;

    private String NAME, BLOOD_GROUP, DISTRICT, GENDER, DOB, LAST_DD, NUMBER;
    private static String NAME_PATTERN = "^[a-zA-z ]*$";
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
    private int thisYear, thisDay, thisMonth, dobyear, lastyear, lastday, lastmonth;

    private DatabaseReference reference;
    private DatePickerDialog.OnDateSetListener onDateSetListener, dateSetListener;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        gettingLayoutIDs();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateOfBirth();
            }
        });

        last_dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastDonationDate();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();

                if (validate()){
                    showProgressDialog();
                    sendDataToFirebase();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent(InformationActivity.this, HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    },7500);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        NUMBER = intent.getStringExtra("ss");
    }

    private void sendDataToFirebase(){

        DonarDetails details = new DonarDetails();
        details.setUID(FirebaseAuth.getInstance().getUid());
        details.setNumber(NUMBER);
        details.setName(NAME);
        details.setBloodGroup(BLOOD_GROUP);
        details.setGender(GENDER);
        details.setDistrict(DISTRICT);
        details.setDOB(DOB);
        if (LAST_DD.equals("Last Donation Date (Optional)")){
            details.setLastDonation("Never Donated");
        }
        else {
            details.setLastDonation(LAST_DD);
        }

        String SEARCH_TEXT = DISTRICT + " " + BLOOD_GROUP;
        details.setSearchText(SEARCH_TEXT);

        reference.child(FirebaseAuth.getInstance().getUid()).setValue(details);
    }

    private void setDateOfBirth(){
        Calendar cal = Calendar.getInstance();
        int dob_year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        thisYear = dob_year;
        thisDay = day;
        thisMonth = month;

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String oldDate = dayOfMonth + "/" + month + "/" +year;
                dobyear = year;

                try {
                    SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = sdff.parse(oldDate);
                    String dd = sdf.format(date);
                    dob.setText(dd);
                }
                catch (Exception e){}
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                InformationActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                dob_year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setLastDonationDate(){
        Calendar cal = Calendar.getInstance();
        int last_year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String oldDate = dayOfMonth + "/" + month + "/" +year;
                lastyear = year;
                lastday = dayOfMonth;
                lastmonth = month;

                try {
                    SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = sdff.parse(oldDate);
                    String dd = sdf.format(date);
                    last_dd.setText(dd);
                }
                catch (Exception e){}
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                InformationActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                last_year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void gettingLayoutIDs(){
        submit = findViewById(R.id.submit_btn);
        name = findViewById(R.id.input_name);
        blood_group = findViewById(R.id.blood_spinner);
        gender = findViewById(R.id.gender_group);
        district = findViewById(R.id.district_spinner);
        dob = findViewById(R.id.date_of_birth_picker);
        last_dd = findViewById(R.id.last_donation_date_picker);

        reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Users");
        progressDialog = new Dialog(this);
    }

    private void showProgressDialog(){
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
    }

    private void initialize(){
        NAME = name.getText().toString();
        DOB = dob.getText().toString();
        LAST_DD = last_dd.getText().toString();
        BLOOD_GROUP = blood_group.getSelectedItem().toString();
        DISTRICT = district.getSelectedItem().toString();

        int genID = gender.getCheckedRadioButtonId();
        RadioButton gen = findViewById(genID);
        GENDER = gen.getText().toString();
    }

    private boolean validate(){
        boolean valid = true;

        if (NAME.isEmpty()){
            name.setError("Enter your Fullname");
            name.requestFocus();
            valid = false;
        }

        else if (NAME.length() <3 || NAME.length() > 28){
            name.setError("Enter a valid name");
            name.requestFocus();
            valid = false;
        }

        else if (!NAME.matches(NAME_PATTERN)){
            name.setError("Enter a valid name");
            name.requestFocus();
            valid = false;
        }

        else if (BLOOD_GROUP.equals("Choose your Blood Group")){
            Toast.makeText(InformationActivity.this, "Define your Blood Group", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        else if (DISTRICT.equals("Choose your District")){
            Toast.makeText(InformationActivity.this, "Define your District", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        else if (DOB.equals("Date of Birth")){
            Toast.makeText(InformationActivity.this, "Define your Date of Birth", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        else if (dobyear > thisYear-16){
            Toast.makeText(InformationActivity.this, "You must should be 16", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        else if (!last_dd.equals("Last Donation Date (Optional)")){
            if (lastyear > thisYear){
                Toast.makeText(InformationActivity.this, "Define a valid Donation date", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        return valid;
    }
}
