package com.example.android.pic_upload_test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_OPEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivSelectedFile = (ImageView) findViewById(R.id.main_iv_SelectedFile);
        TextView txtURI = (TextView) findViewById(R.id.main_tv_URI);
        txtURI.setText(getString(R.string.uri_is, "No image"));

        Button btnUpload = (Button) findViewById(R.id.main_btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        String loc = "content://media/external/images/media/19";  //pre-kitkat uri
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(loc));
        } catch (Exception e) {
            Log.v("bmp Exception", "Can't make uri string into a bitmap: " + e.toString());
        }
        // set string and image
        txtURI.setText(getString(R.string.uri_is, loc));
        ivSelectedFile.setImageBitmap(bmp);
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            // Get URI
            // to String
            Uri fullPhotoUri = data.getData();
            String filePath = convertUri(Uri.decode(fullPhotoUri.toString()));

            // set string on txtView
            TextView txtURI = (TextView) findViewById(R.id.main_tv_URI);
            txtURI.setText(getString(R.string.uri_is, filePath ));  // use string for textbox

            // set image using string version parsed as a URI
            ImageView ivSelectedFile = (ImageView) findViewById(R.id.main_iv_SelectedFile);
            Log.v("URI",filePath);
            ivSelectedFile.setImageURI(Uri.parse(filePath)); // convert string back to URI
        }
    }

    private String convertUri(String badUri) {
        String goodUri = "content://media/external/images/media/";

        // First run gives us up to ':' after content
        int startImagePosition = badUri.indexOf(":") + 1;
        int endImagePosition = badUri.length();
        String imageNumber = badUri.substring(startImagePosition, endImagePosition);

        // Second run should give us the ':' we want
        startImagePosition = imageNumber.indexOf(":") + 1;
        endImagePosition = imageNumber.length();
        imageNumber = imageNumber.substring(startImagePosition, endImagePosition);

        return goodUri + imageNumber;
    }
}
