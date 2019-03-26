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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ISignup extends AppCompatActivity {

    Button btnsignup;
    TextView tvgallery,tvtakephoto;
    EditText etName,etEmail,etPassword,etPhone,etAddress;
    ImageView ivprofile;
    Uri filepath;
    ProgressDialog prog;
    StorageReference mStorageRef;
    DatabaseReference dbf,df,df1;
    FirebaseAuth Auth;
    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isignup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnsignup = (Button) findViewById(R.id.btnSignup_ISignup);
        tvgallery = (TextView)findViewById(R.id.tvgallery_ISignup);
        tvtakephoto = (TextView) findViewById(R.id.tvtakephoto_Isignup);
        etName  = (EditText) findViewById(R.id.etName_Isignup);
        etEmail = (EditText) findViewById(R.id.etEmail_Isignup);
        etPassword = (EditText) findViewById(R.id.etPassword_Isignup);
        etPhone = (EditText) findViewById(R.id.etPhone_Isignup);
        ivprofile = (ImageView) findViewById(R.id.ivProfile_Isignup);
        etAddress = (EditText) findViewById(R.id.etAddress_Isignup);
        prog = new ProgressDialog(this);
        Auth= FirebaseAuth.getInstance();
        dbf = FirebaseDatabase.getInstance().getReference("Institute");
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
                final String name = etName.getText().toString();
                if (name.isEmpty()) {
                    etName.setError("Enter Name");
                    etEmail.requestFocus();
                    return;
                }
                final String Email = etEmail.getText().toString().toLowerCase();
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    etEmail.setError("Enter Valid Email");
                    etEmail.requestFocus();
                    return;
                }
                final String Password = etPassword.getText().toString();
                if (Password.length() < 8) {
                    etPassword.setError("Enter Strong Password");
                    etPassword.requestFocus();
                    return;
                }
                final String Address = etAddress.getText().toString();
                if (Address.isEmpty()) {
                    etAddress.setError("Enter Address");
                    etAddress.requestFocus();
                    return;
                }
               final String Phone = etPhone.getText().toString();
                if (Phone.length() < 10) {
                    etPhone.setError("Enter Valid Number");
                    etPhone.requestFocus();
                    return;
                }
                if (filepath == null) {
                    Toast.makeText(ISignup.this, "Please Select Photo.", Toast.LENGTH_SHORT).show();
                    return;
                }
                prog.setTitle("Sign-up");
                prog.setMessage("Please wait");
                prog.show();
                df = FirebaseDatabase.getInstance().getReference("Student");
                df.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for ( DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            Student s = ds.getValue(Student.class);
                            if(s.getEmail().equals(Email) )
                            {
                                if(flag){
                                etEmail.setError("Already Exist");}
                                etEmail.requestFocus();
                                prog.cancel();
                                return;
                            }
                        }
                        df1 = FirebaseDatabase.getInstance().getReference("Institute");
                        df1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for ( DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    Institute s = ds.getValue(Institute.class);
                                    if(s.getEmail().equals(Email))
                                    {
                                        if(flag){
                                        etEmail.setError("Already Exist");}
                                        etEmail.requestFocus();
                                        prog.cancel();
                                        return;
                                    }
                                }
                                int a = (int) (Math.random() * 326548.111223123);
                                flag = false;
                                String userid = String.valueOf(a);
                                String prouri = "InstitueProfile/" + userid + ".jpg";
                                btnsignup.setClickable(false);


                                final Institute user = new Institute(name, Email, Address, Phone, userid, prouri,0);
                                final StorageReference sf = mStorageRef.child(user.getProuri());
                                sf.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            dbf.child(user.getUserid()).setValue(user);
                                            Auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        prog.cancel();
                                                        startActivity(new Intent(ISignup.this,IMainPage.class));
                                                        Toast.makeText(ISignup.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        prog.cancel();
                                                        btnsignup.setClickable(true);
                                                        Toast.makeText(ISignup.this, "Sign-up Failed", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            btnsignup.setClickable(true);
                                            prog.cancel();
                                            Toast.makeText(ISignup.this, "Image failed to upload", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity( new Intent(ISignup.this,MainPage.class));
        finish();
    }
}
