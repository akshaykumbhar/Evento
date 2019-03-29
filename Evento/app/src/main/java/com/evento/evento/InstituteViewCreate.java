package com.evento.evento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.view.View;
import android.widget.Button;
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
    TextView tvname,tvsub,tvseat,tvavail,tvsd,tved,tvprice,tvup,tvupdate,tvtime;
    ImageView ivimg;
    DatabaseReference db;
    Events e ;
    StorageReference sf;
    Button btnedit,btnupdate,btnshow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_view_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent i = getIntent();
        final String eid = i.getStringExtra("id");
        tvname = (TextView) findViewById(R.id.tv_sed_name);
        tvtime = (TextView) findViewById(R.id.textView4);
        tvsub = (TextView) findViewById(R.id.tv_sed_sub);
        tvseat = (TextView) findViewById(R.id.tv_sed_seat);
        tvavail = (TextView) findViewById(R.id.tv_sed_aseat);
        tvsd = (TextView) findViewById(R.id.tv_sed_sd);
        tved = (TextView) findViewById(R.id.tv_sed_ed);
        tvprice = (TextView) findViewById(R.id.tv_sed_price);
        ivimg = (ImageView) findViewById(R.id.iv_sed_img);
        tvup = (TextView) findViewById(R.id.tv_sed_up);
        tvupdate = (TextView) findViewById(R.id.tv_sed_updates);
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
                        tvtime.setText("Time: "+e.getTime());
                        tvprice.setText("Price: RS. "+String.valueOf(e.getPrice()));
                        if(!e.getUpdate().equals(""))
                        {
                            tvup.setVisibility(View.VISIBLE);
                            tvupdate.setVisibility(View.VISIBLE);
                            tvup.setText("Updates");
                            tvupdate.setText(e.getUpdate());
                        }
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
        btnshow = (Button) findViewById(R.id.button6);
        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(InstituteViewCreate.this,StudentList.class);
                i.putExtra("id",e.getId());
                startActivity(i);
            }
        });
        btnupdate =(Button)findViewById(R.id.button5);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstituteViewCreate.this,EventUpdate.class);
                i.putExtra("id",e.getId());
                i.putExtra("update",e.getUpdate());
                startActivity(i);
            }
        });
        btnedit = (Button)findViewById(R.id.button4);
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstituteViewCreate.this,EventEdit.class);
                i.putExtra("id",e.getId());
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
        finish();
    }
}
