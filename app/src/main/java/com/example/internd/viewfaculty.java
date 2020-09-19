package com.example.internd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class viewfaculty extends AppCompatActivity {
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    TextView view;
    EditText title,data;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_viewfaculty);
        view=(TextView)findViewById(R.id.view);
        title=(EditText)findViewById(R.id.title);
        data=(EditText)findViewById(R.id.data);
        send=(Button)findViewById(R.id.send);
        String s= getIntent().getStringExtra("user");
        final String gr=getIntent().getStringExtra("gr");
        view.setText(s);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t=title.getText().toString();
                String d=data.getText().toString();
                showNotification(t,d,gr);

            }
        });





    }
    public void showNotification(String title,String desc,String gr)
    {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+gr);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",title);
            notificationObj.put("body",desc);

            JSONObject extraData = new JSONObject();
            extraData.put("Topic",title);
            extraData.put("Announcement",desc);



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAmLb2CMk:APA91bGX-dqjTe8CUFVfNBPl3kc2VBsbYaanm6Wc3PHGQtOXjGJP_H4xvk6LMhs9hePtlrjfHFqxztBP9M8I1EmvuY4D0w_uYd8g3mZMR_EpDs4XYw6oCK6EwCI61tgZ2RB77FfX_-y6");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }




}
