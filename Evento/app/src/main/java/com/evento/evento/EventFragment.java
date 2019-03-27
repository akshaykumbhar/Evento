package com.evento.evento;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Collections;

import static com.evento.evento.EventFragment.act;
import static com.evento.evento.EventFragment.cont;
import static com.evento.evento.EventFragment.price;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {


    StorageReference sf;
    FloatingActionButton btnfilter,btnsort;
    DatabaseReference dbf;
    static FirebaseAuth Auth;
  static  FirebaseUser user;
    Events e;
    static ArrayList<String> Org,org1;
    static ArrayList<String> Event,Event1;
    static ArrayList<String> imag,imag1 ;
    static ArrayList<String> id,id1,cate;
    static ArrayList<Integer> price,price1 ;
    static ArrayList<Integer> seat,seat1 ;
    static Context cont;
    static Activity act;
    ListView lv;
   static Resources res;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        cont= getContext();
        act = getActivity();
        res = getResources();
        btnfilter = (FloatingActionButton) view.findViewById(R.id.button2);
      //  btnsort = (Button) view.findViewById(R.id.button3);
        lv = (ListView) view.findViewById(R.id.listView);
        sf = FirebaseStorage.getInstance().getReference();
        dbf = FirebaseDatabase.getInstance().getReference("Events");
        dbf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Org = new ArrayList<String>();
                Event = new ArrayList<String>();
                imag = new ArrayList<String>();
                id = new ArrayList<String>();
                cate = new ArrayList<String>();
                price = new ArrayList<Integer>();
                seat = new ArrayList<Integer>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    e = ds.getValue(Events.class);
                    Org.add(e.getName());
                    Event.add(e.getSub());
                    imag.add(e.getImgurl());
                    id.add(e.getId());
                    cate.add(e.getCategory());
                    price.add(e.getPrice());
                    seat.add(e.getAvail());

                }
                CustomEvent cs = new CustomEvent();
                lv.setAdapter(cs);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Filter.class);
                startActivityForResult(i,123);
            }
        });
     /*   btnsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Sort.class);
                startActivityForResult(i,124);
            }
        });*/



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if(requestCode == 124 && resultCode == getActivity().RESULT_OK)
        {
            int num = data.getIntExtra("num",0);

            switch(num)
            {
                case 0 : break;
                case 1 :
                    seat1 = seat;
                    Collections.sort(seat1);
                    Event1 = Event;
                    id1 = id;
                    imag1 =imag;
                    price1 = price;
                    org1=Org;
                    Event = new ArrayList<String>();
                    Org = new ArrayList<String>();
                    id = new ArrayList<String>();
                    imag = new ArrayList<String>();
                    price = new ArrayList<Integer>();
                    ArrayList<Integer> se = seat;
                    seat = new ArrayList<Integer>();
                   for(int n : seat1)
                   {
                       for(int i = Event1.size()-1;i>=0;i--)
                       {
                           if(se.get(i) == n)
                           {
                               Event.add(Event1.get(i));
                               Org.add(org1.get(i));
                               id.add(id1.get(i));
                               imag.add(imag1.get(i));
                               price.add(price1.get(i));
                               seat.add(se.get(i));
                           }
                       }
                   }
                  /*  Event1 = Event;
                    id1 = id;
                    imag1 =imag;
                    price1 = price;
                    org1=Org;
                    seat1 = seat;
                    Event = new ArrayList<String>();
                    Org = new ArrayList<String>();
                    id = new ArrayList<String>();
                    imag = new ArrayList<String>();
                    price = new ArrayList<Integer>();
                    seat = new ArrayList<Integer>();
                    for(int i = Event.size()-1;i>=0;i++)
                    {
                        Event.add(Event1.get(i));
                        Org.add(org1.get(i));
                        id.add(id1.get(i));
                        imag.add(imag1.get(i));
                        price.add(price1.get(i));
                        seat.add(seat1.get(i));
                    }

                    CustomEvent cs = new CustomEvent();
                    lv.setAdapter(cs);

            }
        }*/


        if(requestCode == 123 && resultCode == getActivity().RESULT_OK);
        {
            org1 = new ArrayList<String>();
            Event1 = new ArrayList<String>();
            id1 = new ArrayList<String>();
            imag1 = new ArrayList<String>();
            price1 = new ArrayList<Integer>();
            seat1 = new ArrayList<Integer>();
            final Boolean c1,c2,c3,c4,c5,c6;
            c1 = data.getBooleanExtra("Android",false);
            c2 = data.getBooleanExtra("Java",false);
            c3 = data.getBooleanExtra("Python",false);
            c4 = data.getBooleanExtra("C++",false);
            c5 = data.getBooleanExtra("Web Framework",false);
            c6 = data.getBooleanExtra("User Interface",false);

           dbf.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   for(DataSnapshot ds : dataSnapshot.getChildren())
                   {
                       Events e1 = ds.getValue(Events.class);
                       org1.add(e1.getName());
                       Event1.add(e1.getSub());
                       id1.add(e1.getId());
                       imag1.add(e1.getImgurl());
                       price1.add(e1.getPrice());
                       seat1.add(e1.getAvail());
                   }
                   Event = new ArrayList<String>();
                   Org = new ArrayList<String>();
                   id = new ArrayList<String>();
                   imag = new ArrayList<String>();
                   price = new ArrayList<Integer>();
                   seat = new ArrayList<Integer>();

                   if(c1 || c2 || c3 || c4 || c5 || c6 )
                   {

                       for(int i = 0 ; i < Event1.size(); i++)
                       {
                           if(c1 && cate.get(i).equals("Android")) {
                               Event.add(Event1.get(i));
                               Org.add(org1.get(i));
                               id.add(id1.get(i));
                               imag.add(imag1.get(i));
                               price.add(price1.get(i));
                               seat.add(seat1.get(i));

                           }
                           if(c2 && cate.get(i).equals("Java")) {
                               Event.add(Event1.get(i));
                               Org.add(org1.get(i));
                               id.add(id1.get(i));
                               imag.add(imag1.get(i));
                               price.add(price1.get(i));
                               seat.add(seat1.get(i));
                           }
                           if(c3 && cate.get(i).equals("Python")) {
                               Event.add(Event1.get(i));
                               Org.add(org1.get(i));
                               id.add(id1.get(i));
                               imag.add(imag1.get(i));
                               price.add(price1.get(i));
                               seat.add(seat1.get(i));
                           }
                           if(c4 && cate.get(i).equals("C++")) {
                               Event.add(Event1.get(i));
                               Org.add(org1.get(i));
                               id.add(id1.get(i));
                               imag.add(imag1.get(i));
                               price.add(price1.get(i));
                               seat.add(seat1.get(i));
                           }
                           if(c5 && cate.get(i).equals("Web Framework")) {
                               Event.add(Event1.get(i));
                               Org.add(org1.get(i));
                               id.add(id1.get(i));
                               imag.add(imag1.get(i));
                               price.add(price1.get(i));
                               seat.add(seat1.get(i));
                           }
                           if(c6 && cate.get(i).equals("User Interface")) {
                               Event.add(Event1.get(i));
                               Org.add(org1.get(i));
                               id.add(id1.get(i));
                               imag.add(imag1.get(i));
                               price.add(price1.get(i));
                               seat.add(seat1.get(i));
                           }

                       }
                       CustomEvent cs = new CustomEvent();
                       lv.setAdapter(cs);

                   }
                   else
                   {

                       for(int i = 0 ; i < Event1.size(); i++)
                       {
                           Event.add(Event1.get(i));
                           Org.add(org1.get(i));
                           id.add(id1.get(i));
                           imag.add(imag1.get(i));
                           price.add(price1.get(i));
                           seat.add(seat1.get(i));

                       }
                       CustomEvent cs = new CustomEvent();
                       lv.setAdapter(cs);
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });


        }
    }
}

