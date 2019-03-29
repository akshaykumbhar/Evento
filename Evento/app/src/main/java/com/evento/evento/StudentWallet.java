package com.evento.evento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentWallet extends AppCompatActivity {
    TextView tv_price;
    FirebaseAuth Auth;
    FirebaseUser user;
    DatabaseReference dbf;
    Student s;
    Boolean flag=true;
    EditText add;
    Button addfund;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_wallet);
        Auth = FirebaseAuth.getInstance();
        tv_price = (TextView) findViewById(R.id.tv_price);
        user = Auth.getCurrentUser();
        add = (EditText) findViewById(R.id.et_add);
        addfund = (Button) findViewById(R.id.w_button4);
        dbf = FirebaseDatabase.getInstance().getReference("Student");
        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot ds : dataSnapshot.getChildren())
                {
                    s = ds.getValue(Student.class);
                    if(s.getEmail().equals(user.getEmail()))
                    {
                        tv_price.setText("\u20B9 "+String.valueOf(s.getWallet()));
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addfund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fund = add.getText().toString();
                if(fund.isEmpty())
                {
                    add.setError("Add Funds");
                    add.requestFocus();
                    return;
                }
                add.setText("");
                Student s1 = new Student(s.getName(),s.getEmail(),s.getCol(),s.getPhone(),s.getUserid(),s.getProuri(),s.getWallet()+Integer.parseInt(fund));
                dbf.child(s.getUserid()).setValue(s1);


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
