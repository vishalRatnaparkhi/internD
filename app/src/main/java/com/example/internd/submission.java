package com.example.internd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class submission extends AppCompatActivity {
    StorageReference mStorageReference,m;
    User use;

    DatabaseReference mDatabaseReference,database;
    ListView listView;
    Constants con;
    //database reference to get uploads data
        String com,username;
    //list to store uploads data
    List<Upload> uploadList;String[] uploads;
    ArrayList arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("data");
        uploadList = new ArrayList<>();
        arrayList=new ArrayList();
        listView = (ListView)findViewById(R.id.listView);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String s) {
                arrayList.removeAll(arrayList);
                uploadList.removeAll(uploadList);
                for (final DataSnapshot postSnapshot : ds.getChildren()) {
                    final Upload upload = postSnapshot.getValue(Upload.class);
                    com=ds.getKey();

                    database=FirebaseDatabase.getInstance().getReference("Users").child("Student");
                    database.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot shot, @Nullable String s) {
                            for(DataSnapshot ps :shot.getChildren()){
                                System.out.println(shot.getKey());
                            if(shot.getKey().compareToIgnoreCase(com)==0) {
                                username = "Vishal";//.getValue(User.class).toString();
                                upload.setUser(username);
                                System.out.println(username);
                            }
                        }}

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //arrayList.add(username);
                    System.out.println(upload);
                    uploadList.add(upload);

                }
                uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    System.out.println("jjjjbb  "+uploadList.get(i).getUser()+"   "+uploadList.get(i).getName());
                    uploads[i]= uploadList.get(i).getUser()+"\n"+uploadList.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads);
                listView.setAdapter(adapter);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        }) ;





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
    }




}
