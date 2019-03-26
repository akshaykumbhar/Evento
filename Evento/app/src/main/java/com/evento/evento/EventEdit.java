package com.evento.evento;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EventEdit extends AppCompatActivity {
    EditText etname,etsub,etseat,etstartdate,etenddate,etaddress,etprice;
    Button btncreate,btngallery,btntakephoto;
    ImageView ivprofile;
    Events e;
    Uri filepath;
    DatabaseReference db;
    StorageReference sf;
    FirebaseAuth Auth;
    FirebaseUser user;
    ProgressDialog prog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etname = (EditText) findViewById(R.id.et_ce_name);
        prog = new ProgressDialog(this);
        etsub = (EditText) findViewById(R.id.et_ce_sub);
        etseat = (EditText) findViewById(R.id.et_ce_seat);
        etstartdate = (EditText) findViewById(R.id.et_ce_sd);
        etenddate = (EditText) findViewById(R.id.et_ce_ed);
        etaddress = (EditText) findViewById(R.id.et_ce_address);
        btncreate = (Button) findViewById(R.id.btn_ce_create);
        btncreate.setText("Save Changes");
        ivprofile = (ImageView)findViewById(R.id.iv_ce_img);
        btngallery = (Button) findViewById(R.id.btn_ce_gallery);
        btntakephoto = (Button) findViewById(R.id.btn_ce_tak);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        sf = FirebaseStorage.getInstance().getReference();
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        db = FirebaseDatabase.getInstance().getReference("Events");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    e = ds.getValue(Events.class);
                    if(e.getId().equals(id))
                    {
                        etname.setText(e.getName());
                        etsub.setText(e.getSub());
                        etseat.setText(String.valueOf(e.getSeat()));
                        etstartdate.setText(e.getStartdate());
                        etenddate.setText(e.getEnddate());
                        etaddress.setText(e.getAddress());
                        final StorageReference ref = sf.child(e.getImgurl());
                        try {
                            final File localFile = File.createTempFile("images", "jpg");
                            ref.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        filepath = Uri.fromFile(localFile);
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                                            RoundedBitmapDrawable rbd= RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                                            rbd.setCircular(true);
                                            ivprofile.setImageDrawable(rbd);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
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
        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,123);
            }
        });

        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),124);

            }
        });
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setMessage("Saving changes");
                prog.show();
                if(!e.getName().equals(etname.getText()))
                {
                    db.child(id).child("name").setValue(etname.getText().toString());
                }
                if(!e.getSub().equals(etsub.getText().toString()))
                {
                    db.child(id).child("sub").setValue(etsub.getText());
                }
                if(e.getSeat() != Integer.parseInt(etseat.getText().toString()))
                {
                    int dif = Integer.parseInt(etseat.getText().toString()) - e.getSeat();
                    db.child(id).child("seat").setValue(Integer.parseInt(etseat.getText().toString()));
                    db.child(id).child("avail").setValue(e.getAvail() + dif);
                }
                if(!e.getStartdate().equals(etstartdate.getText().toString()))
                {
                    db.child(id).child("startdate").setValue(etstartdate.getText().toString());
                }
                if(!e.getEnddate().equals(etenddate.getText().toString()))
                {
                    db.child(id).child("enddate").setValue(etenddate.getText().toString());
                }
                if(!e.getAddress().equals(etaddress.getText().toString()))
                {
                    db.child(id).child("address").setValue(etaddress.getText().toString());
                }
                final StorageReference ref = sf.child(e.getImgurl());
                ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            finish();
                            prog.cancel();
                        }

                    }
                });
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode== RESULT_OK)
        {
            filepath = data.getData();
            if(filepath!=null)
            {

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                    RoundedBitmapDrawable rbd= RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                    rbd.setCircular(true);
                    ivprofile.setImageDrawable(rbd);
                    File f = new File(getExternalCacheDir(),"user.jpeg");
                    try {
                        FileOutputStream fos = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
                        filepath = Uri.fromFile(f);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(requestCode == 124 && resultCode == RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            RoundedBitmapDrawable rbd= RoundedBitmapDrawableFactory.create(getResources(),bitmap);
            rbd.setCircular(true);
            ivprofile.setImageDrawable(rbd);
            File f = new File(getExternalCacheDir(),"user.jpeg");
            try {
                FileOutputStream fos = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
                filepath = Uri.fromFile(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
