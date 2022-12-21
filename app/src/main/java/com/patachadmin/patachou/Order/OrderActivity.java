package com.patachadmin.patachou.Order;

import static com.patachadmin.patachou.Utils.Constant.getUserId;
import static com.patachadmin.patachou.Utils.Constant.getUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.Model.Order;
import com.patachadmin.patachou.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    public static ArrayList<Order> OrderList =new ArrayList<Order>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
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
    public void onStart() {
        getProductsData();
        super.onStart();
    }
    public void getProductsData(){

        dRef=    FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                .child(getUserId(this)).child("Order");
        loadingDialog.show();
        OrderList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    OrderList.add(new Order(postSnapshot.child("OrderId").getValue(String.class),
                            postSnapshot.child("Date").getValue(String.class)
                            ,postSnapshot.child("Status").getValue(String.class) ,
                            postSnapshot.child("SuplierName").getValue(String.class)
                            , postSnapshot.child("Name").getValue(String.class), postSnapshot.child("UserAddress").getValue(String.class)
                            , postSnapshot.child("UserNumber").getValue(String.class)
                            ,postSnapshot.child("UserId").getValue(String.class)
                            ,postSnapshot.child("DeliveryOrderTime").getValue(String.class)
                            ,postSnapshot.child("DeliveryOrderDate").getValue(String.class)));
                }
                arrayAdapter=new ArrayAdapter();
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(OrderActivity.this).inflate(R.layout.item_order_splier,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.order_id.setText(OrderList.get(position).getOrderId());
            holder.order_date.setText(OrderList.get(position).getDate());
            holder.order_accepter.setText(OrderList.get(position).getSuplierName());
            if(OrderList.get(position).getStatus().equals("Start")){
                holder.image_status.setImageDrawable(getResources().getDrawable(R.drawable.green_image));
            }
            else if(OrderList.get(position).getStatus().equals("InProgress")){
                holder.image_status.setImageDrawable(getResources().getDrawable(R.drawable.yellow_image));
            }
            else if(OrderList.get(position).getStatus().equals("Complete")){
                holder.image_status.setImageDrawable(getResources().getDrawable(R.drawable.red_image));
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(OrderList.get(position).getStatus().equals("InProgress")||OrderList.get(position).getStatus().equals("Complete")){
                        if(getUsername(OrderActivity.this).equals(OrderList.get(position).getSuplierName())){
                            startActivity(new Intent(OrderActivity.this, OrderSubActivity.class)
                                    .putExtra("index",position));
                        }
                    }
                    else {
                        startActivity(new Intent(OrderActivity.this, OrderSubActivity.class)
                                .putExtra("index",position));
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return OrderList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            TextView order_id,order_date,order_accepter;
            ImageView image_status;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                image_status=itemView.findViewById(R.id.image_status);
                order_id=itemView.findViewById(R.id.order_id);
                order_date=itemView.findViewById(R.id.order_date);
                cardView=itemView.findViewById(R.id.cardView);
                order_accepter=itemView.findViewById(R.id.order_accepter);
            }
        }
    }









    public void finish(View view) {
        finish();
    }
}