package com.evento.evento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Sort extends AppCompatActivity {
    Button btnapply;
    CheckBox c1,c2,c3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        btnapply = (Button) findViewById(R.id.button8);
        c1 = (CheckBox) findViewById(R.id.checkBox7);
        c2 = (CheckBox) findViewById(R.id.checkBox8);
        c3 = (CheckBox) findViewById(R.id.checkBox9);

        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {


                    c2.setChecked(false);
                    c3.setChecked(false);
                }
            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {


                    c1.setChecked(false);
                    c3.setChecked(false);
                }
            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {


                    c2.setChecked(false);
                    c1.setChecked(false);
                }
            }
        });



        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                int num =0 ;
                if(c1.isChecked())
                {
                  num = 1;
                }
                if(c2.isChecked())
                {
                    num = 2;
                }
                if(c3.isChecked())
                {
                    num = 3;
                }
                i.putExtra("num",num);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        int num =0 ;
        i.putExtra("num",num);
        setResult(RESULT_OK,i);
        finish();
    }
}
