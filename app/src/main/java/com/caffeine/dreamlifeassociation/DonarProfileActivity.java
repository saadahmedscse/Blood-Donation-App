package com.caffeine.dreamlifeassociation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonarProfileActivity extends AppCompatActivity {

    private String UID;
    private TextView name, blood_group, location, dob, last_dd, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_profile);

        name = findViewById(R.id.donar_name);
        blood_group = findViewById(R.id.donar_blood_group);
        location = findViewById(R.id.donar_address);
        dob = findViewById(R.id.donar_dob);
        last_dd = findViewById(R.id.donar_last_donation);
        number = findViewById(R.id.donar_number);

        Button call = findViewById(R.id.donar_call_btn);
        Button msg = findViewById(R.id.donar_message_btn);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number.getText().toString()));
                startActivity(intent);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" +number.getText().toString()));
                intentsms.putExtra( "sms_body", "I need "+ blood_group.getText().toString() + " " + " blood in " + location.getText().toString());
                startActivity(intentsms);
            }
        });

        Intent intent = getIntent();
        UID = intent.getStringExtra("uid");
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Users").child(UID);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DonarDetails details = dataSnapshot.getValue(DonarDetails.class);

                        name.setText(details.getName());
                        name.setText(details.getName());
                        blood_group.setText(details.getBloodGroup());
                        location.setText(details.getDistrict());
                        dob.setText(details.getDOB());
                        last_dd.setText(details.getLastDonation());
                        number.setText(details.getNumber());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 500);
    }
}
