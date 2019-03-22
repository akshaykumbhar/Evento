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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InstitueProfileEdit extends AppCompatActivity {
    Button btnsignup;
    TextView tvgallery,tvtakephoto;
    EditText etName,etEmail,etPassword,etPhone,etAddress;
    ImageView ivprofile;
    Uri filepath;
    ProgressDialog prog;
    StorageReference sf;
    DatabaseReference db;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institue_profile_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnsignup = (Button) findViewById(R.id.btnSignup_ISignup);
        tvgallery = (TextView)findViewById(R.id.tvgallery_ISignup);
        tvtakephoto = (TextView) findViewById(R.id.tvtakephoto_Isignup);
        etName  = (EditText) findViewById(R.id.etName_Isignup);
        etPhone = (EditText) findViewById(R.id.etPhone_Isignup);
        ivprofile = (ImageView) findViewById(R.id.ivProfile_Isignup);
        etAddress = (EditText) findViewById(R.id.etAddress_Isignup);
        prog = new ProgressDialog(this);
        sf = FirebaseStorage.getInstance().getReference();
        final Intent intent = getIntent();
        etName.setText(intent.getStringExtra("name"));
        etAddress.setText(intent.getStringExtra("address"));
        etPhone.setText(intent.getStringExtra("phone"));
        final StorageReference ref = sf.child(intent.getStringExtra("filepath"));
        try {
            final File localFile = File.createTempFile("images", "jpg");
            ref.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Uri filepath = Uri.fromFile(localFile);
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
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference("Institute");
                if (!etName.getText().equals(intent.getStringExtra("name"))) {
                    db.child(intent.getStringExtra("id")).child("name").setValue(etName.getText().toString());
                }
                if(!etPhone.getText().equals(intent.getStringExtra("phone")))
                {
                    db.child(intent.getStringExtra("id")).child("phone").setValue(etPhone.getText().toString());
                }
                if(!etAddress.getText().equals(intent.getStringExtra("address")))
                {
                    db.child(intent.getStringExtra("id")).child("address").setValue(etAddress.getText().toString());
                }
                ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            finish();
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
