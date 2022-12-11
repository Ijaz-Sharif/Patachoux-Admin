package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.getUserId;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patachadmin.patachou.R;

public class AddDeliveryBoyActivity extends AppCompatActivity {
    private Dialog loadingDialog;
    private EditText etRegisterEmail,et_user_name, etRegisterPassword, etRegisterConfirmPassword,et_number,et_name;
    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_boy);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
        et_name=findViewById(R.id.et_name);
        et_number=findViewById(R.id.et_number);
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void registerSuplier(View view) {
        String email = etRegisterEmail.getText().toString();
        String name = et_user_name.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String confirm_password = etRegisterConfirmPassword.getText().toString();
        if (validate(email,name, password, confirm_password)) requestRegister(email, password);

    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String name, String password, String confirm_password) {
        if (email.isEmpty()) etRegisterEmail.setError("Enter email!");
        else if (name.isEmpty()) et_user_name.setError("Enter Last name!");
        else if (et_number.getText().toString().isEmpty()) et_number.setError("Enter number!");
        else if (et_name.getText().toString().isEmpty()) et_name.setError("Enter First name!");
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
                Toast.makeText(AddDeliveryBoyActivity.this,"registration failed!",Toast.LENGTH_LONG).show();
            }
        };
    }
    private void add(){
        try {
            String id = firebaseAuth.getCurrentUser().getUid();
            myRef=  FirebaseDatabase.getInstance().getReference("Suplier").child(getUserId(AddDeliveryBoyActivity.this)).child(id);
            myRef.child("LastName").setValue(et_user_name.getText().toString());
            myRef.child("Id").setValue(id);
            myRef.child("AdminId").setValue(getUserId(AddDeliveryBoyActivity.this));
            myRef.child("Mail").setValue(etRegisterEmail.getText().toString());
            myRef.child("Number").setValue(et_number.getText().toString());
            myRef.child("FirstName").setValue(et_name.getText().toString());
            myRef.child("DeviceToken").setValue("empty");
            myRef.child("Password").setValue(etRegisterPassword.getText().toString());
            loadingDialog.dismiss();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void finish(View view) {
        finish();
    }
}