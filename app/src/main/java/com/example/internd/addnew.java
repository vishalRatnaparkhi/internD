package com.example.internd;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class addnew extends AppCompatActivity implements View.OnClickListener{

    EditText fname;
    EditText mname;
    EditText lname;
    EditText email;
    EditText user;
    EditText pass;
    EditText grno;
    String fnam,mnam,lnam,emai,grnumber,pas,roll;
    int uid=1;
    ProgressBar pbar;
    Spinner spinner;

    Button mRegisterbtn;
    TextView mLoginPageBack;
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;

    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew);
        fname = (EditText) findViewById(R.id.fname);
        mname = (EditText) findViewById(R.id.mname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);

        pass = (EditText) findViewById(R.id.pass);
        grno = (EditText) findViewById(R.id.grno);
        pbar=(ProgressBar) findViewById(R.id.pbar);
        spinner=(Spinner)findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected Class name from the list
                roll = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                roll="Student";
            }
        });



        mRegisterbtn = (Button) findViewById(R.id.sign);

        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mRegisterbtn.setOnClickListener(this);
        //mLoginPageBack.setOnClickListener(this);
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    public void onClick(View v) {
        if (v==mRegisterbtn){
            UserRegister();
        }else if (v== mLoginPageBack){
            startActivity(new Intent(addnew.this,MainActivity.class));
        }
    }

    private void UserRegister() {
         fnam = fname.getText().toString().trim();
         mnam = mname.getText().toString().trim();
         lnam = lname.getText().toString().trim();
         emai = email.getText().toString().trim();
         grnumber= grno.getText().toString().trim();

         pas = pass.getText().toString().trim();


        if (TextUtils.isEmpty(fnam)){
            Toast.makeText(addnew.this, "Enter First Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(emai)){
            Toast.makeText(addnew.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(pas)){
            Toast.makeText(addnew.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (pas.length()<6){
            Toast.makeText(addnew.this,"Password must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(mnam)){
            Toast.makeText(addnew.this, "Enter Middle Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(lnam)){
            Toast.makeText(addnew.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(grnumber)){
            Toast.makeText(addnew.this, "Enter GR Number", Toast.LENGTH_SHORT).show();
            return;
        }


        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(emai,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth(task.getResult().getUser());
                    mAuth.signOut();
                    mDialog.setMessage("User Created...!");
                    mDialog.setCanceledOnTouchOutside(true);
                    mDialog.show();
                    mDialog.dismiss();

                }else{
                    Toast.makeText(addnew.this,"error on creating user"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        });
    }
    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(addnew.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser( user.getUid(),roll);
    }

    private void createAnewUser(String uid,String roll) {
        User user = BuildNewuser();
        mdatabase.child(roll).child(uid).setValue(user);
    }


    private User BuildNewuser(){
        return new User(fnam,mnam,lnam,emai,grnumber,pas,roll, new Date().getTime());
    }



}

