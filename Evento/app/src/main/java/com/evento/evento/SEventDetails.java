package com.evento.evento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Region;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class SEventDetails extends AppCompatActivity {
    TextView tvname,tvsub,tvseat,tvavail,tvsd,tved,tvprice,tvtime;
    ImageView ivimg;
    FirebaseAuth Auth;
    FirebaseUser user ;
    StorageReference sf;
    DatabaseReference db,db1,db2,db3;
    Events e;
    Button btnbook;
    Student s;
    Institute is;
    Register reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sevent_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tvname = (TextView) findViewById(R.id.tv_sed_name);
        tvsub = (TextView) findViewById(R.id.tv_sed_sub);
        tvseat = (TextView) findViewById(R.id.tv_sed_seat);
        tvavail = (TextView) findViewById(R.id.tv_sed_aseat);
        tvtime=(TextView)findViewById(R.id.textView2);
        tvsd = (TextView) findViewById(R.id.tv_sed_sd);
        tved = (TextView) findViewById(R.id.tv_sed_ed);
        tvprice = (TextView) findViewById(R.id.tv_sed_price);
        ivimg = (ImageView) findViewById(R.id.iv_sed_img);
        Intent i = getIntent();
        Auth =FirebaseAuth.getInstance();
        user =Auth.getCurrentUser();
        final String id = i.getStringExtra("id");
        sf = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference("Events");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    e = ds.getValue(Events.class);
                    if(e.getId().equals(id))
                    {
                        tvname.setText(e.getName());
                        tvsub.setText(e.getSub());
                        tvseat.setText("Total Seats: "+String.valueOf(e.getSeat()));
                        tvavail.setText("Available Seats: "+String.valueOf(e.getAvail()));
                        tvsd.setText("Start Date: "+e.getStartdate());
                        tved.setText("End Date: "+e.getEnddate());
                        tvprice.setText("Price: \u20B9 "+String.valueOf(e.getPrice()));
                        tvtime.setText("Time: "+e.getTime());
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
        btnbook = (Button) findViewById(R.id.btn_sed_book);
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.getAvail() == 0)
                {
                    Toast.makeText(SEventDetails.this, "Not seats are available", Toast.LENGTH_SHORT).show();
                    return;
                }
                db =FirebaseDatabase.getInstance().getReference("Register");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            reg = ds.getValue(Register.class);
                            if(reg.getEventid().equals(e.getId()) && reg.getStudentid().equals(user.getEmail()))
                            {
                                Toast.makeText(SEventDetails.this, "Already register", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }

                        db1 = FirebaseDatabase.getInstance().getReference("Student");
                        db1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds: dataSnapshot.getChildren())
                                {
                                    s = ds.getValue(Student.class);
                                    if(s.getEmail().equals(user.getEmail()))
                                    {
                                        if(s.getWallet() < e.getPrice())
                                        {
                                            Toast.makeText(SEventDetails.this, "Not Enough Money in Wallet.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        else
                                        {
                                            Date date = new Date();
                                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                            String strDate= formatter.format(date);
                                            String id[] = strDate.split("/");
                                            String id1 = id[2]+id[1]+id[0]+String.valueOf(((int)(Math.random()*100)+11));
                                            reg = new Register(strDate,id1,e.getId(),user.getEmail(),e.getName()+" : "+e.getSub(),s.getName());
                                            db.child(id1).setValue(reg);
                                            db1.child(s.getUserid()).child("wallet").setValue(s.getWallet()-e.getPrice());
                                            db2 = FirebaseDatabase.getInstance().getReference("Events");
                                            db2.child(e.getId()).child("avail").setValue(e.getAvail()-1);
                                            db3 = FirebaseDatabase.getInstance().getReference("Institute");
                                            db3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot ds : dataSnapshot.getChildren())
                                                    {
                                                        is = ds.getValue(Institute.class);
                                                        if(e.getEmail().equals(is.getEmail()))
                                                        {
                                                            db3.child(is.getUserid()).child("wallet").setValue(is.getWallet() + e.getPrice());
                                                            Toast.makeText(SEventDetails.this, "Event Booked", Toast.LENGTH_SHORT).show();
                                                            btnbook.setText("Booking Done");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





                
                
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
