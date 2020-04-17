package com.caffeine.dreamlifeassociation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CreatePostActivity extends AppCompatActivity {

    private EditText problem, hospital, location, number;
    private Spinner group;
    private TextView quantity;
    private String Id = "", Name, Gender, Problem, Group, Quantity, Number, Hospital, Location, DATE;
    private ImageView increase, decrease;
    private int count = 1;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
    private DatabaseReference reference;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        gettingLayoutIDs();
        increaseDecrease();
        Button submit = findViewById(R.id.submit_blood_request);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Id = "";
                initialize();
                if (validate()){
                    getAndSendData();
                }
                else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void getAndSendData(){
        String UID = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Users").child(UID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonarDetails details = dataSnapshot.getValue(DonarDetails.class);
                Name = details.getName();
                Gender = details.getGender();

                submitRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void submitRequest(){
        RequestModel m = new RequestModel();

        m.setDate(DATE);
        m.setId(Id);
        m.setGroup(Group);
        m.setHospital(Hospital);
        m.setLocation(Location);
        m.setProblem(Problem);
        m.setQuantity(Quantity);
        m.setNumber("+88" + Number);
        m.setName(Name);
        m.setGender(Gender);

        reference.child(Id).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    progressDialog.dismiss();
                    Toast.makeText(CreatePostActivity.this, "Submission Completed", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreatePostActivity.this, BloodRequestActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
            }
        });
    }

    private void showProgressDialog(){
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
    }

    private void initialize(){
        Problem = problem.getText().toString();
        Group = group.getSelectedItem().toString();
        Hospital = hospital.getText().toString();
        Location = location.getText().toString();
        Number = number.getText().toString();

        Date date = new Date();
        DATE = sdf.format(date);

        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        for (int i=0; i<20; i++){
            Id = Id + alphabet.charAt(r.nextInt(alphabet.length()));
        }
    }

    private boolean validate(){
        boolean v = true;

        if (Problem.isEmpty()){
            problem.setError("Define patient problem");
            problem.requestFocus();
            v = false;
        }

        else if (Group.equals("Patient Blood Group")){
            Toast.makeText(CreatePostActivity.this, "Define a blood group", Toast.LENGTH_SHORT).show();
            v = false;
        }

        else if (Number.isEmpty()){
            number.setError("Define patient's number");
            number.requestFocus();
            v = false;
        }

        else if (Number.length() < 11){
            number.setError("Invalid mobile number");
            number.requestFocus();
            v = false;
        }

        else if (Hospital.isEmpty()){
            hospital.setError("Define hospital name");
            hospital.requestFocus();
            v = false;
        }

        else if (Location.isEmpty()){
            location.setError("Define hospital location");
            location.requestFocus();
            v = false;
        }

        return v;
    }

    private void increaseDecrease(){
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                Quantity = Integer.toString(count);
                quantity.setText(Quantity);
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1){
                    count--;
                    Quantity = Integer.toString(count);
                    quantity.setText(Quantity);
                }
            }
        });
    }

    private void gettingLayoutIDs(){
        problem = findViewById(R.id.patient_problem);
        group = findViewById(R.id.patient_blood_group);
        hospital = findViewById(R.id.hospital);
        location = findViewById(R.id.hospital_address);
        increase = findViewById(R.id.increase);
        decrease = findViewById(R.id.decrease);
        quantity = findViewById(R.id.quantity);
        number = findViewById(R.id.patient_number);
        progressDialog = new Dialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Blood Requests");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreatePostActivity.this, BloodRequestActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
