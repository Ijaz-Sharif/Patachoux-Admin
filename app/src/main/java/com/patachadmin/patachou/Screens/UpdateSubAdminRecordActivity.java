package com.patachadmin.patachou.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.R;

public class UpdateSubAdminRecordActivity extends AppCompatActivity {
    private EditText et_user_name,
            et_city,et_postal_code,et_register_address,et_user_number,et_code;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Button btnRegister;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sub_admin_record);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        et_postal_code=findViewById(R.id.et_postal_code);
        et_city=findViewById(R.id.et_city);
        et_user_number=findViewById(R.id.et_user_number);
        et_register_address=findViewById(R.id.et_register_address);
        et_code=findViewById(R.id.et_code);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        firebaseAuth = FirebaseAuth.getInstance();
        et_user_name = findViewById(R.id.et_user_name);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String name = et_user_name.getText().toString();
                String user_number =et_user_number.getText().toString();
                String register_address =et_register_address.getText().toString();
                if (validate(name,user_number,register_address)) updateRecord();
            }
        });
    }

    @Override
    protected void onStart() {
        loadingDialog.show();
        String id =getIntent().getStringExtra("id");
        databaseReference=  FirebaseDatabase.getInstance().getReference().child("SubAdmin").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                et_city.setText(dataSnapshot.child("City").getValue(String.class));
                et_user_name.setText(dataSnapshot.child("Name").getValue(String.class));
                et_code.setText(dataSnapshot.child("SecretCode").getValue(String.class));
                et_postal_code.setText(dataSnapshot.child("PostalCode").getValue(String.class));
                et_register_address.setText(dataSnapshot.child("Address").getValue(String.class));
                et_user_number.setText(dataSnapshot.child("PhoneNumber").getValue(String.class));
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    private void updateRecord(){
        String id =getIntent().getStringExtra("id");
        try {
            FirebaseDatabase.getInstance().getReference("SubAdmin").child(id).child("Name").setValue(et_user_name.getText().toString());
            FirebaseDatabase.getInstance().getReference("SubAdmin").child(id).child("Address").setValue(et_register_address.getText().toString());
            FirebaseDatabase.getInstance().getReference("SubAdmin").child(id).child("PhoneNumber").setValue(et_user_number.getText().toString());
            FirebaseDatabase.getInstance().getReference("SubAdmin").child(id).child("City").setValue(et_city.getText().toString());
            FirebaseDatabase.getInstance().getReference("SubAdmin").child(id).child("PostalCode").setValue(et_postal_code.getText().toString());
            FirebaseDatabase.getInstance().getReference("SubAdmin").child(id).child("SecretCode").setValue(et_code.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(UpdateSubAdminRecordActivity.this,"Record update successful",Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }














    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String name, String user_number, String register_address) {
        if (register_address.isEmpty()) et_user_name.setError("Enter address!");
        else if (et_city.getText().toString().isEmpty()) et_city.setError("Enter City!");
        else if (et_postal_code.getText().toString().isEmpty()) et_postal_code.setError("Enter Postal Code!");
        else if (user_number.isEmpty()) et_user_name.setError("Enter phone number!");
        else if (name.isEmpty()) et_user_name.setError("Enter name!");
        else return true;
        return false;
    }

    public void finish(View view) {
        finish();
    }
}