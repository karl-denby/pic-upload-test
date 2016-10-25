package com.example.android.pic_upload_test;

import android.content.Intent;
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

    static final int REQUEST_IMAGE_OPEN = 1;
    Bitmap bmp = null;
    String encodedBmp = "";

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

        String loc = "content://media/external/images/media/247";  //pre-kitkat uri
        try {
            bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(loc));
            encodedBmp = encodeToBase64(bmp, Bitmap.CompressFormat.JPEG, 100);
        } catch (Exception e) {
            Log.v("bmp Exception", "Can't make uri string into a bitmap: " + e.toString());
        }
        // set string and image
        txtURI.setText(getString(R.string.uri_is, loc));
        ivSelectedFile.setImageBitmap(decodeBase64(encodedBmp));
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
            txtURI.setText(getString(R.string.uri_is, fullPhotoUri.toString()));  // use string for textbox

            // set image using string version parsed as a URI
            ImageView ivSelectedFile = (ImageView) findViewById(R.id.main_iv_SelectedFile);
            ivSelectedFile.setImageURI(fullPhotoUri); // convert string back to URI
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    // String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
    // Bitmap myBitmapAgain = decodeBase64(myBase64Image);
}
