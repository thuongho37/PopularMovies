package com.example.thuon.popularmovies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTextView;
    String default_sort = "http://api.themoviedb.org/3/movie/popular?api_key=99f1f5f44123483aa395e7ecb901cd7a";
    static ArrayList<String> listdata = new ArrayList<String>();
    static List<String> posterData = new ArrayList<String>();
    JSONObject results;
    private static MovieAdapter movieAdapter;
    int max_number_of_movies = 19;
    static String githubSearchResults = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.movie_image);
        getURL(default_sort);


    }

    public static ArrayList<String> getListData(){
        return listdata;
    }

    public static JSONArray getResults(String JSONString) throws JSONException {
        posterData.clear();
        listdata.clear();

        JSONObject movieResults = new JSONObject(JSONString);
        JSONArray jArray = (JSONArray)movieResults.getJSONArray("results");
        if (jArray != null) {

            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getString(i));
            }
        }

     return jArray;
    }

    public static String getJsonString(){
        return githubSearchResults;
    }

    void getPosterPath() throws JSONException {


        for(int i = 0;i<listdata.size();i++) {
            results = new JSONObject(listdata.get(i));
            String poster_path = results.getString("poster_path");
            posterData.add(poster_path);
            Log.d("myTag", poster_path);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.sort_popular, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular:

                String sort_popular = "http://api.themoviedb.org/3/movie/popular?api_key=99f1f5f44123483aa395e7ecb901cd7a";
                getURL(sort_popular);

            case R.id.top_rated:

                String sort_top_rated = "http://api.themoviedb.org/3/movie/top_rated?api_key=99f1f5f44123483aa395e7ecb901cd7a";
                getURL(sort_top_rated);


                return true;

            default:

                super.onOptionsItemSelected(item);

        }
        return true;

    }



    private void getURL(String string){
        URL getURL = NetworkUtils.buildUrl(string);

        new GithubQueryTask().execute(getURL);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, String> {


        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)

        @Override

        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];



            try {

                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                getResults(githubSearchResults);


            } catch (IOException e) {

                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return githubSearchResults;

        }


        // COMPLETED (3) Override onPostExecute to display the results in the TextView

        @Override

        protected void onPostExecute(String githubSearchResults) {

            movieAdapter = new MovieAdapter(MainActivity.this, posterData);

            try {
                getPosterPath();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //movieAdapter = new MovieAdapter(MainActivity.this, posterData);



            GridView listView = (GridView) findViewById(R.id.listview_flavor);
            listView.setAdapter(movieAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    launchDetailActivity(position);


                }
            });
        }}

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(MainActivity.this, MoreInfoActivity.class);
        intent.putExtra(MoreInfoActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }
}

