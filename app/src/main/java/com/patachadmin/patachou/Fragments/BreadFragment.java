package com.patachadmin.patachou.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.Model.Product;
import com.patachadmin.patachou.R;
import com.patachadmin.patachou.Screens.AddProductActivity;
import com.patachadmin.patachou.Screens.UpdateProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class BreadFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference dRef;
    ArrayList<Product> productArrayList =new ArrayList<Product>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    FloatingActionButton btn_add_product;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bread, container, false);
        /////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        btn_add_product=view.findViewById(R.id.btn_add_product);
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProductActivity.class)
                        .putExtra("type","Bread"));
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        getProductsData();
        super.onStart();
    }

    public void getProductsData(){
        dRef=  FirebaseDatabase.getInstance().getReference("Products").child("Bread");
        loadingDialog.show();
        productArrayList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    productArrayList.add(new Product(
                            postSnapshot.child("ProductId").getValue(String.class),
                            postSnapshot.child("ProductName").getValue(String.class)
                            ,postSnapshot.child("ProductImage").getValue(String.class)
                            ,postSnapshot.child("ProductDescription").getValue(String.class)
                    ));

                }
                arrayAdapter =new ArrayAdapter(productArrayList);
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(ArrayList<Product> itemArrayList1){
            productArrayList =itemArrayList1;
        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getContext()).inflate(R.layout.item_product,parent,false);
            return  new ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {


            Picasso.with(getContext())
                    .load(productArrayList.get(position).getProductPic())
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
            holder.product_name.setText(productArrayList.get(position).getProductName());
            holder.product_detail.setText(productArrayList.get(position).getProductDescription());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final CharSequence[] options = {"Update", "Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Delete")) {

                                dRef=  FirebaseDatabase.getInstance().getReference("Products").child("Bread").child(productArrayList.get(position).getProductId());
                                dRef.removeValue();
                                getProductsData();
                            } else if (options[item].equals("Update")) {

                                startActivity(new Intent(getContext(), UpdateProductActivity.class)
                                        .putExtra("type","Bread")
                                        .putExtra("description",productArrayList.get(position).getProductDescription())
                                        .putExtra("pic",productArrayList.get(position).getProductPic())
                                        .putExtra("name",productArrayList.get(position).getProductName())
                                        .putExtra("id",productArrayList.get(position).getProductId()));



                                dialog.dismiss();


                            } else if (options[item].equals("Cancel")) {
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
            return productArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView product_name,product_detail;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.image);
                product_name=itemView.findViewById(R.id.name);
                cardView=itemView.findViewById(R.id.cardView);
                product_detail=itemView.findViewById(R.id.detail);
            }
        }
    }
}