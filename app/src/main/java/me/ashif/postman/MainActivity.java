package me.ashif.postman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;
    private String incomingDataContent;
    private String senderID;
    private ListView incomingDataListView;
    private DataAdapter listAdapter;
    private List<IncomingData> dataList;
    private IncomingData data;
    TextView statusText;
    private BroadcastReceiver intentReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            incomingDataContent = intent.getExtras().getString("sms");
            senderID = intent.getExtras().getString("senderID");
            data = new IncomingData();
            data.setIncomingData(incomingDataContent);
            data.setSenderID(senderID);
            data.setStatus("Completed");
            checkChannel(incomingDataContent);
            dataList.add(data);
            listAdapter.notifyDataSetChanged();
            new PostRequest().execute();
            Toast.makeText(getApplicationContext(),"Initiating HTTP Request",Toast.LENGTH_SHORT).show();
        }
    };

    private void checkChannel(String incomingDataContent) {
        if (incomingDataContent.contains("bing.com/search?q=")) {
            data.setChannel("Google");
        } else if (incomingDataContent.contains("wikipedia.org/wiki/")) {
            data.setChannel("Wikipedia");
        } else
            data.setChannel("Web");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        Toast.makeText(getApplicationContext(), "Listening for Incoming Messages", Toast.LENGTH_LONG).show();

        incomingDataListView = (ListView) findViewById(R.id.IncomingDataList);
        dataList = new ArrayList<>();
        listAdapter = new DataAdapter(this, dataList);
        incomingDataListView.setAdapter(listAdapter);
        Collections.reverse(dataList);

        statusText = (TextView) findViewById(R.id.txtStatusContent);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listAdapter.getCount() > 0) {
                    incomingDataListView.setAdapter(null);
                    Toast.makeText(getApplicationContext(), "Incoming List Cleared", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Nothing to Clear", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent callAboutIntent = new Intent(this,AboutActivity.class);
            startActivity(callAboutIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReciever, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReciever);
        super.onPause();
    }

    class PostRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(
                    "http://murmuring-ridge-67301.herokuapp.com/");
            List<NameValuePair> nameValuePair = new ArrayList<>(2);
            nameValuePair.add(new BasicNameValuePair("content", incomingDataContent));
            nameValuePair.add(new BasicNameValuePair("from_number", senderID));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // writing error to Log
                e.printStackTrace();
            }
            // Making HTTP Request
            try {
                HttpResponse response = httpClient.execute(httpPost);
                // writing response to log
                Log.d("Http Response:", response.toString());
            } catch (ClientProtocolException e) {
                // writing exception to log
                e.printStackTrace();
            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();

            }
            return null;
        }
    }
}
