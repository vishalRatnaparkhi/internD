package com.example.internd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class studentnoti extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;

    final static int PICK_PDF_CODE = 2342;

    //these are the views
    TextView textViewStatus;
    EditText editTextFilename;
    ProgressBar progressBar;

    //the firebase objects for storage and database
    StorageReference mStorageReference,m;
    DatabaseReference mDatabaseReference,database;
    ListView listView;
    Constants con;
    User use;
    //database reference to get uploads data
    String name;
    //list to store uploads data
    List<Upload> uploadList;String[] uploads;
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
        setContentView(R.layout.activity_studentnoti);
        name=getIntent().getStringExtra("name");
        con =new Constants("forstudentupload/", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("student").child(con.DATABASE_PATH_UPLOADS);
        uploadList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listView);


        //adding a clicklistener on listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s=uploads[position];

                Upload upload = uploadList.get(position);

                //Opening the upload file in browser using the upload url

                m = FirebaseStorage.getInstance().getReference(con.STORAGE_PATH_UPLOADS);

                m.child(upload.name+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



            }
        });


        //getting the database reference

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploadList.removeAll(uploadList);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    System.out.println(upload);

                    uploadList.add(upload);
                }

                uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView.setAdapter(adapter);
                //
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("student").child(con.DATABASE_PATH_UPLOADS);
        //getting the views

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //attaching listeners to views
        findViewById(R.id.buttonUploadFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPDF();

            }
        });








        if (getIntent().hasExtra("category")){
            Intent intent = new Intent(studentnoti.this,ReceiveNotificationActivity.class);
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
                title="From "+name+" to Student \n"+title;

                showNotification(title,desc);
            }
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        System.out.println("data  "+data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void uploadFile(final Uri data) {

        String filename = data.getPath().substring(data.getPath().lastIndexOf("/")+1);

        final String file;
        if (filename.indexOf(".") > 0) {
            file = filename.substring(0, filename.lastIndexOf("."));
        } else {
            file =  filename;
        }
        System.out.println(file);
        StorageReference sRef = mStorageReference.child(con.STORAGE_PATH_UPLOADS +file+ ".pdf");

        progressBar.setVisibility(View.VISIBLE);
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        String url =taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                        System.out.println("url "+url);

                        try {


                            final Upload upload = new Upload(file, url,name);


                            mDatabaseReference.child(file).setValue(upload);
                        }catch ( Exception ae){Toast.makeText(getApplicationContext(), ae.getMessage(), Toast.LENGTH_LONG).show();}
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    }
                });


    }







    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }





    public void showNotification(String title,String desc)
    {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"student");
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




    private void createNChannel()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            CharSequence name="My Notification";
            String des="  the noti is..." ;
        int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel nchannel=new NotificationChannel(CHANNEL_ID,name,importance);
            nchannel.setDescription(des);
            NotificationManager nManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nManager.createNotificationChannel(nchannel);

        }
    }
}
