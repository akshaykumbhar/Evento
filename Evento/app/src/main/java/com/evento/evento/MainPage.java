package com.evento.evento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {
    Button btnsign,btnlogout;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnsign = (Button)findViewById(R.id.MainPage_Signin);
        btnlogout = (Button) findViewById(R.id.btnLogout_MainPage);
        Auth = FirebaseAuth.getInstance();
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this,Login.class));
                finish();
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Auth!= null)
                {
                    Auth.signOut();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
