package com.patachadmin.patachou.Screens;

import static com.patachadmin.patachou.Utils.Constant.PRODUCTID;
import static com.patachadmin.patachou.Utils.Constant.TYPE;
import static com.patachadmin.patachou.Utils.Constant.USERID;
import static com.patachadmin.patachou.Utils.Constant.getUserId;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patachadmin.patachou.R;

public class AddProductPriceActivity extends AppCompatActivity {
    EditText product_price;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_price);
        product_price=findViewById(R.id.product_price);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void savePrice(View view) {
        if(product_price.getText().toString().trim().isEmpty()){
            product_price.setError("required");
        }
        else {
            loadingDialog.show();
            DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference("User").child(getUserId(AddProductPriceActivity.this)).child(USERID).child("Products").child(TYPE).child(PRODUCTID);

            databaseReference.child("ProductId").setValue(PRODUCTID);
            databaseReference.child("ProductPrice").setValue(product_price.getText().toString());
            loadingDialog.dismiss();
            finish();



        }
    }

    public void finish(View view) {
        finish();
    }
}