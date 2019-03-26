package com.evento.evento;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class IMainPage extends AppCompatActivity {
    Fragment f;
    FragmentManager fmr;
    ImageButton ibprofile,ibevent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imain_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ibprofile = (ImageButton)findViewById(R.id.ib_i_profile);
         fmr = getFragmentManager();
        final FragmentTransaction ft = fmr.beginTransaction();
        if(f!=null) {
            ft.remove(f);
        }
        f = new InstituteEvent();
        ft.replace(R.id.imp_frag,f);
        ft.addToBackStack(null);
        ft.commit();
        ibprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fmr.beginTransaction();
                if(f != null)
                {
                 ft.remove(f);
                }

                f = new InstituteProfile();
                ft.replace(R.id.imp_frag,f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        ibevent = (ImageButton) findViewById(R.id.ib_i_event);
        ibevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fmr.beginTransaction();
                if(f!=null) {
                    ft.remove(f);
                }
                f = new InstituteEvent();
                ft.replace(R.id.imp_frag,f);
                ft.addToBackStack(null);
                ft.commit();

            }
        });



    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog a = builder.create();
        a.setTitle("Exit");
        a.show();
    }
}
