package com.example.internd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class removeuser extends AppCompatActivity {
    Button delete;
    EditText search_bar;
    DatabaseReference dref;

    String username,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removeuser);

        delete=(Button)findViewById(R.id.delete);

        search_bar=(EditText) findViewById(R.id.search_bar);

        dref= FirebaseDatabase.getInstance().getReference("Users");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(removeuser.this);
                builder.setTitle("Confirm Remove Action...").
                        setMessage("You sure, that you want to Remove User?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final String searchText = search_bar.getText().toString();

                                //Start child search
                                dref.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        for(DataSnapshot ds : dataSnapshot.getChildren()) {

                                            String user = ds.child("grnumber").getValue().toString();

                                            if(searchText.compareToIgnoreCase(user)==0) {
                                              String  uid = ds.getKey();
                                                        username=ds.child("gmail").getValue().toString();
                                                        password=  ds.child("password").getValue().toString();
                                                        System.out.println(username+"   "+password);

                                                        delete(username,password);



                                            }


                                            //arrayList.add(ds.getValue().toString());
                                            //  Log.d("TAG", commandObject.toString());


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

    public void delete(String username,String password)
    {
        try {


            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            AuthCredential credential = EmailAuthProvider.getCredential(username, password);
            System.out.println("credentials  "+credential.toString());

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Remove User", "User account deleted.");
                                            }
                                            else
                                                Log.d("Remove User", "ERROR!.");

                                        }
                                    });

                        }
                    });




        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
