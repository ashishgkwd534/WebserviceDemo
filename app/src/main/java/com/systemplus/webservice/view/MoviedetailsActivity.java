package com.systemplus.webservice.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.systemplus.webservice.R;
import com.systemplus.webservice.model.Genre;
import com.systemplus.webservice.model.MoviesResponse;
import com.systemplus.webservice.model.ProductionCompany;
import com.systemplus.webservice.model.ProductionCountry;
import com.systemplus.webservice.model.Result;
import com.systemplus.webservice.model.SpokenLanguage;

import java.util.List;

public class MoviedetailsActivity extends BaseActivity {
RecyclerView mRecycler;

    List<MoviesResponse> moviesResult;
    List<Genre> moviesGenre;
    List<ProductionCompany> movieCompany;
    List<ProductionCountry> movieContry;
    List<SpokenLanguage> movieLaguage;
    private MovieDetailsAdapter mMovieDetailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);
        Intent intent=getIntent();
        MoviesResponse moviesResponse = intent.getParcelableExtra(MainActivity.MOVIE_DETAILS);
        //Toast.makeText(this, new Gson().toJson(moviesResponse), Toast.LENGTH_SHORT).show();
        //showToast(new Gson().toJson(moviesResponse));
        mRecycler=findViewById(R.id.moviesdetails);
        //moviesResult=moviesResponse.getGenres();
        moviesGenre=moviesResponse.getGenres();
        movieCompany=moviesResponse.getProductionCompanies();
        movieContry=moviesResponse.getProductionCountries();
        movieLaguage=moviesResponse.getSpokenLanguages();

        mMovieDetailsAdapter = new MovieDetailsAdapter(moviesResponse, MoviedetailsActivity.this);
        mRecycler.setLayoutManager(new LinearLayoutManager(MoviedetailsActivity.this));
        mRecycler.setAdapter(mMovieDetailsAdapter);

    }

}
