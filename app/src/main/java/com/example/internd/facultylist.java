package com.example.internd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class facultylist extends AppCompatActivity {
    DatabaseReference dref;
    ListView listview;
    String s,gr;
    ArrayList<String> arrayList=new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facultylist);
        dref=FirebaseDatabase.getInstance().getReference("Users").child("Faculty");
        listview=(ListView)findViewById(R.id.listview);
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,arrayList);
        listview.setAdapter(arrayAdapter );
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println(ds.getValue());
                    User user = ds.getValue(User.class);
                    s=user.toString();
                    gr=user.grnumber;
                    arrayList.add(user.toString());
                    //arrayList.add(ds.getValue().toString());
                    //  Log.d("TAG", commandObject.toString());
                    arrayAdapter.notifyDataSetChanged();

                    //Toast.makeText(studentlist.this,commandObject.toString(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                                            Intent i = new Intent(facultylist.this, viewfaculty.class);
                                            i.putExtra("user",s);
                                            i.putExtra("gr",gr);
                                            // <-- Assumed you image is Parcelable
                                            startActivity(i);
                                        }
                                    });




       /* dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println(ds.toString());
                    String user = ds.getValue().toString();
                    arrayList.add(user);
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
        });*/


    }
}
