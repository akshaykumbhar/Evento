package com.evento.evento;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.

 */
public class SeventList extends Fragment {
    ListView lv;
    FirebaseAuth Auth;
    FirebaseUser user ;
    DatabaseReference db,db1;
    static ArrayList<String> events,eid;
    Register reg;
    Events es;

    public SeventList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_sevent_list, container, false);
        lv = (ListView)view.findViewById(R.id.lv_sel);
        Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();

        db = FirebaseDatabase.getInstance().getReference("Register");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events = new ArrayList<String>();
                eid = new ArrayList<String>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    reg = ds.getValue(Register.class);
                    if(reg.getStudentid().equals(user.getEmail()))
                    {
                        events.add(reg.getEname());
                        eid.add(reg.getEventid());
                    }

                }
                ArrayAdapter ad = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,events);
                lv.setAdapter(ad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(),StudentEventView.class);
                i.putExtra("id",eid.get(position));
                startActivity(i);
            }
        });
        return  view;
    }


}
