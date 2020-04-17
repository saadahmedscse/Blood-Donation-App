package com.caffeine.dreamlifeassociation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caffeine.dreamlifeassociation.Menu.MenuActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout profile, info, blood_requ, search_btn;
    private ImageView search_icon;
    private RecyclerView recyclerView;
    private EditText search_bar;
    private int count = 0;
    private Animation slide_right, slide_left;

    Context context = HomeActivity.this;
    ArrayList<DonarDetails> list;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gettingLayoutIDs();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        blood_requ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BloodRequestActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count){
                    case 0:
                        count++;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                search_icon.setImageResource(R.drawable.close);
                            }
                        }, 600);
                        search_bar.startAnimation(slide_right);
                        search_bar.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        count--;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                search_icon.setImageResource(R.drawable.search);
                            }
                        }, 600);
                        search_bar.startAnimation(slide_left);
                        search_bar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (reference != null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(DonarDetails.class));
                        }

                        RecyclerAdapter adapter = new RecyclerAdapter(list, context);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().toLowerCase();
                filterList(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterList(String text){
        ArrayList<DonarDetails> myList = new ArrayList<>();

        for (DonarDetails object: list){
            if (object.getSearchText().toLowerCase().contains(text)){
                myList.add(object);
            }
        }
        RecyclerAdapter adapter = new RecyclerAdapter(myList, context);
        recyclerView.setAdapter(adapter);
    }

    private void gettingLayoutIDs(){
        slide_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
        slide_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);

        profile = findViewById(R.id.profile_btn);
        info = findViewById(R.id.info_btn);
        blood_requ = findViewById(R.id.post_btn);
        search_icon = findViewById(R.id.search_icon);
        search_btn = findViewById(R.id.search_btn);
        recyclerView = findViewById(R.id.recycler_view);
        search_bar = findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Users");
    }
}
