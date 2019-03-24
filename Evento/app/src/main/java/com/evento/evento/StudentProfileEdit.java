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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StudentProfileEdit extends AppCompatActivity {
    Button btnsignup;
    TextView tvgallery,tvtakephoto;
    EditText etName,etEmail,etPassword,etPhone;
    Spinner spCollege;
    ImageView ivprofile;
    Uri filepath;
    ProgressDialog prog;
    StorageReference mStorageRef;
    DatabaseReference dbf,df,df1;
    FirebaseAuth Auth;
    FirebaseUser user;
    Student s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnsignup = (Button) findViewById(R.id.btnSignup_Signup);
        tvgallery = (TextView)findViewById(R.id.tvgallery_Signup);
        tvtakephoto = (TextView) findViewById(R.id.tvtakephoto_signup);
        etName  = (EditText) findViewById(R.id.etName_signup);
        etPhone = (EditText) findViewById(R.id.etPhone_signup);
        //spCollege = (Spinner) findViewById(R.id.spCollege_Signup);
        ivprofile = (ImageView) findViewById(R.id.ivProfile_signup);
        prog = new ProgressDialog(this);
        Auth= FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        dbf = FirebaseDatabase.getInstance().getReference("Student");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    s = ds.getValue(Student.class);
                    if(s.getEmail().equals(user.getEmail()))
                    {
                        etName.setText(s.getName());
                        etPhone.setText(s.getPhone());
                       final StorageReference ref =  mStorageRef.child(s.getProuri().toString());
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

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setTitle("Saving Changes");
                prog.show();
                if(!etName.getText().toString().equals(s.getName()))
                {
                    dbf.child(s.getUserid()).child("name").setValue(etName.getText().toString());
                }
                if(!etPhone.getText().toString().equals(s.getPhone()))
                {
                    dbf.child(s.getUserid()).child("phone").setValue(etPhone.getText().toString());
                }
                StorageReference ref = mStorageRef.child(s.getProuri());
                ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            prog.cancel();
                            finish();
                        }
                    }
                });

            }
        });

        tvgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,123);
            }
        });
        tvtakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),124);
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
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                filepath = Uri.fromFile(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


    }
}
