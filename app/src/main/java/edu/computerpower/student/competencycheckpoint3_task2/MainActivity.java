package edu.computerpower.student.competencycheckpoint3_task2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String URL_EARTHQUAKE = "http://api.geonames.org/earthquakesJSON?";
    private static final String URL_COUNTRY = "http://api.geonames.org/findNearbyPlaceNameJSON?";

    String txtResult = "The Largest Earthquake on those coordinates: ";
    Button btnFind;
    Button btnReset;
    EditText edtNorth;
    EditText edtEast;
    EditText edtSouth;
    EditText edtWest;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFind = (Button) findViewById(R.id.btnFind);
        btnReset = (Button) findViewById(R.id.btnReset);
        edtNorth = (EditText) findViewById(R.id.edtNorth);
        edtSouth = (EditText) findViewById(R.id.edtSouth);
        edtEast = (EditText) findViewById(R.id.edtEast);
        edtWest = (EditText) findViewById(R.id.edtWest);
        textView = (TextView) findViewById(R.id.textView);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringUrl = URL_EARTHQUAKE + "north="+edtNorth.getText().toString()+
                        "&south="+edtSouth.getText().toString()+"&east="+edtEast.getText().toString()+
                        "&west="+edtWest.getText().toString()+"&username=mjayadi";

                new JsonEarthquake().execute(stringUrl);

                
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult = "The Largest Earthquake on those coordinates: ";
                textView.setText(txtResult);
                edtNorth.setText("");
                edtWest.setText("");
                edtSouth.setText("");
                edtEast.setText("");
            }
        });

    }

    public void FindEarthquake() {



    }

    public class JsonEarthquake extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String resultAfterJSON = "The Largest Earthquake on those coordinates:  : \n\n";
            textView.setText(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray arr = new JSONArray(jsonObject.getString("earthquakes"));

                // The largest earthquake is at index [0] only
                JSONObject element = arr.getJSONObject(0);
                resultAfterJSON += "Date Time  : " + element.getString("datetime") + "\n";
                resultAfterJSON += "Magnitude  : " + element.getString("magnitude") + "\n";
                String strLng = element.getString("lng");
                String strLat = element.getString("lat");
                resultAfterJSON += "Longitude  : " + strLng + "\n";
                resultAfterJSON += "Latitude   : " + strLat + "\n";
                resultAfterJSON += "Country   : ";

                textView.setText(resultAfterJSON);

                String strUrlCountry = URL_COUNTRY+"lat="+strLat+"&lng="+strLng+"&username=mjayadi";
                new JsonCountry().execute(strUrlCountry);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //DisplayInfo(result);
        }


    }

    public class JsonCountry extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //textView.setText(result);
            DisplayInfo(result);
        }

        private void DisplayInfo(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray arr = new JSONArray(jsonObject.getString("geonames"));
                for (int a = 0; a < arr.length(); a++) {
                    JSONObject element = arr.getJSONObject(a);

                    textView.setText(textView.getText()+ element.getString("countryName"));

                    //Toast.makeText(getBaseContext(), element.getString("countryCode") + ("\n") + element.getString
                    //        ("countryName"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
