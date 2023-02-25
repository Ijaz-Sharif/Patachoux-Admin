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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.Model.Report;
import com.patachadmin.patachou.R;

import java.text.ParseException;
import java.util.ArrayList;
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
    ArrayList<Report> reportArrayList=new ArrayList<Report>();
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
    @RequiresApi(api = Build.VERSION_CODES.N)
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
    public boolean getDifferenceFromStartDate(Date startDate, Date date) {
        //milliseconds
        long different = date.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;

        if(elapsedDays<0){
             return false;

        }
        else {
            return true;
        }
    }
    public boolean getDifferenceFromEndDate(Date endDate, Date date) {
        //milliseconds
        long different = date.getTime() - endDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;

        if(elapsedDays<0){
            return false;

        }
        else {
            return true;
        }
    }

    public boolean addRecord(String reportDate){

        try {
            Date date1 = null;
            Date date2 = null;
            Date date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                date1 = simpleDateFormat.parse(start_date.getText().toString());
                date2 = simpleDateFormat.parse(end_date.getText().toString());
                date = simpleDateFormat.parse(reportDate);
                if(getDifferenceFromStartDate(date1,date)&&getDifferenceFromEndDate(date,date2)){
                    //Toast.makeText(this," greater the start date",Toast.LENGTH_LONG).show();
                    return true;
                }
                else {
                    return false;
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;


    }


    public void getReportsData(){

       DatabaseReference dRef=    FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                .child(getUserId(this)).child("Order");
       reportArrayList.clear();
        loadingDialog.show();

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    if(addRecord(postSnapshot.child("DeliveryOrderDate").getValue(String.class))){
                        if(postSnapshot.child("Status").getValue(String.class).equals("Complete")){

                        reportArrayList.add(new Report(
                                postSnapshot.child("DeliveryOrderDate").getValue(String.class)
                                , postSnapshot.child("TotalPayment").getValue(String.class),
                                postSnapshot.child("DeliveryOrderTime").getValue(String.class)
                               ));

                        }
                    }
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