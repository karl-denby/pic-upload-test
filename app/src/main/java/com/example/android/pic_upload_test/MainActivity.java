package com.example.android.pic_upload_test;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_OPEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtURI = (TextView) findViewById(R.id.main_tv_URI);
        txtURI.setText(getString(R.string.uri_is, "/data/"));

        Button btnUpload = (Button) findViewById(R.id.main_btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();

            TextView txtURI = (TextView) findViewById(R.id.main_tv_URI);
            txtURI.setText(getString(R.string.uri_is, fullPhotoUri.getPath()));

            ImageView ivSelectedFile = (ImageView) findViewById(R.id.main_iv_SelectedFile);
            ivSelectedFile.setImageURI(fullPhotoUri);
        }
    }
}
