package com.systemplus.webservice.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.systemplus.webservice.R;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


    }

    protected void hidProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }

    protected void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading data, Please wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
}
