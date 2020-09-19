package com.example.internd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class intern extends AppCompatActivity {
    EditText cname,sdate,edate,advisor,caddress,iprofile;
    Button add;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern);

        String gr=getIntent().getStringExtra("gr");
        cname=(EditText)findViewById(R.id.cname);
        sdate=(EditText)findViewById(R.id.sdate);
        edate=(EditText)findViewById(R.id.edate);
        advisor=(EditText)findViewById(R.id.advisor);
        caddress=(EditText)findViewById(R.id.caddress);
        iprofile=(EditText)findViewById(R.id.iprofile);
        add=(Button)findViewById(R.id.add);
       final  String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Users").
                addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("company").child(uid).getValue(Company.class)!=null) {
                          Company c=  dataSnapshot.child("company").child(uid).getValue(Company.class);
                            cname.setText(c.cname);
                            sdate.setText(c.sdate);
                            edate.setText(c.edate);
                            advisor.setText(c.advisor);
                            iprofile.setText(c.iprofile);
                            caddress.setText(c.caddress);


                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Company c=new Company(cname.getText().toString(),edate.getText().toString(),
                        sdate.getText().toString(),caddress.getText().toString(),advisor.getText().toString(),
                        iprofile.getText().toString());
                mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                mdatabase.child("company").child(uid).setValue(c);
                Intent intent = new Intent(intern.this, student.class);
                startActivity(intent);
            }
        });

    }
}
