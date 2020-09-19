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

public class studentlist extends AppCompatActivity {
    DatabaseReference dref;
    ListView listview;
    ArrayList<String> arrayList=new ArrayList<>();
    String s,gr,uid,name;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlist);
        name=getIntent().getStringExtra("name");
        dref=FirebaseDatabase.getInstance().getReference("Users").child("Student");
        listview=(ListView)findViewById(R.id.listview);
        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,arrayList);
        listview.setAdapter(arrayAdapter );
       dref.addValueEventListener(new ValueEventListener() {
            @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println(ds.getValue());
                    User user = ds.getValue(User.class);
                    uid=ds.getKey();
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


                Intent i = new Intent(studentlist.this, viewstudent.class);
                i.putExtra("user",s);
                i.putExtra("gr",gr);
                i.putExtra("uid",uid);
                i.putExtra("name",name);
                // <-- Assumed you image is Parcelable
                startActivity(i);
            }
        });



    }



}