class CustomEvent extends BaseAdapter
        {
            StorageReference sf;
            @Override
            public int getCount() {
                return EventFragment.Org.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int i, View view, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(EventFragment.cont);
                sf = FirebaseStorage.getInstance().getReference();
                view = inflater.inflate(R.layout.custevent,null);
                TextView tvname = (TextView) view.findViewById(R.id.tvname);
                TextView tvemail = (TextView) view.findViewById(R.id.tvemail);
                final ImageView ivprofile = (ImageView) view.findViewById(R.id.ivpro);
                Button btn = (Button)view.findViewById(R.id.btnbook);

                tvname.setText(EventFragment.Org.get(i));
                tvemail.setText(EventFragment.Event.get(i));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventFragment.Auth =FirebaseAuth.getInstance();
                        EventFragment.user = EventFragment.Auth.getCurrentUser();
                        if(EventFragment.user == null)
                        {
                            Toast.makeText(EventFragment.cont, "Please Login", Toast.LENGTH_SHORT).show();
                        }
                        else{


                            Intent intent = new Intent(EventFragment.cont,SEventDetails.class);
                            intent.putExtra("id",EventFragment.id.get(i));
                            act.startActivity(intent);
                    }}
                });
                StorageReference ref = sf.child(EventFragment.imag.get(i));
                try {
                    final File localFile = File.createTempFile("images"+String.valueOf(i), "jpg");
                    ref.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Uri filepath = Uri.fromFile(localFile);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(act.getContentResolver(), filepath);
                                    ivprofile.setImageBitmap(bitmap);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return view;
            }
        }
