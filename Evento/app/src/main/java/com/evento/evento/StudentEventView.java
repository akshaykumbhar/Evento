package com.evento.evento;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class StudentEventView extends AppCompatActivity {
    TextView tvname,tvsub,tvup,tvupdate,tvsd,tved;
    ImageView ivimg;
    FirebaseAuth Auth;
    FirebaseUser user ;
    StorageReference sf;
    DatabaseReference db,db1,db2,db3;
    Events e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event_view);
        tvname = (TextView) findViewById(R.id.tv_sed_name);
        tvsub = (TextView) findViewById(R.id.tv_sed_sub);
        tvsd = (TextView) findViewById(R.id.tv_sed_sd);
        tved = (TextView) findViewById(R.id.tv_sed_ed);
        ivimg = (ImageView) findViewById(R.id.iv_sed_img);
        tvup = (TextView) findViewById(R.id.tv_updates);
        tvupdate = (TextView) findViewById(R.id.tv_update);
        sf = FirebaseStorage.getInstance().getReference();
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        db = FirebaseDatabase.getInstance().getReference("Events");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    e = ds .getValue(Events.class);
                    if(e.getId().equals(id))
                    {
                        tvname.setText(e.getName());
                        tvsd.setText("Start Date: "+e.getStartdate());
                        tved.setText("End Date: "+e.getEnddate());
                        tvsub.setText(e.getSub());
                        if(!e.getUpdate().equals(""))
                        {
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
    }
}
