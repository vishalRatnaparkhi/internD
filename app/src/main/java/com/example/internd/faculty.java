package com.example.internd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class faculty extends AppCompatActivity {




    Button sign,logout,search,studentlist,facultylist,remove,snoti,submission,anoti;
    private EditText mSearchField;
    DatabaseReference dref;
    ListView listview;
    ArrayList<String> arrayList=new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    ProgressDialog dialog;


    private RecyclerView mResultList;


    private DatabaseReference mUserDatabase;
    EditText search_bar;
    String sa,uid,gr,roll;
    TextView faculty;
    String name;
     String searchText,searchuser;


    StorageReference m1,mStorageReference2,m2;
    DatabaseReference mDatabaseReference1,mDatabaseReference2;
    ListView listView1;
    Constants con;

    Button faculty1;
    TextView data,interd,fac;
    List<Upload> uploadList1,uploadList2;String[] uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        dialog = new ProgressDialog(this);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        sign = (Button) findViewById(R.id.sign);
        snoti=(Button)findViewById(R.id.snoti);
        anoti=(Button)findViewById(R.id.anoti);
        submission=(Button)findViewById(R.id.submission);

        studentlist=(Button)findViewById(R.id.students);
        faculty1=(Button)findViewById(R.id.faculty1);
        search=(Button)findViewById(R.id.search);
        faculty=(TextView)findViewById(R.id.faculty);
        logout = (Button) findViewById(R.id.logout);
        search_bar=(EditText)findViewById(R.id.search_bar);

        if(getIntent().hasExtra("gr"))
            FirebaseMessaging.getInstance().subscribeToTopic(getIntent().getStringExtra("gr"));
        FirebaseMessaging.getInstance().subscribeToTopic("faculty");
        final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("student");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
        FirebaseDatabase.getInstance().getReference("Users").
                addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Faculty").child(uid).getValue(User.class)!=null) {
                    User user = dataSnapshot.child("Faculty").child(uid).getValue(User.class);
                    name=user.fname+" "+user.lname;
                    faculty.setText(user.toString());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(faculty.this);
                builder.setTitle("Confirm Logout Action...").
                        setMessage("You sure, that you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                faculty.super.finish();
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


        studentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(faculty.this,studentlist.class);
                in.putExtra("name",name);
                startActivity(in);
            }
        });

        snoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(faculty.this,studentnoti.class);
                in.putExtra("name",name);
                startActivity(in);

            }
        });



        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(faculty.this,submission.class);
                startActivity(in);
            }
        });
        anoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(faculty.this,adminnoti.class);
                in.putExtra("name",name);
                startActivity(in);

            }
        });

        dref=FirebaseDatabase.getInstance().getReference("Users");
        listView1 = (ListView)findViewById(R.id.listView1);
        listview=(ListView)findViewById(R.id.listview);
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,arrayList);
        listview.setAdapter(arrayAdapter );

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listview.setVisibility(View.VISIBLE);
                listview.setBackgroundColor(Color.WHITE);
                searchText = search_bar.getText().toString();

                //Start child search
                dref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            if(ds.child("grnumber").getValue()!=null){
                            String user = ds.child("grnumber").getValue().toString();
                            System.out.println(user);
                            if (searchText.compareToIgnoreCase(user) == 0) {
                                 searchuser = ds.getKey();
                                sa = ds.getValue(User.class).toString();
                                gr = ds.getValue(User.class).grnumber;
                                roll=ds.child("roll").getValue().toString();
                                arrayList.add(ds.getValue(User.class).toString() + "       \t" + ds.child("roll").getValue().toString());
                                System.out.println("    " + dref.child(searchuser).getKey().toString());
                            }
                        }


                            //arrayList.add(ds.getValue().toString());
                            //  Log.d("TAG", commandObject.toString());
                            arrayAdapter.notifyDataSetChanged();

                            //Toast.makeText(studentlist.this,commandObject.toString(),Toast.LENGTH_SHORT).show();
                        }

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
                });


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Intent i;
                        if(roll.compareToIgnoreCase("Student")==0)
                            i = new Intent(faculty.this, viewstudent.class);
                        else
                            i=new Intent(faculty.this, viewfaculty.class);
                        i.putExtra("user",sa);
                        i.putExtra("gr",gr);
                        i.putExtra("uid",searchuser);
                        i.putExtra("name",name);
                        // <-- Assumed you image is Parcelable
                        startActivity(i);
                    }
                });

                arrayList.removeAll(arrayList);
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                method();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(faculty.this);
                builder.setTitle("Confirm Logout Action...").
                        setMessage("You sure, that you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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


        fac=(TextView)findViewById(R.id.fac);
        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference("student");
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("student");
        uploadList1 = new ArrayList<>();
        uploadList2 = new ArrayList<>();



        faculty1.setOnClickListener(new View.OnClickListener() {
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


    public void method() {
        // Opening new user registration activity using intent on button click.
        Intent intent = new Intent(faculty.this, addnew.class);
        startActivity(intent);
    }
}






































    // View Holder Class











