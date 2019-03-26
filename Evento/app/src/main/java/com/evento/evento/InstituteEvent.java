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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
public class InstituteEvent extends Fragment{
    Button btncreate ;
    FirebaseAuth Auth;
    FirebaseUser user ;
    DatabaseReference db;
    ArrayList<String> Event,ids;
    Events e;
    ListView lv;

    public InstituteEvent() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institute_event, container, false);
        btncreate = (Button) view.findViewById(R.id.btn_createEvent);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),CreateEvent.class));
            }
        });
        Auth = FirebaseAuth.getInstance();
        lv = (ListView) view.findViewById(R.id.lv_ennt);
        user = Auth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("Events");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(Event != null && ids!=null)
                {
                    Event = null;
                    ids=null;
                }
                Event = new ArrayList<String>();
                ids = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    e = ds.getValue(Events.class);
                    if(e.getEmail().equals(user.getEmail()))
                    {
                        Event.add(e.getName());
                        ids.add(e.getId());
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,Event);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(),InstituteViewCreate.class);
                i.putExtra("id",ids.get(position));
                startActivity(i);
            }
        });


        return  view;
    }


}
