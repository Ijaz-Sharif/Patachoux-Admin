package com.patachadmin.patachou.Service;

import static com.patachadmin.patachou.Utils.Constant.getUserId;
import static com.patachadmin.patachou.Utils.Constant.getUsername;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patachadmin.patachou.CallBacks.CallListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationService {
    private static final NotificationService instance = new NotificationService();
    public static NotificationService getInstance() {
        return instance;
    }
    String url ="https://fcm.googleapis.com/fcm/send";
    String serverKey="Key=AAAAL0Zv1Xg:APA91bFbcTihrHz5Ozd2dO31KvnF4Yf1cILBxwG8E8RuSdrAi6IQI0gk98VqFmfZyrbRg8McP7Ci5lpHQ00oX7tpMa3QUawtm1eNZK2HUDs-k4Ak86s6JYL7dPqry1IAgkHbwv6PFT2X";



    public ArrayList<String> deviceTokenArrayList = new ArrayList<String>();
    public  String deviceToken;





    public void getDeviceToken(Context c,String userId ,CallListner callListner){
        DatabaseReference myRef1=   FirebaseDatabase.getInstance().getReference("User").child(getUserId(c)).child(userId);
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deviceToken =dataSnapshot.child("DeviceToken").getValue(String.class);
                callListner.callback(true);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }






    public  void completeAdminOrder( final Context context , final CallListner callListner){
        JSONObject jsonObject = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        try {
            jsonObject.put("title","Order Completed");
            jsonObject.put("body",getUsername(context)+" has complete the order");
            jsonObject.put("check","user");
            bodyJson.put("to",deviceToken);
            bodyJson.put("data",jsonObject);
            final String requestBody = bodyJson.toString();

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, bodyJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                     Toast.makeText(context, "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                    callListner.callback(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                      Toast.makeText(context, "error:  " + error.toString(), Toast.LENGTH_SHORT).show();
                    callListner.callback(false);


                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", serverKey);
                    params.put("Content-Type","application/json");
                    return params;
                }
            };


            RequestQueue a = Volley.newRequestQueue(context);

            a.add(jsonOblect);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public  void completeOrder( final Context context , final CallListner callListner){
        JSONObject jsonObject = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        try {
            jsonObject.put("title","Order Completed");
            jsonObject.put("body",getUsername(context)+" has complete the order");
            jsonObject.put("check","user");
            bodyJson.put("to",deviceToken);
            bodyJson.put("data",jsonObject);
            final String requestBody = bodyJson.toString();

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, bodyJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // Toast.makeText(context, "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                    callListner.callback(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //   Toast.makeText(context, "error:  " + error.toString(), Toast.LENGTH_SHORT).show();
                    callListner.callback(false);


                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", serverKey);
                    params.put("Content-Type","application/json");
                    return params;
                }
            };


            RequestQueue a = Volley.newRequestQueue(context);

            a.add(jsonOblect);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



}
