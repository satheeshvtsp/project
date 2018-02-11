package com.example.suresh.passwordlessauth;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class verifyrespose extends AppCompatActivity {
TextView e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyrespose);
    e=(TextView)findViewById(R.id.verres);
        Intent b1=getIntent();
        if(b1!=null) {
            String value1 = b1.getStringExtra("res");
if(value1.equals("success")){
    e.setText("registration sucessfully");
}
else{
            e.setText("Registration failed");
}
            Button b = (Button) findViewById(R.id.backmain);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i5;
                    i5 = new Intent(verifyrespose.this, MainActivity.class);
                    startActivity(i5);
                }
            });
        }


    }
}
