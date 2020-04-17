package com.caffeine.dreamlifeassociation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, blood_group, district, dob, last_dd, number;
    private DatabaseReference reference;
    RelativeLayout doante, sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gettingLayoutIDs();
        getInformation();

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        doante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
                String last = last_dd.getText().toString();
                DatabaseReference ref = reference.child("lastDonation");
                String today = sdf.format(new Date());

                ref.setValue(today);

                Toast.makeText(ProfileActivity.this, "Donated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInformation(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonarDetails details = dataSnapshot.getValue(DonarDetails.class);
                name.setText(details.getName());
                blood_group.setText(details.getBloodGroup());
                district.setText(details.getDistrict());
                dob.setText(details.getDOB());
                last_dd.setText(details.getLastDonation());
                number.setText(details.getNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gettingLayoutIDs(){
        name = findViewById(R.id.name);
        blood_group = findViewById(R.id.blood_group);
        district = findViewById(R.id.address);
        dob = findViewById(R.id.dob);
        last_dd = findViewById(R.id.last_donation);
        number = findViewById(R.id.number);

        doante = findViewById(R.id.donate);
        sign_out = findViewById(R.id.sign_out);

        String UID = FirebaseAuth.getInstance().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Users").child(UID);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
