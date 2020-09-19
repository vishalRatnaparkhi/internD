package com.example.internd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;




import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class adminnoti extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    Button send;
    NotificationCompat.Builder mBuilder;
    NotificationManager nManager;
    PendingIntent mResult;
    TaskStackBuilder mTaskBuilder;
    Intent mResultIntent;
    private
    EditText t,d;
    String title,desc;
    static String CHANNEL_ID="channel_id01"
            ;    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminnoti);
       final  String name=getIntent().getStringExtra("name");
        if (getIntent().hasExtra("category")){
            Intent intent = new Intent(adminnoti.this,ReceiveNotificationActivity.class);

            intent.putExtra(title,getIntent().getStringExtra(title));
            intent.putExtra(desc,getIntent().getStringExtra(desc));
            startActivity(intent);
        }
        mRequestQue = Volley.newRequestQueue(this);








        send=(Button)findViewById(R.id.send);
        t=(EditText)findViewById(R.id.title);
        d=(EditText)findViewById(R.id.desc);
       /* mBuilder=new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Message from Admin");
        mBuilder.setContentText("message is");
        mResultIntent=new Intent(this,studentnoti.class);
        mTaskBuilder=TaskStackBuilder.create(this);
        mTaskBuilder.addParentStack(studentnoti.this);
        mTaskBuilder.addNextIntent(mResultIntent);
        mResult=mTaskBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(mResult);
        nManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);*/


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=t.getText().toString();
                desc=d.getText().toString();
                title="From "+name+" to Admin\n "+title;

                showNotification(title,desc);
            }
        });




    }
    public void showNotification(String title,String desc)
    {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"admin");
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



        /*createNChannel();
        Intent mainIn=new Intent(this,addnew.class);
        mainIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pend=PendingIntent.getActivity(this,0,mainIn,PendingIntent.FLAG_ONE_SHOT);

        createNChannel();
        Intent openIn=new Intent(this,studentlist.class);
        openIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent open=PendingIntent.getActivity(this,0,openIn,PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle("Message from Admin");
        builder.setContentText("message is");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setAutoCancel(true);
        builder.setContentIntent(pend);

        builder.addAction(R.drawable.ic_open,"Open",open);
        NotificationManagerCompat nManager=NotificationManagerCompat.from(this);
        nManager.notify(NOTIFICATION_ID,builder.build());*/





}
