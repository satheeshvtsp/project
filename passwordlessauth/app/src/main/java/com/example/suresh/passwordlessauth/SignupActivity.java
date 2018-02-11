package com.example.suresh.passwordlessauth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v4.content.ContextCompat.startActivity;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private static final int REQUEST_READ_PHONE_STATE = 1234;
    public static EditText e1;
    private Button b1;
    private Button b2;
    static String t1;
    String s=null;
    String s2=null;
    //String server_url = "http://127.0.0.1/projectpassword/mod1.php";

    AlertDialog.Builder builder;

    TelephonyManager telephony;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        b1 = (Button) findViewById(R.id.bssignup);
        e1 = (EditText) findViewById(R.id.e1);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

            e1.setText("");
            if (telephony != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                    return;
                } else doimei();
            }
        } else {
            telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            e1.setText("");
            s = telephony.getDeviceId();
          s2 = telephony.getDeviceId();

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setEnabled(false);
                initial();
                if (!validate()) {
                    Toast.makeText(SignupActivity.this, "Enter valid Email ", Toast.LENGTH_SHORT).show();
                } else {
                    final String imei1, imei2, useremail;
                    imei1 = s;
                    imei2 = s2;
                    useremail = e1.getText().toString();
                   PostDataAsyncTask postdataasynctask=new PostDataAsyncTask(SignupActivity.this);
                   postdataasynctask.execute(imei1,imei2,useremail);
                }
                    }
        });

                b2 = (Button) findViewById(R.id.bslogin);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i1 = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(i1);
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

    private boolean validate() {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(t1);
        return matcher.matches();
    }
    private void initial() {

        t1=e1.getText().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    doimei();
                }
                else{
                    b1.setEnabled(false);
                }
                break;

            default:
                break;
        }

    }


}
class PostDataAsyncTask extends AsyncTask<String, String, String> {
    public static final String TAG = "MyActivity1";
    Context context;
    public PostDataAsyncTask(Context context){
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
                    Intent i5=new Intent(context,serverresponse.class);
                    i5.putExtra("res",responseStr);
                    i5.putExtra("useremail",useremail);
                   context.startActivity(i5);
                }
                else if(responseStr.equals("activated")){
                    Log.i(TAG,"activated");
                    //Toast.makeText(context, "Already you haven account.", Toast.LENGTH_LONG).show();
                    publishProgress("Already you haven account.pls login with that");
                }
                else{

                    Log.i(TAG,"activated1");
                    publishProgress("Please Do again.Some error happen");
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