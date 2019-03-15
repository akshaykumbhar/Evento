package com.evento.evento;

import android.content.Context;
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


/**
 * A simple {@link Fragment} subclass.

 */
public class StudentProfile extends Fragment {
    TextView tvname,tvcolname;
    ImageView profilepic;
    FirebaseUser user ;
    FirebaseAuth Auth;
    StorageReference sf;
    DatabaseReference dbf;
    Student s;
    public StudentProfile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        tvname = (TextView) view.findViewById(R.id.ptvname);
        tvcolname = (TextView) view.findViewById(R.id.ptvcolname);
        profilepic = (ImageView) view.findViewById(R.id.ivProfilePic);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();
        sf = FirebaseStorage.getInstance().getReference();
        if (user != null)
        {
            dbf = FirebaseDatabase.getInstance().getReference("Student");
            dbf.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Toast.makeText(getContext(), "checking", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(),user.getEmail(), Toast.LENGTH_SHORT).show();
                        s = ds.getValue(Student.class);
                        Toast.makeText(getContext(),s.getEmail(), Toast.LENGTH_SHORT).show();
                        if(s.getEmail().equals(user.getEmail()))
                        {
                            Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                            tvname.setText(s.getName().toString());
                            tvcolname.setText(s.getCol().toString());
                            StorageReference ref = sf.child(s.getProuri().toString());
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
                                                profilepic.setImageDrawable(rbd);
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
        }


        return view;
    }


}
