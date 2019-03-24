package com.evento.evento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventUpdate extends AppCompatActivity {
    EditText et ;
    Button btn ;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);
        et = (EditText) findViewById(R.id.et_eu);
        btn = (Button) findViewById(R.id.btn_eu);
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        final String update = i.getStringExtra("update");
        et.setText(update);
        db = FirebaseDatabase.getInstance().getReference("Events");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et.getText().toString().equals(update))
                {
                    db.child(id).child("update").setValue(et.getText().toString());
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
