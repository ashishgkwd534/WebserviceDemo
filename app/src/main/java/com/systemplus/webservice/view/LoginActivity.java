package com.systemplus.webservice.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.systemplus.webservice.R;

public class LoginActivity extends BaseActivity {
 EditText txt_username,txtpassword;
 Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_username=findViewById(R.id.txtusername);
    }
}
