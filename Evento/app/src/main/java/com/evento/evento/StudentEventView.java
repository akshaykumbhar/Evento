package com.evento.evento;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView tvname,tvsub,tvup,tvupdate,tvsd,tved,tvtime;
    Button btnsetreminder;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tvname = (TextView) findViewById(R.id.tv_sed_name);
        tvsub = (TextView) findViewById(R.id.tv_sed_sub);
        tvsd = (TextView) findViewById(R.id.tv_sed_sd);
        tved = (TextView) findViewById(R.id.tv_sed_ed);
        ivimg = (ImageView) findViewById(R.id.iv_sed_img);
        tvup = (TextView) findViewById(R.id.tv_updates);
        tvtime = (TextView)findViewById(R.id.textView3);
        tvupdate = (TextView) findViewById(R.id.tv_update);
        btnsetreminder = (Button) findViewById(R.id.button3);
        btnsetreminder.setText("Set reminder one day before Event");
        final Intent in = new Intent(StudentEventView.this,SetReminder.class);
        final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        sf = FirebaseStorage.getInstance().getReference();
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        db = FirebaseDatabase.getInstance().getReference("Events");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        tvtime.setText("Time: "+e.getTime());
                        in.putExtra("Name",e.getName());
                        in.putExtra("Date",e.getStartdate());
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
                btnsetreminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PendingIntent pi  = PendingIntent.getBroadcast(StudentEventView.this,1234,in,0);
                        long time = System.currentTimeMillis() + 10000;
                        am.set(AlarmManager.RTC_WAKEUP,time,pi);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
