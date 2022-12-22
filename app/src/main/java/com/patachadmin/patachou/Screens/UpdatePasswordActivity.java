package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.getAdminEmail;
import static com.patachadmin.patachou.Utils.Constant.getAdminPassword;
import static com.patachadmin.patachou.Utils.Constant.setAdminEmail;
import static com.patachadmin.patachou.Utils.Constant.setAdminPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patachadmin.patachou.R;

public class UpdatePasswordActivity extends AppCompatActivity {
    private EditText etLoginEmail, etLoginPassword;
    DatabaseReference databaseReference;
    Button btnUpdate;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        etLoginEmail =findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        etLoginEmail.setText(getAdminEmail(this));
        etLoginPassword.setText(getAdminPassword(this));
        btnUpdate=findViewById(R.id.btnUpdate);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();
                setAdminPassword(UpdatePasswordActivity.this,password);
                setAdminEmail(UpdatePasswordActivity.this,email);
                databaseReference=   FirebaseDatabase.getInstance().getReference("SuperAdmin").child("AdminEmail");
                databaseReference.setValue(email);
                databaseReference=   FirebaseDatabase.getInstance().getReference("SuperAdmin").child("AdminPassword");
                databaseReference.setValue(password);
                loadingDialog.dismiss();
                Toast.makeText(UpdatePasswordActivity.this, "Record updated", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
    public void finish(View view) {
        finish();
    }
}