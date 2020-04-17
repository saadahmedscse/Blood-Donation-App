package com.caffeine.dreamlifeassociation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<DonarDetails> list;
    Context context;

    public RecyclerAdapter(ArrayList<DonarDetails> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donars_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");

        long days = 0;

        String gender = list.get(position).getGender();
        String last = list.get(position).getLastDonation();
        String today = sdf.format(new Date());

        try {
            Date TODAY = sdf.parse(today);
            Date LAST = sdf.parse(last);
            days = ((TODAY.getTime() - LAST.getTime()) / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {}

        if (gender.equals("Male")){
            holder.picture.setImageResource(R.drawable.male);
        }
        else {
            holder.picture.setImageResource(R.drawable.female);
        }

        if (last.equals("Never Donated")){
            holder.availability.setBackgroundResource(R.drawable.green_oval);
        }
        else {
            if (days < 120){
                holder.availability.setBackgroundResource(R.drawable.oval);
            }
            else {
                holder.availability.setBackgroundResource(R.drawable.green_oval);
            }
        }

        holder.name.setText(list.get(position).getName());
        holder.location.setText(list.get(position).getDistrict());
        holder.blood_group.setText(list.get(position).getBloodGroup());
        holder.uid.setText(list.get(position).getUID());
        holder.number.setText(list.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView picture;
        TextView name, location, blood_group, call, message, uid, number;
        LinearLayout availability;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DonarProfileActivity.class);
                    intent.putExtra("uid", uid.getText().toString());
                    context.startActivity(intent);
                }
            });

            picture = itemView.findViewById(R.id.user_picture);
            uid = itemView.findViewById(R.id.user_uid);
            number = itemView.findViewById(R.id.user_number);
            name = itemView.findViewById(R.id.user_name);
            location = itemView.findViewById(R.id.user_location);
            blood_group = itemView.findViewById(R.id.user_blood_group);
            call = itemView.findViewById(R.id.call_donar);
            message = itemView.findViewById(R.id.message_donar);
            availability = itemView.findViewById(R.id.availability);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+number.getText().toString()));
                    context.startActivity(intent);
                }
            });

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentsms = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" +number.getText().toString()));
                    intentsms.putExtra( "sms_body", "I need "+ blood_group.getText().toString() + " " + " blood in " + location.getText().toString());
                    context.startActivity(intentsms);
                }
            });
        }
    }
}
