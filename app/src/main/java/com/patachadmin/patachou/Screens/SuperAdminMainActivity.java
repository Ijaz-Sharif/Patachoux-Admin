package com.patachadmin.patachou.Screens;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.MainActivity;
import com.patachadmin.patachou.Model.Admin;
import com.patachadmin.patachou.R;

import java.util.ArrayList;

public class SuperAdminMainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    ArrayList<Admin> adminArrayList =new ArrayList<Admin>();
    ArrayAdapter arrayAdapter;

    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_main);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logoutUser:
                setAdminLoginStatus(SuperAdminMainActivity.this,false);
                setSuperAdminLoginStatus(SuperAdminMainActivity.this,false);
                startActivity(new Intent(SuperAdminMainActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
                return true;
            case R.id.setting_account:
                startActivity(new Intent(SuperAdminMainActivity.this, UpdateAdminPasswordActivity.class));
                return true;
            case R.id.view_products:
                startActivity(new Intent(SuperAdminMainActivity.this, MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {

        getAdminData();
        super.onStart();
    }
    public void getAdminData(){
        dRef=  FirebaseDatabase.getInstance().getReference("SubAdmin");
        loadingDialog.show();
        adminArrayList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    adminArrayList.add(new Admin(postSnapshot.child("UserId").getValue(String.class),
                            postSnapshot.child("Name").getValue(String.class)
                            ,postSnapshot.child("Mail").getValue(String.class)
                            , postSnapshot.child("Password").getValue(String.class)));

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

    public void addAdmin(View view) {
        startActivity(new Intent(this,AddSubAdminActivity.class));
    }

    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(SuperAdminMainActivity.this).inflate(R.layout.item_admin,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.name.setText(adminArrayList.get(position).getAdminName());
            holder.email.setText(adminArrayList.get(position).getAdminEmail());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final CharSequence[] options = {"Delete","Update", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuperAdminMainActivity.this);
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
                            else if (options[item].equals("Update")) {
                                startActivity(new Intent(SuperAdminMainActivity.this,UpdateSubAdminRecordActivity.class)
                                        .putExtra("id",adminArrayList.get(position).getAdminId()));
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
            return adminArrayList.size();
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
        loadingDialog.show();

        dRef=  FirebaseDatabase.getInstance().getReference("SubAdmin").child(adminArrayList.get(position).getAdminId());
        dRef.removeValue();
        DatabaseReference databaseReference;
        databaseReference=  FirebaseDatabase.getInstance().getReference("Suplier").child(adminArrayList.get(position).getAdminId());
        databaseReference.removeValue();

        FirebaseDatabase.getInstance().getReference("User").child(adminArrayList.get(position).getAdminId()).removeValue();
        getAdminData();
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SuperAdminMainActivity.super.onBackPressed();
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