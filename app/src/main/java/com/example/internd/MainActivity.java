package com.example.internd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;


public class MainActivity extends AppCompatActivity {
    EditText Email, Password;
    String uid;
    Button LogInButton;
    TextView tv;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String email, password;
    DatabaseReference mdatabase;

    ProgressDialog dialog;

    String s="vishal";

    public static final String TAG="LOGIN";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogInButton = (Button) findViewById(R.id.login);


       //

        Email = (EditText) findViewById(R.id.user);
        Password = (EditText) findViewById(R.id.pass);
        tv=(TextView)findViewById(R.id.tv);
        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    Intent intent = new Intent(MainActivity.this, addnew.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Log.d(TAG,"AuthStateChanged:Logout");
                }

            }
        };
        // LogInButton.setOnClickListener((View.OnClickListener) this);
        //RegisterButton.setOnClickListener((View.OnClickListener) this);
        //Adding click listener to log in button.
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling EditText is empty or no method.
                userSign();


            }
        });

        // Adding click listener to register button.



    }

    @Override
    protected void onStart() {
        super.onStart();
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }

    @Override
    public void onBackPressed() {
        MainActivity.super.finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);

        startActivity(intent);

    }



    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Loging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();

                    Toast.makeText(MainActivity.this, "Login not successfull", Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();

                    checkIfEmailVerified();

                }
            }
        });

    }
    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified() {
        final FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        } else {
            Email.getText().clear();
            users.getEmail();
            Password.getText().clear();
            Intent intent = new Intent(MainActivity.this, addnew.class);
            uid = users.getUid();
            System.out.println("Uid is " + uid);
//reading data from datababe



            FirebaseDatabase database;
            DatabaseReference myRef;

            myRef = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference order = myRef.child("Users");
                    order.getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot :dataSnapshot.getChildren()) {
                        String roll="";String gr="";
                        if(postSnapshot.child(uid).child("roll").getValue()!=null) {
                            roll = postSnapshot.child(uid).child("roll").getValue().toString();
                             gr = postSnapshot.child(uid).getValue(User.class).grnumber;
                        }
                        System.out.println(roll);
                        if(roll.compareToIgnoreCase("Student")==0)
                            method1(uid,gr);
                        if(roll.compareToIgnoreCase("Faculty")==0)
                            method2(uid,gr);
                        if(roll.compareToIgnoreCase("Admin")==0)
                            method3(uid,gr);


                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //DatabaseReference myRef = database.getReference();
            ;
                   /* if(s.compareToIgnoreCase("Student")==0)
                        method1(uid);
                    else
                    if(s.compareToIgnoreCase("Faculty")==0)
                        method2(uid);
                    else
                        method3(uid);*/





        }


    }
    public void method1(String uid,String gr)
    {
        Intent intent = new Intent(MainActivity.this, student.class);
        intent.putExtra("UID",uid);

        intent.putExtra("gr",gr);
        startActivity(intent);

    }
    public void method2(String uid,String gr)
    {
        Intent intent = new Intent(MainActivity.this, faculty.class);
        intent.putExtra("gr",gr);
        intent.putExtra("UID",uid);
        startActivity(intent);

    }
    public void method3(String uid,String gr)
    {
        Intent intent = new Intent(MainActivity.this, admin.class);
        intent.putExtra("gr",gr);
        intent.putExtra("UID",uid);
        startActivity(intent);

    }

}