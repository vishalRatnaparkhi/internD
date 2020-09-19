package com.example.internd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class student extends AppCompatActivity {
    StorageReference mStorageReference1,m1,mStorageReference2,m2;
    DatabaseReference mDatabaseReference1,mDatabaseReference2;
    ListView listView1,listView2;
    Constants con;
    String name;
    Button mysubmission, fnoti, anoti, intern,logout,faculty,admin;
    TextView data,interd,fac,ad;
    List<Upload> uploadList1,uploadList2;String[] uploads;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        FirebaseMessaging.getInstance().subscribeToTopic("student");
        if (getIntent().hasExtra("gr"))
            FirebaseMessaging.getInstance().subscribeToTopic(getIntent().getStringExtra("gr"));
        FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("faculty");
        mysubmission = (Button) findViewById(R.id.mysubmission);
        fnoti = (Button) findViewById(R.id.fnoti);
        logout=(Button)findViewById(R.id.logout);
        anoti = (Button) findViewById(R.id.anoti);

        faculty = (Button) findViewById(R.id.faculty);
        intern = (Button) findViewById(R.id.intern);
        data=(TextView)findViewById(R.id.data);
        interd=(TextView)findViewById(R.id.interd);
        fac=(TextView)findViewById(R.id.fac);
        listView1 = (ListView)findViewById(R.id.listView1);


        final String uid  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String gr = getIntent().getStringExtra("gr");


        FirebaseDatabase.getInstance().getReference("Users").
                addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("Student").child(uid).getValue(User.class)!=null) {
                User user = dataSnapshot.child("Student").child(uid).getValue(User.class);
                System.out.println("USER "+user);
                if(user!=null)
                                data.setText(user.toString());
                                name=user.fname+" "+user.lname;
                   }
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(student.this);
                builder.setTitle("Confirm Logout Action...").
                        setMessage("You sure, that you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                student.super.finish();
                                Intent i = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });


        anoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(student.this, adminnoti.class);
                intent.putExtra("name",name);
                startActivity(intent);


            }
        });
        fnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(student.this, facultynoti.class);
                intent.putExtra("name",name);
                startActivity(intent);

            }
        });
        intern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(student.this, intern.class);
                intent.putExtra("uid",uid);
                intent.putExtra("gr",gr);
                startActivity(intent);

            }
        });
        mysubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(student.this, mysubmission.class);
                intent.putExtra("uid",uid);
                intent.putExtra("gr",gr);
                intent.putExtra("name",name);

                startActivity(intent);

            }
        });
        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference("student");
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("student");
        uploadList1 = new ArrayList<>();
        uploadList2 = new ArrayList<>();



        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView1.setVisibility(View.VISIBLE);

                fac.setVisibility(View.VISIBLE);


                mDatabaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            String key = child.getKey();
                            fun(key);


                        }uploadList1.removeAll(uploadList1);


                            uploadList1.addAll(uploadList2);
                        uploadList2.removeAll(uploadList2);
                        uploads = new String[uploadList1.size()];

                        for (int i = 0; i < uploads.length; i++) {
                            uploads[i] =uploadList1.get(i).getUser()+" \n"+ uploadList1.get(i).getName();

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                        listView1.setAdapter(adapter);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseEr) {

                    }
                });

            }
        });






        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Upload upload = uploadList1.get(position);

                //Opening the upload file in browser using the upload url

                m1 = FirebaseStorage.getInstance().getReference("forstudentupload/");

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


        //adding a clicklistener on listview






















    }
    public void fun(String key){

        mDatabaseReference2.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Upload user = child.getValue(Upload.class);
                    System.out.println(user);
                    uploadList2.add(user);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }







    }