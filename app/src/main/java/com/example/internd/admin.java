package com.example.internd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class admin extends AppCompatActivity {

    Button sign,logout,search,studentlist,facultylist,remove,snoti,fnoti,submission;
    private EditText mSearchField;
    DatabaseReference dref;
    ListView listview;
    ArrayList<String> arrayList=new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    ProgressDialog dialog;


    private RecyclerView mResultList;


    private DatabaseReference mUserDatabase;
    EditText search_bar;
    String sa,uid,gr,name,searchuser,searchText,roll;
    TextView admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);





        dialog = new ProgressDialog(this);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users");
        sign = (Button) findViewById(R.id.sign);
        snoti=(Button)findViewById(R.id.snoti);
        submission=(Button)findViewById(R.id.submission);
        fnoti=(Button)findViewById(R.id.fnoti);

        studentlist=(Button)findViewById(R.id.students);
        search=(Button)findViewById(R.id.search);
        facultylist=(Button)findViewById(R.id.faculty);
        logout = (Button) findViewById(R.id.logout);
        search_bar=(EditText)findViewById(R.id.search_bar);
        admin=(TextView)findViewById(R.id.admin);
        FirebaseMessaging.getInstance().subscribeToTopic("admin");
        if(getIntent().hasExtra("gr"))
            FirebaseMessaging.getInstance().subscribeToTopic(getIntent().getStringExtra("gr"));
        FirebaseMessaging.getInstance().unsubscribeFromTopic("student");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("faculty");

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Users").
                addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Admin").child(uid).getValue(User.class)!=null) {
                            User user = dataSnapshot.child("Admin").child(uid).getValue(User.class);
                            name=user.fname+" "+user.lname;
                            admin.setText(user.toString());
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });



        studentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(admin.this,studentlist.class);
                in.putExtra("name",name);
                startActivity(in);
            }
        });

        snoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(admin.this,studentnoti.class);
                in.putExtra("name",name);
                startActivity(in);

            }
        });

        fnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(admin.this,facultynoti.class);
                in.putExtra("name",name);
                startActivity(in);

            }
        });
        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(admin.this,submission.class);
                startActivity(in);
            }
        });

        facultylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(admin.this,facultylist.class);
                startActivity(in);
            }
        });





        dref=FirebaseDatabase.getInstance().getReference("Users");
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
                            if(ds.child("grnumber").getValue()!=null) {
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
                //End Child Seacrh


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        listview.setVisibility(View.GONE);

                        Intent i;
                        if(roll.compareToIgnoreCase("Student")==0)
                            i= new Intent(admin.this, viewstudent.class);
                        else
                            i= new Intent(admin.this, viewfaculty.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(admin.this);
                builder.setTitle("Confirm Logout Action...").
                        setMessage("You sure, that you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                admin.super.finish();
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


    }





    // View Holder Class









    public void method() {
        // Opening new user registration activity using intent on button click.
        Intent intent = new Intent(admin.this, addnew.class);
        startActivity(intent);
    }
}
