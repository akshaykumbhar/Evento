package com.evento.evento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Login extends AppCompatActivity {
    Button btnsign,btnlogin;
    FirebaseAuth Auth;
    EditText etEmail,etPassword;
    ProgressDialog prog;
    TextView tvforget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnsign = (Button)findViewById(R.id.btnsignup_login);
        btnlogin = (Button)findViewById(R.id.btnlogin_Login);
        Auth = FirebaseAuth.getInstance();
        prog = new ProgressDialog(this);
        etEmail = (EditText)findViewById(R.id.etEmail_login);
        etPassword = (EditText)findViewById(R.id.etPassword_login);
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ChoiceSign.class));
                finish();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                {
                    etEmail.setError("Invalid Email");
                    etEmail.requestFocus();
                    return;
                }
                prog.setTitle("Login");
                prog.setMessage("Please wait");
                prog.show();
                btnlogin.setClickable(false);
                Auth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                           final FirebaseUser user  =  Auth.getCurrentUser();
                            DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("Student");
                            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for ( DataSnapshot ds : dataSnapshot.getChildren())
                                    {
                                        Student s = ds.getValue(Student.class);
                                        if(s.getEmail().equals(user.getEmail()))
                                        {
                                            prog.cancel();
                                            startActivity(new Intent(Login.this,MainPage.class));
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            dbf = FirebaseDatabase.getInstance().getReference("Institute");
                            dbf.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for ( DataSnapshot ds : dataSnapshot.getChildren())
                                    {
                                        Institute s = ds.getValue(Institute.class);
                                        if(s.getEmail().equals(user.getEmail()))
                                        {
                                            prog.cancel();
                                            startActivity(new Intent(Login.this,IMainPage.class));
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            prog.cancel();
                            btnlogin.setClickable(true);
                            Toast.makeText(Login.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        tvforget = (TextView) findViewById(R.id.tvForget);
        tvforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em  = etEmail.getText().toString().toLowerCase();
                if(em.isEmpty())
                {
                    etEmail.setError("Enter Email");
                    etEmail.requestFocus();
                    return;
                }
                tvforget.setText("Resend Reset Link");
                Auth.sendPasswordResetEmail(em);
                Toast.makeText(Login.this, "Resest Password Email as been send to you", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Login.this,MainPage.class));
        finish();
    }
}
