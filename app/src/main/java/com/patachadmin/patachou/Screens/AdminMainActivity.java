package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.USERID;
import static com.patachadmin.patachou.Utils.Constant.getUserId;
import static com.patachadmin.patachou.Utils.Constant.setAdminLoginStatus;
import static com.patachadmin.patachou.Utils.Constant.setSuperAdminLoginStatus;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.Model.User;
import com.patachadmin.patachou.R;
import com.patachadmin.patachou.User.UserProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    ArrayList<User> userArrayList =new ArrayList<User>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
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

    @Override
    protected void onStart() {
        getUserData();
        super.onStart();
    }

    public void getUserData(){
        dRef=  FirebaseDatabase.getInstance().getReference("User").child(getUserId(this));
        loadingDialog.show();
        userArrayList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    userArrayList.add(new User(postSnapshot.child("Name").getValue(String.class)
                            ,postSnapshot.child("UserImage").getValue(String.class)
                            ,postSnapshot.child("UserId").getValue(String.class)
                            ,postSnapshot.child("Mail").getValue(String.class)));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logoutUser:
                setAdminLoginStatus(AdminMainActivity.this,false);
                setSuperAdminLoginStatus(AdminMainActivity.this,false);

                startActivity(new Intent(AdminMainActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
//
            case R.id.view_suplier:
                startActivity(new Intent(AdminMainActivity.this, DeliveryBoyActivity.class));
                return true;
//            case R.id.view_order:
//                startActivity(new Intent(AdminMainActivity.this, OrderActivity.class));
              //  return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(AdminMainActivity.this).inflate(R.layout.item_suplier,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {
            Picasso.with(AdminMainActivity.this)
                    .load(userArrayList.get(position).getPic())
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerCrop()
                    .into(holder.image);

            holder.name.setText(userArrayList.get(position).getName());
            holder.email.setText(userArrayList.get(position).getEmail());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final CharSequence[] options = {"Delete","View Products", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Delete")) {
                                dialog.dismiss();
                                FirebaseDatabase.getInstance().getReference("User").child(getUserId(AdminMainActivity.this)).child(userArrayList.get(position).getId());
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                            else if (options[item].equals("View Products")) {
                                USERID =userArrayList.get(position).getId();
                               startActivity(new Intent(AdminMainActivity.this, UserProductActivity.class));
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
            return userArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView name,email;
            CardView cardView;
            ImageView image;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                email=itemView.findViewById(R.id.email);
                name=itemView.findViewById(R.id.name);
                cardView=itemView.findViewById(R.id.cardView);
                image=itemView.findViewById(R.id.image);
            }
        }
    }
    public void addUser(View view) {
        startActivity(new Intent(this,AddUserActivity.class));
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AdminMainActivity.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}