package com.example.internd;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class mysubmission extends AppCompatActivity implements View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysubmission);

         con =new Constants("upload/",FirebaseAuth.getInstance().getCurrentUser().getUid());
        mStorageReference = FirebaseStorage.getInstance().getReference();
        name=getIntent().getStringExtra("name");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("data").child(con.DATABASE_PATH_UPLOADS);
        uploadList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listView);


        //adding a clicklistener on listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                String s=uploads[position];

                                                Upload upload = uploadList.get(position);

                                                //Opening the upload file in browser using the upload url

                                                m = FirebaseStorage.getInstance().getReference("upload/");

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


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("data").child(con.DATABASE_PATH_UPLOADS);
        //getting the views

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //attaching listeners to views
        findViewById(R.id.buttonUploadFile).setOnClickListener(this);

    }

    //this function will get the pdf from the storage



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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonUploadFile:
                getPDF();
                break;

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



}
