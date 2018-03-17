package com.karldenby.pic_upload_test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    // Constants
    static final int REQUEST_IMAGE_OPEN = 1;

    // Variables to map to widgets on this activity
    Bitmap bmp;
    String encodedBmp;
    ImageView ivSelectedFile;
    TextView txtURI;
    Button btnUpload;

    // For Save/Load
    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivSelectedFile = (ImageView) findViewById(R.id.main_iv_SelectedFile);
        txtURI = (TextView) findViewById(R.id.main_tv_URI);
        btnUpload = (Button) findViewById(R.id.main_btn_upload);

        txtURI.setText(getString(R.string.uri_is, "No image"));
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // clear out string and image
        txtURI.setText("");
        ivSelectedFile.setImageBitmap(null);

        // save some data
        // Save the dice values
        SharedPreferences sharedPref = context.getSharedPreferences("BMP Test", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bmp", encodedBmp);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Load saved values???
        SharedPreferences sharedPref = context.getSharedPreferences("BMP Test", Context.MODE_PRIVATE);
        encodedBmp = sharedPref.getString("bmp", encodedBmp);

        // set string and image
        if (encodedBmp != null) {
            ivSelectedFile.setImageBitmap(decodeBmpFromBase64(encodedBmp));
            txtURI.setText("Using saved String");
        } else {
            txtURI.setText("Nothing stored");
        }

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

            // set string on txtView
            TextView txtURI = (TextView) findViewById(R.id.main_tv_URI);
            txtURI.setText(getString(R.string.uri_is, fullPhotoUri.toString()));

            // set image using string version parsed as a URI
            ImageView ivSelectedFile = (ImageView) findViewById(R.id.main_iv_SelectedFile);
            ivSelectedFile.setImageURI(fullPhotoUri); // convert string back to URI

            // Now turn our Bitmap into a String
            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
                encodedBmp = encodeBmpToBase64(bmp, Bitmap.CompressFormat.JPEG, 100);
                Log.v("BMP encode: ", "Success");
            } catch (Exception e) {
                Log.v("BMP encode: ", "Error: " + e.toString());
            }

        }
    }

    private static String encodeBmpToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private static Bitmap decodeBmpFromBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
