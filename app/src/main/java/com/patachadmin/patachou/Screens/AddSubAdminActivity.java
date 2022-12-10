package com.patachadmin.patachou.Screens;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patachadmin.patachou.R;

public class AddSubAdminActivity extends AppCompatActivity {
    private EditText etRegisterEmail,et_user_name, etRegisterPassword, etRegisterConfirmPassword,
            et_city,et_postal_code,et_register_address,et_user_number;
    private FirebaseAuth firebaseAuth;

    DatabaseReference myRef;
    Button btnRegister;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_admin);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        et_postal_code=findViewById(R.id.et_postal_code);
        et_city=findViewById(R.id.et_city);
        et_user_number=findViewById(R.id.et_user_number);
        et_register_address=findViewById(R.id.et_register_address);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        firebaseAuth = FirebaseAuth.getInstance();
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = findViewById(R.id.et_register_confirm_password);
        et_user_name = findViewById(R.id.et_user_name);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etRegisterEmail.getText().toString();
                String name = et_user_name.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String confirm_password = etRegisterConfirmPassword.getText().toString();
                String user_number =et_user_number.getText().toString();
                String register_address =et_register_address.getText().toString();
                if (validate(email,name, password, confirm_password,user_number,register_address)) requestRegister(email, password);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String name, String password, String confirm_password, String user_number, String register_address) {
        if (email.isEmpty()) etRegisterEmail.setError("Enter email!");
        else if (register_address.isEmpty()) et_user_name.setError("Enter address!");
        else if (et_city.getText().toString().isEmpty()) et_city.setError("Enter City!");
        else if (et_postal_code.getText().toString().isEmpty()) et_postal_code.setError("Enter Postal Code!");
        else if (user_number.isEmpty()) et_user_name.setError("Enter phone number!");
        else if (name.isEmpty()) et_user_name.setError("Enter name!");
        else if (!email.contains("@")||!email.contains(".")) etRegisterEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etRegisterPassword.setError("Enter password!");
        else if (password.length()<6) etRegisterPassword.setError("Password must be at least 6 characters!");
        else if (confirm_password.isEmpty()) etRegisterConfirmPassword.setError("Enter password!");
        else if (!password.equals(confirm_password)) etRegisterConfirmPassword.setError("Password not matched!");
        else return true;
        return false;
    }

    private void requestRegister(String email, String password) {
        loadingDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getCreateUserWithEmailOnClickListener(email));
    }
    private OnCompleteListener<AuthResult> getCreateUserWithEmailOnClickListener(String email) {
        return task -> {
            if (task.isSuccessful()) {
                add();
            } else {
                loadingDialog.dismiss();
                Toast.makeText(AddSubAdminActivity.this,"Registration failed!",Toast.LENGTH_LONG).show();

            }
        };
    }

    private void add(){
        try {
            String id = firebaseAuth.getCurrentUser().getUid();
            myRef=  FirebaseDatabase.getInstance().getReference("SubAdmin").child(id);
            myRef.child("Name").setValue(et_user_name.getText().toString());
            myRef.child("UserId").setValue(id);
            myRef.child("Mail").setValue(etRegisterEmail.getText().toString());
            myRef.child("Address").setValue(et_register_address.getText().toString());
            myRef.child("PhoneNumber").setValue(et_user_number.getText().toString());
            myRef.child("City").setValue(et_city.getText().toString());
            myRef.child("PostalCode").setValue(et_postal_code.getText().toString());
            myRef.child("Password").setValue(etRegisterPassword.getText().toString());
            myRef.child("DeviceToken").setValue("empty");
            loadingDialog.dismiss();
            Toast.makeText(AddSubAdminActivity.this,"Registration successful",Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void finish(View view) {
        finish();
    }
}