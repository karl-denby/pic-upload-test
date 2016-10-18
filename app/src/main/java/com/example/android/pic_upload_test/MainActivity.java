package com.example.android.pic_upload_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtResourceId = (TextView) findViewById(R.id.main_tv_resourceId);
        txtResourceId.setText(getString(R.string.res_id_is, 12345));
    }
}
