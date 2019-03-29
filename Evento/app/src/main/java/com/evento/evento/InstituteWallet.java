package com.evento.evento;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class InstituteWallet extends AppCompatActivity {
    FirebaseAuth Auth ;
    FirebaseUser user ;
    DatabaseReference dbr;
    Institute i ;
    TextView tvwallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_wallet);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        dbr = FirebaseDatabase.getInstance().getReference("Institute");
        tvwallet = (TextView)findViewById(R.id.tv_iwallet);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    i = ds.getValue(Institute.class);
                    if(i.getEmail().equals(user.getEmail()))
                    {
                        tvwallet.setText("\u20B9 "+String.valueOf(i.getWallet()));
                    }
                }
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
