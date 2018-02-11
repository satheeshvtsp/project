package com.example.suresh.passwordlessauth;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

//import static android.widget.Toast.makeText;

public class loginverify extends AppCompatActivity {
    public static  Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginverify);
        final EditText e2=(EditText)findViewById(R.id.vlcode);
        e2.setText("");
        Intent b1=getIntent();
        if(b1!=null) {
            String value1 = b1.getStringExtra("res");
            final String useremail = b1.getStringExtra("useremail");
            Toast.makeText(this, "verification code sent in mail", Toast.LENGTH_LONG).show();

             b = (Button) findViewById(R.id.blverify);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    b.setEnabled(false);
                    String s = e2.getText().toString();
                    int flag = 0;
                    for (int i = 0; i < s.length(); i++) {
                        if (('0' >= s.charAt(i) && s.charAt(i) <= '9')||('a' >= s.charAt(i) && s.charAt(i) <= 'z')||('A' >= s.charAt(i) && s.charAt(i) <= 'Z') ){
                            continue;
                        } else flag = 0;
                    }
                    if (s == "" || s.length() != 6 || flag == 1) {
                        Toast.makeText(loginverify.this, "Enter valid code", Toast.LENGTH_LONG).show();
                    } else {



                        new PostDataAsynclogverify(loginverify.this).execute(useremail, s);


                    }
                }
            });
        }
    }
    public static void buttondisable(){ b.setEnabled(true);}
}
class PostDataAsynclogverify extends AsyncTask<String, String, String> {
    public Context context;
public PostDataAsynclogverify(Context context){
    this.context=context;
}
    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            postText(strings);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String lenghtOfFile) {
        // do stuff after posting data
    }

    private void postText(String... strings) {
        try {
            String useremail = strings[0];
            String s=strings[1];

            String postReceiverUrl = "http://passwordlessauth.000webhostapp.com/loginverify.php";


            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("verifycode", s));
            nameValuePairs.add(new BasicNameValuePair("useremail", useremail));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // makeText(serverresponse.this, "Wait a Second for response", Toast.LENGTH_LONG).show();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String responseStr = EntityUtils.toString(resEntity).trim();


                if(responseStr.equals("success")){
                    Intent i5;
                    i5 = new Intent(context, frontpage.class);
                    i5.putExtra("res", responseStr);
                    i5.putExtra("useremail",useremail);
                    context.startActivity(i5);}
                else if(responseStr.equals("invalid")){
                    publishProgress("you entered a invalid code");
                   loginverify.buttondisable();

                }
                else if(responseStr.equals("notregistered")){
                    publishProgress("account not found.please create new one");//,Toast.LENGTH_LONG);
                }
                else if(responseStr.equals("notactivated")){
                    publishProgress("Account needs activation");//,Toast.LENGTH_LONG);
                }
                else{
                    publishProgress("Something goes wrong .please do again");
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onProgressUpdate(String... values) {
        if (values != null) {
            // Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            for (String value : values)
                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setMessage(value)
                        .setCancelable(false)
                        .setPositiveButton("ok", null).show();
        }
    }


}