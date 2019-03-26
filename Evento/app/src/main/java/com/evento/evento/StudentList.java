package com.evento.evento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {
    TextView tv;
    ListView lv;
    static ArrayList<String> name;
    DatabaseReference db;
    Register reg;
    int count= 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tv = (TextView) findViewById(R.id.tv_sl_count);
        lv= ( ListView)findViewById(R.id.lv_sl);
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        db = FirebaseDatabase.getInstance().getReference("Register");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = new ArrayList<String>();
                count = 0 ;
                for ( DataSnapshot ds : dataSnapshot.getChildren())
                {
                    reg =ds.getValue(Register.class);
                    if(reg.getEventid().equals(id))
                    {
                       name.add(reg.getSname()+" || Email id: "+reg.getStudentid());
                        count++;
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(StudentList.this,R.layout.support_simple_spinner_dropdown_item,name);
                lv.setAdapter(adapter);
                tv.setText("Student Count: "+String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
