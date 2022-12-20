package com.patachadmin.patachou.Order;

import static com.patachadmin.patachou.Order.OrderActivity.OrderList;
import static com.patachadmin.patachou.Utils.Constant.getUserId;
import static com.patachadmin.patachou.Utils.Constant.getUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.CallBacks.CallListner;
import com.patachadmin.patachou.Model.Cart;
import com.patachadmin.patachou.R;
import com.patachadmin.patachou.Service.NotificationService;

import java.util.ArrayList;

public class OrderSubActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    ArrayList<Cart> orderList =new ArrayList<Cart>();
    DatabaseReference databaseReference;
    Button btn_order_status;
    TextView user_name,user_address,user_number,time,date;
    int index=0;
    String dial;
    boolean notificationStatus=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sub);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        user_number=findViewById(R.id.user_number);
        user_address=findViewById(R.id.user_address);
        user_name=findViewById(R.id.user_name);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date);
        /////loading dialog
        loadingDialog=new Dialog(this);
        btn_order_status=findViewById(R.id.btn_order_status);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        user_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

    }

    private void makePhoneCall(){
        if(ContextCompat.checkSelfPermission(OrderSubActivity.this,
                android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(OrderSubActivity.this,
                    new String[]{  android.Manifest.permission.CALL_PHONE},1);
        }
        else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==requestCode){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
        }
    }
    @Override
    protected void onStart() {
        index=getIntent().getIntExtra("index",-1);
        user_address.setText("Address : "+OrderList.get(index).getUserAddress());
        user_number.setText("Number : "+OrderList.get(index).getUserNumber());
        user_name.setText("Name : "+OrderList.get(index).getUserName());
        date.setText("Delivery Date : "+OrderList.get(index).getDeliveryDate());
        time.setText("Delivery Time : "+OrderList.get(index).getDeliveryTime());
        dial="tel:" +OrderList.get(index).getUserNumber();
        if(OrderList.get(index).getStatus().equals("InProgress")){
            btn_order_status.setText("Complete Order");
        }
        if(OrderList.get(index).getStatus().equals("Complete")){
            btn_order_status.setVisibility(View.GONE);
        }

        getProductsData();
        super.onStart();
    }


    public void getProductsData(){
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                .child(getUserId(this)).child("Order")
                .child(OrderList.get(index).getOrderId()).child("OrderItems");
        loadingDialog.show();
        orderList.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    orderList.add(new Cart(
                            postSnapshot.child("ProductName").getValue(String.class)
                            ,postSnapshot.child("ProductPrice").getValue(String.class)
                            ,postSnapshot.child("ProductQuantity").getValue(String.class)
                            ,postSnapshot.child("ProductImage").getValue(String.class)
                            ,postSnapshot.child("ProductId").getValue(String.class)));

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

    public void updateOrderStatus(View view) {
                loadingDialog.show();
        databaseReference =FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                .child(getUserId(this)).child("Order").child(OrderList.get(index).getOrderId());



        if(btn_order_status.getText().toString().equalsIgnoreCase("Complete Order")){

            DatabaseReference databaseReference=   FirebaseDatabase.getInstance().getReference().child("User").child(getUserId(OrderSubActivity.this))
                    .child(OrderList.get(index).getUserId());



            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // get the admin email and password from the firebase
                    String code = dataSnapshot.child("SecretCode").getValue().toString();

                    if(code.equals("not available")){

                        completeOrder();

                    }
                    else {
                        showAlert(code);
                    }
                    // validate the email and password



                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }
        else {
            databaseReference.child("Status").setValue("InProgress");
            databaseReference.child("SuplierName").setValue(getUsername(OrderSubActivity.this));
            loadingDialog.dismiss();
            Toast.makeText(OrderSubActivity.this,"order started ",Toast.LENGTH_LONG).show();
            finish();
        }
    }



    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(OrderSubActivity.this).inflate(R.layout.item_slip,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.product_name.setText(orderList.get(position).getProductName());
            holder.price.setText("$ "+orderList.get(position).getProductPrice());
            holder.product_quantity.setText("Quantity :"+orderList.get(position).getProductQuantity());
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView product_name,product_quantity,price;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                product_name=itemView.findViewById(R.id.product_name);
                cardView=itemView.findViewById(R.id.cardView);
                product_quantity=itemView.findViewById(R.id.product_quantity);
                price=itemView.findViewById(R.id.product_price);
            }
        }
    }



    public void showAlert(String code){
        AlertDialog.Builder boite;
        boite = new AlertDialog.Builder(OrderSubActivity.this);
        boite.setTitle("Code Confirmation");


        final EditText input = new EditText(OrderSubActivity.this);
        boite.setView(input);

        boite.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(input.getText().toString().equals(code)){
                    if(!notificationStatus){
                        completeOrder();
                    }



                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(OrderSubActivity.this, "wrong code", Toast.LENGTH_SHORT).show();
                    input.setError("wrong code");
                }
                //whatever action
            }
        });
        boite.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //whatever action
                dialogInterface.dismiss();
            }
        });
        boite.show();
    }




    public void completeOrder(){
        notificationStatus=true;
        NotificationService.getInstance().getDeviceToken(OrderSubActivity.this,OrderList.get(index).getUserId() , new CallListner() {
            @Override
            public void callback(boolean status) {

                if(status){
                    NotificationService.getInstance().completeOrder(OrderSubActivity.this, new CallListner() {
                        @Override
                        public void callback(boolean status) {
                            if(status){
                                databaseReference.child("Status").setValue("Complete");
                                databaseReference.child("SuplierName").setValue(getUsername(OrderSubActivity.this));

                                Toast.makeText(OrderSubActivity.this,"order completed ",Toast.LENGTH_LONG).show();
                            }

                            loadingDialog.dismiss();
                            finish();
                        }
                    });
                }

            }
        });
    }






    public void finish(View view) {
        finish();
    }
}