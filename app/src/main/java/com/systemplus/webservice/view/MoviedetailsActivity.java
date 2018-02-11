package com.systemplus.webservice.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.systemplus.webservice.R;
import com.systemplus.webservice.model.MoviesResponse;

public class MoviedetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);
        Intent intent=getIntent();
        MoviesResponse moviesResponse = intent.getParcelableExtra(MainActivity.MOVIE_DETAILS);
        //Toast.makeText(this, new Gson().toJson(moviesResponse), Toast.LENGTH_SHORT).show();
        showToast(new Gson().toJson(moviesResponse));
    }

}
