package com.evento.evento;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class MainPage extends AppCompatActivity {

    FirebaseAuth Auth;
    FirebaseUser user;
    StorageReference sf;
    DatabaseReference dbf;
    Fragment f;
    ImageView btnProfile,btnEvent,btneventsl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        btnEvent = (ImageView) findViewById(R.id.btnEvent);
        btnProfile = (ImageView) findViewById(R.id.btnProfile);
        btneventsl = (ImageView) findViewById(R.id.btnevents);
        final FragmentManager fmr = getFragmentManager();
        FragmentTransaction ft = fmr.beginTransaction();
        if(f!=null) {
            ft.remove(f);
        }
        f = new EventFragment();
        ft.replace(R.id.frag,f);
        ft.addToBackStack(null);
        ft.commit();
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    startActivity(new Intent(MainPage.this,Login.class));
                    finish();
                }
                else
                {
                    FragmentTransaction ft = fmr.beginTransaction();
                    if(f!=null) {
                        ft.remove(f);
                    }
                    f = new StudentProfile();
                    ft.replace(R.id.frag,f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fmr.beginTransaction();
                if(f!=null) {
                    ft.remove(f);
                }
                f = new EventFragment();
                ft.replace(R.id.frag,f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        btneventsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fmr.beginTransaction();
                if(f!=null) {
                    ft.remove(f);
                }
                f = new SeventList();
                ft.replace(R.id.frag,f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
