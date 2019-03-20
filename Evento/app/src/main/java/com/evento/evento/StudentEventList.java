package com.evento.evento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentEventList extends AppCompatActivity {
    ListView lv;
    FirebaseAuth Auth;
    FirebaseUser user ;
    DatabaseReference db,db1;
    ArrayList<String> events;
    Register reg;
    Events es;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event_list);
        lv = (ListView)findViewById(R.id.lv_sel);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();

        db = FirebaseDatabase.getInstance().getReference("Register");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events = new ArrayList<String>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    reg = ds.getValue(Register.class);
                    if(reg.getStudentid().equals(user.getEmail()))
                    {
                        events.add(reg.getEname());
                    }

                }
                ArrayAdapter ad = new ArrayAdapter(StudentEventList.this,R.layout.support_simple_spinner_dropdown_item,events);
                lv.setAdapter(ad);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
        finish();
    }
}
