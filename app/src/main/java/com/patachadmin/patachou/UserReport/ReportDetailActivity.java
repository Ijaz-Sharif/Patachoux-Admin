package com.patachadmin.patachou.UserReport;

import static com.patachadmin.patachou.UserReport.ReportActivity.reportArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patachadmin.patachou.Order.OrderSubActivity;
import com.patachadmin.patachou.R;

public class ReportDetailActivity extends AppCompatActivity {
     RecyclerView recylerview;
     ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repoert_detail);

        recylerview=findViewById(R.id.recylerview);
        recylerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        arrayAdapter=new ArrayAdapter();
        recylerview.setAdapter(arrayAdapter);

    }
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(ReportDetailActivity.this).inflate(R.layout.item_report,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.report_date.setText("Date : "+reportArrayList.get(position).getOrderDate());
            holder.total_payment.setText("$ "+reportArrayList.get(position).getOrderPayment());
            holder.report_time.setText("Time : "+reportArrayList.get(position).getOrderTime());
        }

        @Override
        public int getItemCount() {
            return reportArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView report_date,total_payment,report_time;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                report_date=itemView.findViewById(R.id.report_date);
                cardView=itemView.findViewById(R.id.cardView);
                total_payment=itemView.findViewById(R.id.total_payment);
                report_time=itemView.findViewById(R.id.report_time);
            }
        }
    }
    public void finish(View view) {
        finish();
    }
}