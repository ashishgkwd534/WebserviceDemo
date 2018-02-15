package com.systemplus.webservice.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.systemplus.webservice.R;
import com.systemplus.webservice.api.ApiClient;
import com.systemplus.webservice.api.ApiInterface;

import java.util.List;

public class HomeActivity extends BaseActivity {
Button btnMovie,btnPost,btnGooglrapi,btnPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);





        btnMovie = findViewById(R.id.btnMovie);
        btnPost = findViewById(R.id.btnPost);
        btnGooglrapi = findViewById(R.id.btnGoogleapi);
        btnPermission=findViewById(R.id.btnPermissions);

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
        btnPermission.setOnClickListener( new View.OnClickListener(){
            public void onClick( View view){
                Intent intent=new Intent(HomeActivity.this,DexterpermissionActivity.class);
                startActivity(intent);



            }
        });
    }





}
