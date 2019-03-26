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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateEvent extends AppCompatActivity {
    EditText etname,etsub,etseat,etstartdate,etenddate,etaddress,etprice;
    Spinner spcategory;
    Button btncreate,btngallery,btntakephoto;
    ImageView ivprofile;
    Uri filepath;
    DatabaseReference db;
    StorageReference sf;
    FirebaseAuth Auth;
    FirebaseUser user;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        etname = (EditText) findViewById(R.id.et_ce_name);
        prog = new ProgressDialog(this);
        etsub = (EditText) findViewById(R.id.et_ce_sub);
        etseat = (EditText) findViewById(R.id.et_ce_seat);
        etstartdate = (EditText) findViewById(R.id.et_ce_sd);
        etenddate = (EditText) findViewById(R.id.et_ce_ed);
        etaddress = (EditText) findViewById(R.id.et_ce_address);
        etprice = (EditText) findViewById(R.id.et_cs_price);
        spcategory = (Spinner) findViewById(R.id.sp_ce_category);
        btncreate = (Button) findViewById(R.id.btn_ce_create);
        ivprofile = (ImageView)findViewById(R.id.iv_ce_img);
        btngallery = (Button) findViewById(R.id.btn_ce_gallery);
        btntakephoto = (Button) findViewById(R.id.btn_ce_tak);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        sf = FirebaseStorage.getInstance().getReference();


        final ArrayList<String> CollegeName = new ArrayList<String>();
        CollegeName.add(0,"Select Category");
        CollegeName.add("Andriod");
        CollegeName.add("Java");
        CollegeName.add("Python");
        CollegeName.add("C++");
        CollegeName.add("Web Framework");
        CollegeName.add("User Interface");
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,CollegeName);
        spcategory.setAdapter(adapter);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filepath == null)
                {
                    Toast.makeText(CreateEvent.this, "Select Image Please", Toast.LENGTH_LONG).show();
                    return;
                }
                final String name = etname.getText().toString();
                if(name.isEmpty())
                {
                    etname.setError("Enter name");
                    etname.requestFocus();
                    return;
                }
                final String sub = etsub.getText().toString();
                if(sub.isEmpty())
                {
                    etsub.setError("Enter SubTitle");
                    etsub.requestFocus();
                    return;
                }
                final String seat = etseat.getText().toString();
                if(seat.isEmpty())
                {
                    etseat.setError("Enter seat number");
                    etseat.requestFocus();
                    return;
                }
                final String sd = etstartdate.getText().toString();
                if(sd.length()!=10)
                {
                    etstartdate.setError("DD/MM/YYYY");
                    etstartdate.requestFocus();
                    return;
                }
                final String ed = etenddate.getText().toString();
                if(ed.length()!=10)
                {
                    etenddate.setError("DD/MM/YYYY");
                    etenddate.requestFocus();
                    return;
                }
                final String add = etaddress.getText().toString();
                if(add.isEmpty())
                {
                    etaddress.setError("Enter Address");
                    etaddress.requestFocus();
                    return;
                }
                final String category = CollegeName.get(spcategory.getSelectedItemPosition());
                if(spcategory.getSelectedItemPosition()==0)
                {
                    Toast.makeText(CreateEvent.this, "Select Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String price = etprice.getText().toString();
                if(price.isEmpty())
                {
                    etprice.setError("Enter Price");
                    etprice.requestFocus();
                    return;
                }
                prog.setTitle("Creating Event");
                prog.setMessage("Please wait");
                prog.show();
                btncreate.setText("Creating");
                btncreate.setClickable(false);

                String s[] = sd.split("/");
                final String id = s[2]+s[1]+s[0]+String.valueOf((int)(Math.random()*100)+10);

                db = FirebaseDatabase.getInstance().getReference("Events");
                final String prourl = "Events/"+id+".jpg";
                StorageReference s1 = sf.child(prourl);
                s1.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Events e = new Events(id,name,sub,prourl,user.getEmail(),Integer.parseInt(seat),Integer.parseInt(seat),sd,ed,add,Integer.parseInt(price),category,"");
                        db.child(id).setValue(e);
                        Toast.makeText(CreateEvent.this, "Event Created", Toast.LENGTH_SHORT).show();
                        prog.cancel();
                        finish();

                    }
                });




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
        super.onBackPressed();
        finish();
    }
}
