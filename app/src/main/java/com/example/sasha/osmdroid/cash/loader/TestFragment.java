package com.example.sasha.osmdroid.cash.loader;

/**
 * Created by sasha on 12/20/14.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.database.HelperFactory;
import com.example.sasha.osmdroid.types.CityGuide;
import com.example.sasha.osmdroid.types.CustomGeoPoint;
import com.j256.ormlite.table.TableUtils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TestFragment extends Fragment {
    Button request, save, show, clear;
    TextView text;
    EditText ip;
    EditText port;
    EditText metod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        request = (Button) rootView.findViewById(R.id.button2);
        text = (TextView) rootView.findViewById(R.id.textView5);
        save = (Button) rootView.findViewById(R.id.button3);
        port = (EditText) rootView.findViewById(R.id.editText2);
        show = (Button) rootView.findViewById(R.id.button4);
        clear = (Button) rootView.findViewById(R.id.button5);
        ip = (EditText) rootView.findViewById(R.id.editText);
        metod = (EditText) rootView.findViewById(R.id.editText3);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
                new HttpRequestCytiesList().execute();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CityGuide a : MainActivity.guides) {
                    try {
                        HelperFactory.getHelper().getCityGuideDAO().create(a);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    text.setText("DB = " + HelperFactory.getHelper().getCityGuideDAO().getAllCities());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), CityGuide.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //HelperFactory.getHelper().getCityGuideDAO().
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
//            getActivity().getMenuInflater().inflate(R.menu.test, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_request:
                new HttpRequestCytiesList().execute();
                break;
            case R.id.clear_db:
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), CityGuide.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), CustomGeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.show_db:
                try {
                    text.setText("DB = " + HelperFactory.getHelper().getCityGuideDAO().getAllCities() + "\n----------\n" + HelperFactory.getHelper().getCustomGeoPointDAO().getAllPoints());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.clear_all:
                try {
                    TableUtils.dropTable(HelperFactory.getHelper().getConnectionSource(), CityGuide.class, true);
                    TableUtils.dropTable(HelperFactory.getHelper().getConnectionSource(), CustomGeoPoint.class, true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), CityGuide.class);
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), CustomGeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.list_save:
                for (CityGuide a : MainActivity.guides) {
                    try {
                        HelperFactory.getHelper().getCityGuideDAO().create(a);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.get_points:
                new HttpRequestPoints().execute();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    private class HttpRequestCytiesList extends AsyncTask<Void, Void, CityGuide[]> {
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            url = "http://" + ip.getText().toString() + ":" + port.getText().toString() + "/" + metod.getText().toString();
        }

        @Override
        protected CityGuide[] doInBackground(Void... params) {
            try {
                //final String url = "http://"+ip+":8080/getCities";
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                CityGuide[] greeting = restTemplate.getForObject(url, CityGuide[].class);
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
            MainActivity.guides = new ArrayList<>(Arrays.asList(greeting));
        }

    }

    private class HttpRequestPoints extends AsyncTask<Void, Void, CustomGeoPoint[]> {
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            url = "http://" + ip.getText().toString() + ":" + port.getText().toString() + "/" + "getPoints?id=2";
        }

        @Override
        protected CustomGeoPoint[] doInBackground(Void... params) {
            try {
                //final String url = "http://"+ip+":8080/getCities";
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                CustomGeoPoint[] geoPoints = restTemplate.getForObject(url, CustomGeoPoint[].class);
                Log.v("MainActivity", url);
                return geoPoints;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Log.v("MainActivity", url);
            }

            return null;
        }

        @Override
        protected void onPostExecute(CustomGeoPoint[] geoPoints) {
            //text.setText(Arrays.toString(geoPoints));
            CityGuide cityGuide = MainActivity.guides.get(0);
            for (CustomGeoPoint point : geoPoints) {
                try {
                    cityGuide.addPoint(point);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            text.setText(cityGuide.toString());
            try {
                HelperFactory.getHelper().getCityGuideDAO().create(cityGuide);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
