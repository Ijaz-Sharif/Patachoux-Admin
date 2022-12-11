package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.getUserId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.Model.DeliveryBoy;
import com.patachadmin.patachou.R;

import java.util.ArrayList;

public class DeliveryBoyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    ArrayList<DeliveryBoy> suplierArrayList =new ArrayList<DeliveryBoy>();
    ArrayAdapter arrayAdapter;

    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
    public void addSpulier(View view) {
        startActivity(new Intent(this,AddDeliveryBoyActivity.class));
    }
    @Override
    protected void onStart() {

        getSuplierData();
        super.onStart();
    }
    public void getSuplierData(){
        dRef=  FirebaseDatabase.getInstance().getReference("Suplier").child(getUserId(this));
        loadingDialog.show();
        suplierArrayList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    suplierArrayList.add(new DeliveryBoy(postSnapshot.child("LastName").getValue(String.class)
                            ,postSnapshot.child("Mail").getValue(String.class)
                            ,postSnapshot.child("FirstName").getValue(String.class)
                            ,postSnapshot.child("Number").getValue(String.class)
                            ,postSnapshot.child("Id").getValue(String.class)));

                }
                arrayAdapter =new ArrayAdapter();
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void finish(View view) {
        finish();
    }

    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(DeliveryBoyActivity.this).inflate(R.layout.item_suplier,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.name.setText(suplierArrayList.get(position).getFirstName() + " "+suplierArrayList.get(position).getLastName());
            holder.email.setText(suplierArrayList.get(position).getEmail());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final CharSequence[] options = {"Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryBoyActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Delete")) {
                                dialog.dismiss();
                                deleteAccount(position);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return suplierArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView name,email;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                email=itemView.findViewById(R.id.email);
                name=itemView.findViewById(R.id.name);
                cardView=itemView.findViewById(R.id.cardView);
            }
        }
    }

    public void deleteAccount(int position){
        dRef=  FirebaseDatabase.getInstance().getReference("Suplier").child(getUserId(DeliveryBoyActivity.this)).child(suplierArrayList.get(position).getId());
        dRef.removeValue();
        getSuplierData();
    }


}