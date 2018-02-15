package com.systemplus.webservice.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

import java.util.List;

public class DexterpermissionActivity extends BaseActivity {
Button btnCamera,btnStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dexterpermission);

        btnStorage=findViewById(R.id.btnSetting);
        btnCamera=findViewById(R.id.btnCamera);


        btnStorage.setOnClickListener( new View.OnClickListener(){
            public void onClick( View view){
                requestStoragePermission();



            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener(){

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);

    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions( Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            showToast("All Permission Granted");
                        }
                        if( report.isAnyPermissionPermanentlyDenied()){
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                showToast("Error occured");
            }
        }).onSameThread().check();
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(DexterpermissionActivity.this);
        builder.setTitle("Need a permissions");
        builder.setMessage("This appNeed a Access for external file location to make a operations");
        builder.setPositiveButton("Goto Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSetting();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        builder.show();
    }

    private void openSetting() {
        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri=Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,101);
    }
}
