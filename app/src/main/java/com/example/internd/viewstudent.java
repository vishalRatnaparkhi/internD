package com.example.internd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import static com.example.internd.mysubmission.PICK_PDF_CODE;

public class viewstudent extends AppCompatActivity {
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    ProgressBar progressBar;

    //the firebase objects for storage and database


    //list to store uploads data
    List<Upload> uploadList1,uploadList2;String[] uploads;

    TextView view,interd,other,student;
    EditText title,data;
    Button send,receive ,upload;
    StorageReference mStorageReference1,m1,mStorageReference2,m2;
    DatabaseReference mDatabaseReference1,mDatabaseReference2;
    ListView listView1,listView2;
    Constants con;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_viewstudent);
        view=(TextView)findViewById(R.id.view);
        interd=(TextView)findViewById(R.id.interd);
        title=(EditText)findViewById(R.id.title);
        data=(EditText)findViewById(R.id.data);
       student= (TextView)findViewById(R.id.student);
        other=(TextView)findViewById(R.id.other);

        receive=(Button)findViewById(R.id.receive);
        upload=(Button)findViewById(R.id.upload);
        send=(Button)findViewById(R.id.send);
        listView1 = (ListView)findViewById(R.id.listView1);
        listView2 = (ListView)findViewById(R.id.listView2);

        name=getIntent().getStringExtra("name");
        String s= getIntent().getStringExtra("user");
        final String gr=getIntent().getStringExtra("gr");
        final String uid= getIntent().getStringExtra("uid");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView1.setVisibility(View.GONE);
                listView2.setVisibility(View.VISIBLE);
                student.setVisibility(View.GONE);
                other.setVisibility(View.VISIBLE);

            }
        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView2.setVisibility(View.GONE);
               other.setVisibility(View.GONE);
               student.setVisibility(View.VISIBLE);
                listView1.setVisibility(View.VISIBLE);

            }
        });






        con =new Constants("forstudentupload/", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mStorageReference2 = FirebaseStorage.getInstance().getReference();
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("student").child(con.DATABASE_PATH_UPLOADS);
        uploadList2 = new ArrayList<>();
        //getting the database reference
        //retrieving upload data from firebase database
        mDatabaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploadList2.removeAll(uploadList2);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    System.out.println(upload);

                    uploadList2.add(upload);
                }

                uploads = new String[uploadList2.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i]= uploadList2.get(i).getUser()+"\n"+uploadList2.get(i).getName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView2.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //adding a clicklistener on listview
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Upload upload = uploadList2.get(position);

                //Opening the upload file in browser using the upload url

                m2 = FirebaseStorage.getInstance().getReference("forstudentupload/");

                m2.child(upload.name+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        System.out.println("fffffffffffffffffffffffffffffffffffff");

                    }
                });



            }
        });




        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("student").child(con.DATABASE_PATH_UPLOADS);
        //getting the views

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //attaching listeners to views
        findViewById(R.id.buttonUploadFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPDF();

            }
        });










        ///////end of upload view of files


        System.out.println(uid);
        view.setText(s);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t=title.getText().toString();
                String d=data.getText().toString();
                showNotification(t,d,gr);

            }
        });


        FirebaseDatabase.getInstance().getReference("Users").
                addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("company").child(uid).getValue(Company.class)!=null){
                            Company c = dataSnapshot.child("company").child(uid).getValue(Company.class);
                            interd.setText(c.toString());


                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });



        mStorageReference1 = FirebaseStorage.getInstance().getReference();
        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference("data").child(uid);
        uploadList1 = new ArrayList<>();
        //getting the database reference
        //retrieving upload data from firebase database
        mDatabaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploadList1.removeAll(uploadList1);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    System.out.println(upload);

                    uploadList1.add(upload);
                }

                uploads = new String[uploadList1.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList1.get(i).getName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView1.setAdapter(adapter);
                //
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Upload upload = uploadList1.get(position);

                //Opening the upload file in browser using the upload url

                m1 = FirebaseStorage.getInstance().getReference("upload/");

                m1.child(upload.name+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        StorageReference sRef = mStorageReference2.child(con.STORAGE_PATH_UPLOADS +file+ ".pdf");

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


                            mDatabaseReference2.child(file).setValue(upload);
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
