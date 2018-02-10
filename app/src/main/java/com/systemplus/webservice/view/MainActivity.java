package com.systemplus.webservice.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.systemplus.webservice.R;
import com.systemplus.webservice.api.ApiClient;
import com.systemplus.webservice.api.ApiInterface;
import com.systemplus.webservice.model.MovieData;
import com.systemplus.webservice.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView moviesList;

    private static final String API_KEY = "7e8f60e325cd06e164799af1e317d7a7";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        moviesList = findViewById(R.id.moviesList);

        Call<MovieData> call = apiService.getTopRatedMovies(API_KEY);

        showProgressDialog();
        call.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                hidProgressDialog();
                MovieData movieData = response.body();
                List<Result> moviesResult = movieData.getResults();

                MovieClassAdapter movieClassAdapter = new MovieClassAdapter(moviesResult, MainActivity.this);
                moviesList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                moviesList.setAdapter(movieClassAdapter);

            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                hidProgressDialog();
                showToast(t.getMessage());
            }
        });
    }

    private void hidProgressDialog() {
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading data, Please wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
