package com.caffeine.dreamlifeassociation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private static String COUNTRY_CODE = "+88";
    private Button signIn;
    private EditText number;
    private ProgressBar bar;
    private String NUMBER, FINAL_NUMER;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        gettingLayoutIDs();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NUMBER = number.getText().toString();

                if (NUMBER.isEmpty()) {
                    number.setError("Enter your mobile number");
                    number.requestFocus();
                } else if (NUMBER.length() < 11) {
                    number.setError("Enter a valid mobile number");
                    number.requestFocus();
                } else {
                    FINAL_NUMER = COUNTRY_CODE + NUMBER;

                    Intent intent = new Intent(SignInActivity.this, VerificationActivity.class);
                    intent.putExtra("number", FINAL_NUMER);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkingUser();
    }

    private void checkingUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            reference.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String uid = dataSnapshot.child("uid").getValue(String.class);
                    if (uid == null){
                        bar.setVisibility(View.GONE);
                    }
                    else {
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            bar.setVisibility(View.GONE);
        }
    }

    private void gettingLayoutIDs(){
        signIn = findViewById(R.id.sing_in_btn);
        number = findViewById(R.id.input_number);
        bar = findViewById(R.id.signinprogress);

        reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Users");
    }
}
