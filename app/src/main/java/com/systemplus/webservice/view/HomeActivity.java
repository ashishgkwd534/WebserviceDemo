package com.systemplus.webservice.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.systemplus.webservice.R;
import com.systemplus.webservice.api.ApiClient;
import com.systemplus.webservice.api.ApiInterface;

public class HomeActivity extends BaseActivity {
Button btnMovie,btnPost,btnGooglrapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnMovie = findViewById(R.id.btnMovie);
        btnPost = findViewById(R.id.btnPost);
        btnGooglrapi = findViewById(R.id.btnGoogleapi);

        btnMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
