package com.evento.evento;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Filter extends AppCompatActivity {
    CheckBox c1,c2,c3,c4,c5,c6,c10,c11;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        c1 = (CheckBox) findViewById(R.id.checkBox);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        c3 = (CheckBox) findViewById(R.id.checkBox3);
        c4 = (CheckBox) findViewById(R.id.checkBox4);
        c5 = (CheckBox) findViewById(R.id.checkBox5);
        c6 = (CheckBox) findViewById(R.id.checkBox6);
        c10 = (CheckBox) findViewById(R.id.checkBox10);
        c11 = (CheckBox) findViewById(R.id.checkBox11);
        btn =(Button) findViewById(R.id.button7);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Android",c1.isChecked());
                intent.putExtra("Java",c2.isChecked());
                intent.putExtra("Python",c3.isChecked());
                intent.putExtra("C++",c4.isChecked());
                intent.putExtra("Web Framework",c5.isChecked());
                intent.putExtra("User Interface",c6.isChecked());
                intent.putExtra("Culture Events",c10.isChecked());
                intent.putExtra("Sport Events",c11.isChecked());
                setResult(RESULT_OK,intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Android",false);
        intent.putExtra("Java",false);
        intent.putExtra("Python",false);
        intent.putExtra("C++",false);
        intent.putExtra("Web Framework",false);
        intent.putExtra("User Interface",false);
        intent.putExtra("Culture Events",false);
        intent.putExtra("Sport Events",false);
        setResult(RESULT_OK,intent);
        finish();

    }
}
