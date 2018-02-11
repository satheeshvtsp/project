package com.example.suresh.passwordlessauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class frontpage extends AppCompatActivity {
TextView e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);

        e=(TextView)findViewById(R.id.verres);
        Intent b1=getIntent();
        if(b1!=null) {
            String value1 = b1.getStringExtra("res");
            if(value1.equals("success")){
                e.setText("Logged in sucessfully");
            }
            else{
                e.setText("not valid");
            }
            Button b = (Button) findViewById(R.id.backmain);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i5;
                    i5 = new Intent(frontpage.this, MainActivity.class);
                    startActivity(i5);
                }
            });
        }



    }
}
