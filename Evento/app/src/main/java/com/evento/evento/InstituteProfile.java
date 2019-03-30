package com.evento.evento;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstituteProfile extends Fragment {
    FirebaseAuth Auth ;
    FirebaseUser user ;
    DatabaseReference dbr;
    StorageReference sf;
    TextView tvname,tvcol;
    ImageView iv_propic;
    ImageButton ibedit,ibwallet,iblogout,ibabout;
    Institute  i ;
    Boolean flag = true;

    public InstituteProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_institute_profile, container, false);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        tvname = (TextView) view.findViewById(R.id.iptvname);
        sf = FirebaseStorage.getInstance().getReference();
        iv_propic = (ImageView) view.findViewById(R.id.iivProfilePic);
        ibwallet=(ImageButton) view.findViewById(R.id.iiv_imageButton2);
        ibedit = (ImageButton) view.findViewById(R.id.iiv_imageButton);
        ibabout = (ImageButton) view.findViewById(R.id.iimageButton1);
        ibabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),IEditProfile.class));
            }
        });
        ibedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),InstitueProfileEdit.class);
                intent.putExtra("name",i.getName());
                intent.putExtra("address",i.getAddress());
                intent.putExtra("id",i.getUserid());
                intent.putExtra("filepath",i.getProuri());
                intent.putExtra("phone",i.getPhone());
                startActivity(intent);
            }
        });
        ibwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),InstituteWallet.class));
            }
        });
        iblogout = (ImageButton) view.findViewById(R.id.iiv_imageButton3);
        iblogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.signOut();
                startActivity(new Intent(getContext(),MainPage.class));
                getActivity().finish();
            }
        });
        dbr = FirebaseDatabase.getInstance().getReference("Institute");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    i = ds.getValue(Institute.class);
                    if(i.getEmail().equals(user.getEmail()))
                    {
                        if(!flag)
                        {
                            break;
                        }
                        flag = false;
                        tvname.setText(i.getName());
                        StorageReference ref = sf.child(i.getProuri().toString());
                        try {
                            final File localFile = File.createTempFile("images", "jpg");
                            ref.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Uri filepath = Uri.fromFile(localFile);
                                        try {
                                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                                            RoundedBitmapDrawable rbd= RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                                            rbd.setCircular(true);
                                            iv_propic.setImageDrawable(rbd);
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

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
