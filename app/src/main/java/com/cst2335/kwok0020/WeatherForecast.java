package com.cst2335.kwok0020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    TextView min;
    TextView max;
    TextView currentTem;
    TextView UV;
    ImageView img;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notround);

        currentTem = findViewById(R.id.currentTem);
        max = findViewById(R.id.maxTem);
        min = findViewById(R.id.minTem);
        UV = findViewById(R.id.UVrate);
        img = findViewById(R.id.imageView2);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


       ForecastQuery req = new ForecastQuery();
       req.execute();
        //ForecastQuery req2 = new ForecastQuery();
        //req2.execute("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");


    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{


        String current;
        String minT;
        String maxT;
        String uvR;
        String iconName;
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sun);
        Bitmap pic;

        public String doInBackground(String ... args) {

            try {
                //create a URL object of what server to contact:
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //Creating a Pull parser uses the Factory pattern:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if (xpp.getName().equals("temperature")) {
                            current = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            minT = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxT = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (xpp.getName().equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                if(fileExistance(iconName + ".png")){
                    FileInputStream fis = null;
                    try{
                        fis = openFileInput(iconName + ".png");
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                    pic = BitmapFactory.decodeStream(fis);
                    Log.e("IFFound", iconName + ".png file found locally");
                }
                else{
                    URL url2 = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                    urlConnection = (HttpURLConnection) url2.openConnection();
                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if(responseCode == 200){
                        pic = BitmapFactory.decodeStream(urlConnection.getInputStream());

                        FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                        pic.compress(Bitmap.CompressFormat.PNG,80,outputStream);
                        outputStream.flush();
                        outputStream.close();

                        publishProgress(100);
                        Log.e("IFFound", iconName + ".png file not found locally, download it");
                    }
                }

                URL url3 = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                urlConnection = (HttpURLConnection) url3.openConnection();
                response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject uvReport = new JSONObject(result);
                uvR = uvReport.getString("value");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            progressBar.setVisibility(View.INVISIBLE);
            currentTem.setText(current);
            min.setText(minT);
            max.setText(maxT);
            UV.setText(uvR);
            img.setImageBitmap(pic);


            Log.i("HTTP", fromDoInBackground);
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }
}