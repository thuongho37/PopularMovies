package com.example.thuon.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by thuon on 3/14/2018.
 */

public class MoreInfoActivity extends AppCompatActivity {

    JSONArray mArray = new JSONArray();
    int position;
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    ArrayList<String> arrayList;// = MainActivity.getListData();
    JSONObject results;// = new JSONObject(arrayList.get(position));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more_info);



        try {
            mArray = MainActivity.getResults(MainActivity.getJsonString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        arrayList = MainActivity.getListData();
        try {
            results = new JSONObject(arrayList.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView mPoster_Thumbnail = (ImageView) findViewById(R.id.movie_poster_thumbnail);
        try {
            Picasso.with(MoreInfoActivity.this)
                    .load("http://image.tmdb.org/t/p/w185/"+results.getString("poster_path"))
                    .into(mPoster_Thumbnail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            populateUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    private void populateUI() throws JSONException {


        TextView mOriginalTitle = (TextView) findViewById(R.id.original_title);
        TextView mPlotSummary = (TextView) findViewById(R.id.plot_summary);
        TextView mUserRating = (TextView) findViewById(R.id.user_rating);
        TextView mReleaseDate = (TextView) findViewById(R.id.release_date);
        mOriginalTitle.setText(results.getString("original_title"));
        mPlotSummary.setText(results.getString("overview"));
        mUserRating.setText(results.getString("vote_average"));
        mReleaseDate.setText(results.getString("release_date"));
}}
