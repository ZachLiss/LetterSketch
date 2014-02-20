package edu.pitt.cs1635.zll1.prog2;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public void submitClicked(View view) {
        MyCanvas c = (MyCanvas) findViewById(R.id.the_canvas);

        if(c.encoding.equals("")) {
            // then havent entered anything yet
            // make a toast and say so
            Toast.makeText(this, "Make a sketch before submitting!", Toast.LENGTH_LONG).show();
        }else {
            // there is a drawing so we can send it to the server
            // append the end of the encoding
            String e = c.encoding + ", 255, 255]";

            try {
                new SendEncoding().execute(e);
            }catch(Exception ex) {
                // error sending encoding... toast
                Toast.makeText(this, "Error sending your sketch", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void colorChange(View view) {
        MyCanvas c = (MyCanvas) findViewById(R.id.the_canvas);

       // Log.i("color id", "" + view.getId());
        Button b = (Button) findViewById(view.getId());

        Button b1 = (Button) findViewById(R.id.black_button);
        Button b2 = (Button) findViewById(R.id.cyan_button);
        Button b3 = (Button) findViewById(R.id.red_button);
        Button b4 = (Button) findViewById(R.id.green_button);
        Button b5 = (Button) findViewById(R.id.blue_button);
        Button b6 = (Button) findViewById(R.id.yellow_button);

        b1.setText("");
        b2.setText("");
        b3.setText("");
        b4.setText("");
        b5.setText("");
        b6.setText("");

        b.setText("X");


        switch(view.getId()) {
            case R.id.black_button:
                c.setColor(Color.BLACK);
                break;
            case R.id.cyan_button:
                c.setColor(Color.CYAN);
                break;
            case R.id.red_button:
                c.setColor(Color.RED);
                break;
            case R.id.green_button:
                c.setColor(Color.GREEN);
                break;
            case R.id.blue_button:
                c.setColor(Color.BLUE);
                break;
            case R.id.yellow_button:
                c.setColor(Color.YELLOW);
                break;
        }
    }

    public void clearClicked(View view) {
        MyCanvas c = (MyCanvas) findViewById(R.id.the_canvas);
        c.newDrawing();
        TextView t = (TextView) findViewById(R.id.response);
        t.setText("");
    }

    public void aboutClicked(View view) {
        Intent intent = new Intent(this, ShowInfoActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    class SendEncoding extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String key = "11773edfd643f813c18d82f56a8104ed";
            String e = urls[0];

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://cwritepad.appspot.com/reco/usen");
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("key", key));
            nvp.add(new BasicNameValuePair("q", e));
            String message = "";
            try {
                post.setEntity(new UrlEncodedFormEntity(nvp));
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                message = EntityUtils.toString(entity);

                Log.i("Status Line", response.getStatusLine().toString());
                Log.i("Message", message);
            }catch(Exception ex) {
                //well that sucks maybe do a toast?
                Log.i("Error", ex.toString());
            }
            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            TextView t = (TextView) findViewById(R.id.response);
            t.setText(message);
        }
    }
}

