package com.example.marij.marijasabljic;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int koja=0;
    int proslo=0;
    DBAdapter db;
    String[] rijeci1;
    String[] rijeci2;
    String cijeliTekst1;
    String cijeliTekst2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBAdapter(this);

        int koja=1;
        new DownloadTextTask().execute("https://web.math.pmf.unizg.hr/~karaga/android/sportasidata.txt",
                "http://web.math.pmf.unizg.hr/~karaga/android/sportovidata.txt");

    }



    private String DownloadText(String URL)
    {
        int BUFFER_SIZE = 3000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer))>0) {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private class DownloadTextTask extends AsyncTask< String, Void, String> {

        protected  String doInBackground(String... urls) {
            /*
            int count = urls.length;
            String s="";
            for (int i = 0; i < count; i++) {
                s += DownloadText(urls[i]);

            }
            return s;
            */

            return DownloadText(urls[0]);
        }


        @Override
        protected void onPostExecute(String result) {
            DBAdapter db = new DBAdapter(getBaseContext());

           // Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();

        //ako smo skinuli datoteku s prvog linka



            cijeliTekst1=result;
            rijeci1 = cijeliTekst1.split(" |\\\n");  //razmake i novi red
            int p=0;
            db.open();
            while(p<5)
            {
                int i=p*3;
                TextView txt = (TextView) findViewById(R.id.sportasi);
                txt.append("ID: " + rijeci1[i] + " " +
                        "IME: " + rijeci1[i+1] + " " +
                        "PREZIME:  " + rijeci1[i + 2]+"\n");

                long id = db.insertSportas(Integer.parseInt(rijeci1[i]), rijeci1[i+1], rijeci1[i + 2]);
                p = p + 1;

            }
            db.close();





        /*  NE RADI -- za skidanje s drugog linka
            if(koja==2)
            {
                cijeliTekst2=result;
                rijeci2 = cijeliTekst2.split(" |\\\n");  //razmake i novi red

                int p=0;
                //db.open();
                while(p<5)
                {
                    int i=p*4;
                    TextView txt2 = (TextView) findViewById(R.id.sportovi);
                    txt2.append("ID: " + rijeci2[i] + " " +
                            "NAZIV: " + rijeci2[i+1] + " " +
                            "VRSTA:  " + rijeci2[i + 2] + " " +
                            "OLIMPIJSKI:  " + rijeci2[i + 3]+"\n");
                    //Toast.makeText(getBaseContext(), rijeci1[i], Toast.LENGTH_SHORT).show();
                    //long id = db.insertSportas(Integer.parseInt(rijeci1[i]), rijeci1[i+1], rijeci1[i + 2]);
                    p = p + 1;

                }
                //db.close();

            }
            */



        }
    }
    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }
    public void obrisi( int id)
    {
        db.open();
        boolean i = db.deleteSportas(id);
        db.close();

    }
}



