package com.evento.evento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class InstituteViewCreate extends AppCompatActivity {
    TextView tvname,tvsub,tvseat,tvavail,tvsd,tved,tvprice;
    ImageView ivimg;
    DatabaseReference db;
    Events e ;
    StorageReference sf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_view_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent i = getIntent();
        final String eid = i.getStringExtra("id");
        tvname = (TextView) findViewById(R.id.tv_sed_name);
        tvsub = (TextView) findViewById(R.id.tv_sed_sub);
        tvseat = (TextView) findViewById(R.id.tv_sed_seat);
        tvavail = (TextView) findViewById(R.id.tv_sed_aseat);
        tvsd = (TextView) findViewById(R.id.tv_sed_sd);
        tved = (TextView) findViewById(R.id.tv_sed_ed);
        tvprice = (TextView) findViewById(R.id.tv_sed_price);
        ivimg = (ImageView) findViewById(R.id.iv_sed_img);
        sf = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference("Events");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    e = ds.getValue(Events.class);
                    if(e.getId().equals(eid))
                    {
                        tvname.setText(e.getName());
                        tvsub.setText(e.getSub());
                        tvseat.setText("Total Seats: "+String.valueOf(e.getSeat()));
                        tvavail.setText("Available Seats: "+String.valueOf(e.getAvail()));
                        tvsd.setText("Start Date: "+e.getStartdate());
                        tved.setText("End Date: "+e.getEnddate());
                        tvprice.setText("Price: RS. "+String.valueOf(e.getPrice()));
                        StorageReference ref = sf.child(e.getImgurl().toString());
                        try {
                            final File localFile = File.createTempFile("images", "jpg");
                            ref.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Uri filepath = Uri.fromFile(localFile);
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                                            ivimg.setImageBitmap(bitmap);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
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
