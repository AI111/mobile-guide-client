package com.example.sasha.osmdroid.cash.loader;

/**
 * Created by sasha on 12/20/14.
 */
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.types.CityGuide;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class TestFragment extends Fragment {
    Button request;
    TextView text;
    EditText ip;
    EditText port;
    EditText metod;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        request =(Button)rootView.findViewById(R.id.button2);
        text = (TextView)rootView.findViewById(R.id.textView5);
        port = (EditText)rootView.findViewById(R.id.editText2);
        ip=(EditText)rootView.findViewById(R.id.editText);
        metod=(EditText)rootView.findViewById(R.id.editText3);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"hhhhhhhhhhhhh",Toast.LENGTH_SHORT).show();
                new HttpRequestTask().execute();
            }
        });
        return rootView;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_main);
//        button = (Button)findViewById(R.id.button2);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new HttpRequestTask().execute();
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//      //  new HttpRequestTask().execute();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_refresh) {
//            new HttpRequestTask().execute();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */



    private class HttpRequestTask extends AsyncTask<Void, Void, CityGuide[]> {
        String url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            url="http://"+ip.getText().toString()+":"+port.getText().toString()+"/"+metod.getText().toString();
        }

        @Override
        protected CityGuide[] doInBackground(Void... params) {
            try {
                //final String url = "http://"+ip+":8080/getCities";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                CityGuide[]  greeting = restTemplate.getForObject(url, CityGuide[].class);
                Log.v("MainActivity", url);
                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.v("MainActivity", url);
            }

            return null;
        }

        @Override
        protected void onPostExecute(CityGuide[] greeting) {
            text.setText(Arrays.toString(greeting));
            MainActivity.guides=new ArrayList<>(Arrays.asList(greeting));
        }

    }

}
