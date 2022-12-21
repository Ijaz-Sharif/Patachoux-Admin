package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.getAdminEmail;
import static com.patachadmin.patachou.Utils.Constant.getAdminPassword;
import static com.patachadmin.patachou.Utils.Constant.setAdminEmail;
import static com.patachadmin.patachou.Utils.Constant.setAdminPassword;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patachadmin.patachou.R;

public class UpdateAdminPasswordActivity extends AppCompatActivity {
    private EditText etLoginEmail, etLoginPassword;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_admin_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        etLoginEmail =findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        etLoginEmail.setText(getAdminEmail(this));
        etLoginPassword.setText(getAdminPassword(this));
    }



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String password) {
        if (email.isEmpty()) etLoginEmail.setError("Enter email!");
        else if (!email.contains("@")||!email.contains(".")) etLoginEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etLoginPassword.setError("Enter password!");
        else if (password.length()<6) etLoginPassword.setError("Password must be at least 6 characters!");
        else return true;
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void updateData(View view) {

//
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();
        requestLogin(email,password);
//        if (validate(email, password)) requestLogin(email, password);
    }
    private void requestLogin(String email, String password) {

           try {
               FirebaseDatabase.getInstance().getReference("SuperAdmin").child("AdminEmail").setValue(etLoginEmail.getText().toString());
               FirebaseDatabase.getInstance().getReference("SuperAdmin").child("AdminPassword").setValue( etLoginPassword.getText().toString());
               setAdminPassword(this,password);
               setAdminEmail(this,email);
               Toast.makeText(UpdateAdminPasswordActivity.this, "Record updated", Toast.LENGTH_SHORT).show();
           }
           catch (Exception e){
               Log.d("error",e.getMessage().toString());
           }




    }

    public void finish(View view) {
        finish();
    }
}