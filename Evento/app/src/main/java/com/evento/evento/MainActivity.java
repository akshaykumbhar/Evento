package com.evento.evento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth Auth;
    FirebaseUser user;
    DatabaseReference db;
    Student s;
    Institute is;
    Boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        if (user != null) {
            db = FirebaseDatabase.getInstance().getReference("Student");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        s = ds.getValue(Student.class);
                        if(s.getEmail().equals(user.getEmail()))
                        {
                            if(flag) {
                                flag=false;
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(getApplicationContext(), MainPage.class));
                                        finish();
                                    }
                                }, 5000);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            db = FirebaseDatabase.getInstance().getReference("Institute");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        is = ds.getValue(Institute.class);
                        if(is.getEmail().equals(user.getEmail()))
                        {
                            if(flag)
                            {
                                flag=false;
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(getApplicationContext(), IMainPage.class));
                                        finish();
                                    }
                                }, 5000);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    flag = false;
                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                    finish();
                }
            }, 5000);
        }
    }
}
