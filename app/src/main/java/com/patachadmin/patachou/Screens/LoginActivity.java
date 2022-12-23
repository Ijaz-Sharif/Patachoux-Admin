package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.setAdminEmail;
import static com.patachadmin.patachou.Utils.Constant.setAdminLoginStatus;
import static com.patachadmin.patachou.Utils.Constant.setAdminPassword;
import static com.patachadmin.patachou.Utils.Constant.setSuperAdminLoginStatus;
import static com.patachadmin.patachou.Utils.Constant.setUserId;
import static com.patachadmin.patachou.Utils.Constant.setUsername;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.patachadmin.patachou.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginEmail, etLoginPassword;
    DatabaseReference myRef;
    private Dialog loadingDialog;
    private FirebaseAuth firebaseAuth;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        etLoginEmail =findViewById(R.id.et_login_email);
        getDeviceToken();
        etLoginPassword = findViewById(R.id.et_login_password);
        myRef = FirebaseDatabase.getInstance().getReference("SuperAdmin");
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void loginAdmin(View view) {


        if (! etLoginEmail.getText().toString().contains("@")||! etLoginEmail.getText().toString().contains(".")){
            etLoginEmail.setError("Enter valid email!");
        }
        else if (etLoginPassword.getText().toString().isEmpty()) {
            etLoginPassword.setError("Enter password!");
        }
        else if ( etLoginEmail.getText().toString().isEmpty())
        {
            etLoginEmail.setError("Enter email!");

        }
        else {
            requestLogin( etLoginEmail.getText().toString(),  etLoginPassword.getText().toString());
        }
    }
    private void requestLogin(String email, String password) {
        loadingDialog.show();
        myRef.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // get the admin email and password from the firebase
                String dbmail = dataSnapshot.child("AdminEmail").getValue().toString();
                String dbpass = dataSnapshot.child("AdminPassword").getValue().toString();
                // validate the email and password
                if (email.equals(dbmail) && password.equals(dbpass)) {
                    // open the admin dashboard screen
                    setSuperAdminLoginStatus(LoginActivity.this,true);
                    setAdminLoginStatus(LoginActivity.this,false);
                    setAdminEmail(LoginActivity.this,email);
                    setAdminPassword(LoginActivity.this,password);
                    loadingDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), SuperAdminMainActivity.class));
                    finish();

                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                if(loadingDialog.isShowing()){
                                    loadingDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "wrong mail or password" + task.getException(), Toast.LENGTH_LONG).show();
                                }

                            } else if (task.isSuccessful()) {
                                getData();

                            }
                        }
                    });

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getData(){
        final String admin_m=etLoginEmail.getText().toString().trim();
        myRef=  FirebaseDatabase.getInstance().getReference().child("SubAdmin");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(admin_m.equals(dataSnapshot1.child("Mail").getValue(String.class))) {
                        setAdminEmail(LoginActivity.this,admin_m);
                        setUsername(LoginActivity.this,dataSnapshot1.child("Name").getValue(String.class));
                        setUserId(LoginActivity.this,dataSnapshot1.child("UserId").getValue(String.class));
                        setSuperAdminLoginStatus(LoginActivity.this,false);
                        setAdminLoginStatus(LoginActivity.this,true);



                        FirebaseDatabase.getInstance().getReference("SubAdmin").child(dataSnapshot1.child("UserId").getValue(String.class))
                                .child("DeviceToken").setValue(token);

                        startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                        finish();
                        loadingDialog.dismiss();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginActivity.super.onBackPressed();
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
    public void getDeviceToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("message", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token= task.getResult();


                    }
                });
    }
}