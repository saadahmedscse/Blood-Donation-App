package com.caffeine.dreamlifeassociation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    ArrayList<RequestModel> list;

    public PostAdapter(ArrayList<RequestModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
        String today = sdf.format(new Date());
        String postdate = list.get(position).getDate();
        long days = 0;

        try {
            Date TODAY = sdf.parse(today);
            Date POST = sdf.parse(postdate);

            days = ((TODAY.getTime() - POST.getTime()) / (1000 * 60 * 60 * 24));
        }
        catch (Exception e){}

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dream Life Association").child("Blood Requests").child(list.get(position).getId());
        if (days > 1){
            reference.removeValue();
        }

        String blood = "Blood Group " + list.get(position).getGroup();
        String gender = list.get(position).getGender();
        String Quantity = "Need " + list.get(position).getQuantity() + " bag blood";

        if (gender.equals("Male")){
            holder.gender.setImageResource(R.drawable.male);
        }
        else {
            holder.gender.setImageResource(R.drawable.female);
        }

        holder.problem.setText(list.get(position).getProblem());
        holder.group.setText(blood);
        holder.quantity.setText(Quantity);
        holder.number.setText(list.get(position).getNumber());
        holder.name.setText(list.get(position).getName());
        holder.date.setText(postdate);
        holder.hospital.setText(list.get(position).getHospital());
        holder.location.setText(list.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView gender;
        TextView name, problem, group, quantity, number, date, hospital, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.poster_name);
            gender = itemView.findViewById(R.id.post_gender);
            problem = itemView.findViewById(R.id.post_patient_problem);
            group = itemView.findViewById(R.id.post_patient_group);
            quantity = itemView.findViewById(R.id.post_quantity);
            number = itemView.findViewById(R.id.post_patient_number);
            date = itemView.findViewById(R.id.post_date);
            hospital = itemView.findViewById(R.id.post_patient_hospital);
            location = itemView.findViewById(R.id.post_patient_location);
        }
    }
}
