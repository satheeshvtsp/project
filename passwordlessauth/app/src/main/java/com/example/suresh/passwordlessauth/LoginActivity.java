package com.example.suresh.passwordlessauth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LoginActivity extends AppCompatActivity {
    String s,s2;
    private Button b1,b2;
    private static final String TAG = "MyActivity";
    private static final int REQUEST_READ_PHONE_STATE = 1234;
    TelephonyManager telephony;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b1=(Button)findViewById(R.id.blsignup);
        final EditText e=(EditText)findViewById(R.id.logemail);
        b2=(Button)findViewById(R.id.bllogin);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

            e.setText("");
            if (telephony != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                    return;
                } else doimei();
            }
        } else {
            telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            e.setText("");
            s = telephony.getDeviceId();
            s2 = telephony.getDeviceId();

        }


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i1);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b2.setEnabled(false);

                if (!validate(e.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Enter valid Email ", Toast.LENGTH_LONG).show();
                } else {
                    final String imei1, imei2, useremail;
                    imei1 = s;
                    imei2 = s2;
                    useremail = e.getText().toString();
                    PostDataAsync postdataasync=new PostDataAsync(LoginActivity.this);
                    postdataasync.execute(imei1,imei2,useremail);
                }
            }
        });



    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doimei() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            s = null;
            s2 = null;
            int count = telephony.getPhoneCount();
            if (count >= 2) {
                s = telephony.getDeviceId(0);
                s2 = telephony.getDeviceId(1);
                if (s != null && s2 != null) {
                    //e1.setText((CharSequence) s + "HELLO" + s2);
                }
            }
            if (count == 1) {
                s = telephony.getDeviceId();
                s2 = telephony.getDeviceId();
                if (s != null && s2 != null) {
                    // e1.setText((CharSequence) s + "HELLO" + s2);
                }
            }
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
    }

    private boolean validate(String t1) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(t1);
        return matcher.matches();
    }



}


class PostDataAsync extends AsyncTask<String, String, String> {
    private static final String TAG = "MyActivity";
    Context context;
    public PostDataAsync(Context context){
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
    private void postText(String... strings){
        try{
            // url where the data will be posted
            String imei1=strings[0];
            String imei2=strings[1];
            String useremail=strings[2];

            String postReceiverUrl = "http://passwordlessauth.000webhostapp.com/firstcheck.php";
            Log.v(TAG, "postURL: " + postReceiverUrl);

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("imei1", imei1));
            nameValuePairs.add(new BasicNameValuePair("imei2", imei2));
            nameValuePairs.add(new BasicNameValuePair("useremail", useremail));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // execute HTT]P post request
            // makeText(SignupActivity.this, "Wait a Second for response", LENGTH_LONG).show();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);
                //SignupActivity.e1.setText("responsestr"+responseStr);
                if(responseStr.equals("success")){
                    Intent i5=new Intent(context,loginverify.class);
                    i5.putExtra("res",responseStr);
                    i5.putExtra("useremail",useremail);
                    context.startActivity(i5);
                }
                else if(responseStr.equals("activated")){
                    Log.i(TAG,"activated");
                    publishProgress("You Account needs activation");

                }
                else{

                    Log.i(TAG,"activated1");
                    publishProgress( "you don't have a account.please create a account");
                    // dlgAlert.create().show();
                    Log.i(TAG,"activate1d");
                }

                //ProgressDialog Dialog = new ProgressDialog(SignupActivity.this);
                // you can add an if statement here and do other actions based on the response

            }

        } catch (ClientProtocolException e) {

            Log.v(TAG,"some error ----------------------------><");
            e.printStackTrace();
        } catch (IOException e) {
            Log.v(TAG,"dcdcdc->>>>>>>>>>>>>>>.");
            e.printStackTrace();
        }
    }
    @Override
    protected void onProgressUpdate(String... values) {
        if (values != null) {
            for (String value : values) {
                // shows a toast for every value we get
                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setMessage(value)
                        .setCancelable(false)
                        .setPositiveButton("ok", null).show();
                // Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            }
        }
    }

}