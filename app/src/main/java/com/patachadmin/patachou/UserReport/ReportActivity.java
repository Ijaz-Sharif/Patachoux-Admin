package com.patachadmin.patachou.UserReport;

import static com.patachadmin.patachou.Utils.Constant.getUserId;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.Model.Order;
import com.patachadmin.patachou.Order.OrderActivity;
import com.patachadmin.patachou.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {
        String userId;
        EditText start_date,end_date;
    private Dialog loadingDialog;
    SimpleDateFormat simpleDateFormat;
    final Calendar myCalendar= Calendar.getInstance();
    DatePickerDialog datePicker;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
       // userId=getIntent().getStringExtra("userId");
        end_date=findViewById(R.id.end_date);
        start_date=findViewById(R.id.start_date);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
      simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");


        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd/M/yyyy";
                java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat(myFormat, Locale.US);
                start_date.setText(dateFormat.format(myCalendar.getTime()));
            }
        };

        start_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                datePicker =  new DatePickerDialog(ReportActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                //datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            }
        });
        DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd/M/yyyy";
                java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat(myFormat, Locale.US);
                end_date.setText(dateFormat.format(myCalendar.getTime()));
            }
        };

        end_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                datePicker =  new DatePickerDialog(ReportActivity.this,date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
               // datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void searchRecord(View view) {
        if(start_date.getText().toString().isEmpty()){
            start_date.setError("required");
        }else if(end_date.getText().toString().isEmpty()){
            end_date.setError("required");
        }
        else {

                    try {
                        Date date1 = null;
                        Date date2 = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                            date1 = simpleDateFormat.parse(start_date.getText().toString());
                            date2 = simpleDateFormat.parse(end_date.getText().toString());
                            getDifference(date1, date2);
                        }


        } catch (ParseException e) {
            e.printStackTrace();
      }

        }


    }
    public void getDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;

        if(elapsedDays<0){
            Toast.makeText(this,"start date in not greater then end date ",Toast.LENGTH_LONG).show();
        }else if(elapsedDays==0){
            Toast.makeText(this,"start date and end date should be different ",Toast.LENGTH_LONG).show();

        }
        else {
            getReportsData();
        }
    }
    public void getReportDateDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;

        if(elapsedDays<0){
            Toast.makeText(this,"start date in not greater then end date ",Toast.LENGTH_LONG).show();
        }else if(elapsedDays==0){
            Toast.makeText(this,"start date and end date should be different ",Toast.LENGTH_LONG).show();

        }
    }

    public void getReportsData(){

       DatabaseReference dRef=    FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                .child(getUserId(this)).child("Order");
        loadingDialog.show();

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

//                    OrderList.add(new Order(postSnapshot.child("OrderId").getValue(String.class),
//                            postSnapshot.child("Date").getValue(String.class)
//                            ,postSnapshot.child("Status").getValue(String.class) ,
//                            postSnapshot.child("SuplierName").getValue(String.class)
//                            , postSnapshot.child("Name").getValue(String.class),
//                            postSnapshot.child("UserAddress").getValue(String.class)
//                            , postSnapshot.child("UserNumber").getValue(String.class)
//                            ,postSnapshot.child("UserId").getValue(String.class)
//                            ,postSnapshot.child("DeliveryOrderTime").getValue(String.class)
//                            ,postSnapshot.child("DeliveryOrderDate").getValue(String.class)));
                }

                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    public void finish(View view) {
        finish();
    }


}